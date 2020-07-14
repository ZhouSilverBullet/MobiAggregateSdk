package com.mobi.ks.wrapper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.kwad.sdk.KsAdSDK;
import com.kwad.sdk.export.i.IAdRequestManager;
import com.kwad.sdk.export.i.KsFeedAd;
import com.kwad.sdk.protocol.model.AdScene;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.IAdView;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.utils.LogUtils;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 18:03
 * @Dec csj wrapper
 */
public class NativeExpressAdWrapper extends BaseAdWrapper implements IAdView, IAdRequestManager.FeedAdListener, KsFeedAd.AdInteractionListener {
    public static final String TAG = "CsjNativeExpressAd";
    private final BaseAdProvider mAdProvider;
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    Context mContext;
    ViewGroup mViewContainer;
    IExpressListener mListener;
    private List<KsFeedAd> mList;

    public NativeExpressAdWrapper(BaseAdProvider adProvider,
                                  Context context,
                                  ViewGroup viewContainer,
                                  LocalAdParams adParams,
                                  IExpressListener listener) {
        mContext = context;
        mAdProvider = adProvider;
        mAdParams = adParams;
        mViewContainer = viewContainer;
        mListener = listener;

        mMobiCodeId = mAdParams.getMobiCodeId();

        if (mAdProvider != null) {
            mProviderType = mAdProvider.getProviderType();
        }
    }

    private void createNativeExpressAD() {
        String postId = mAdParams.getPostId();
        if (checkPostIdEmpty(mAdProvider, postId)) {
            return;
        }

        AdScene scene = new AdScene(getPostId(postId));
        scene.adNum = mAdParams.getAdCount();
        KsAdSDK.getAdManager().loadFeedAd(scene, this);

    }


    @Override
    public void onError(int code, String message) {
        localExecFail(mAdProvider, code, message);
    }

    @Override
    public void onFeedAdLoad(@Nullable List<KsFeedAd> list) {
        if (list == null || list.size() == 0) {
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_LOAD_EMPTY_ERROR, "没有对应的广告");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad();
        }

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "CsjNativeExpressAd load isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "CsjNativeExpressAd load isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }


        //任务执行成功
        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        mList = list;

        for (KsFeedAd ksFeedAd : list) {
            ksFeedAd.setAdInteractionListener(this);
        }

        if (mAdProvider != null) {
            mAdProvider.callbackExpressLoad(mListener, this, mAdParams.isAutoShowAd());
        }
    }


//    @Override
//    public void onRenderFail(View view, String message, int code) {
////        localExecFail(mAdProvider, code, message);
//        localRenderFail(mAdProvider, code, message);
//    }
//
//    @Override
//    public void onRenderSuccess(View view, float v, float v1) {
//
//        if (mAdProvider != null) {
//            mAdProvider.callbackExpressRenderSuccess(mListener);
//            mAdProvider.trackRenderSuccess();
//        }
//    }


    /**
     * 创建任务执行
     */
    @Override
    public void run() {
        if (mAdProvider != null) {
            mAdProvider.trackEventStart();
        }
        createNativeExpressAD();
    }

    @Override
    public int getStyleType() {
        return MobiConstantValue.STYLE.NATIVE_EXPRESS;
    }

    @Override
    public void show() {
        if (mList == null) {
            return;
        }

        if (mViewContainer != null) {
            if (mViewContainer.getVisibility() != View.VISIBLE) {
                mViewContainer.setVisibility(View.VISIBLE);
            }
            //防止广告重叠显示
            if (mViewContainer.getChildCount() > 0) {
                mViewContainer.removeAllViews();
            }

            for (KsFeedAd view : mList) {
                mViewContainer.addView(view.getFeedView(mContext));
            }
        }

        if (mAdProvider != null) {
            mAdProvider.trackStartShow();
        }
    }

    @Override
    public void onDestroy() {
        mContext = null;
        if (mList != null) {
            mList.clear();
        }
    }

    @Override
    public void onAdClicked() {
        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick();
            mAdProvider.callbackExpressClick(mListener);
        }
    }

    @Override
    public void onAdShow() {
        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow();
            mAdProvider.callbackExpressShow(mListener);
        }
    }

    @Override
    public void onDislikeClicked() {
        if (mAdProvider != null) {
            mAdProvider.trackEventClose();
            mAdProvider.callbackExpressDismissed(mListener);
        }
    }
}
