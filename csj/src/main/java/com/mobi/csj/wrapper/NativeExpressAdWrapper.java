package com.mobi.csj.wrapper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.ExpressAdView;
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
public class NativeExpressAdWrapper extends BaseAdWrapper implements ExpressAdView, TTAdNative.NativeExpressAdListener, TTNativeExpressAd.ExpressAdInteractionListener, TTAppDownloadListener {
    public static final String TAG = "CsjNativeExpressAd";
    private final BaseAdProvider mAdProvider;
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    Context mContext;
    ViewGroup mViewContainer;
    IExpressListener mListener;
    private List<TTNativeExpressAd> mList;

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


        TTAdNative mTTAdNative = createAdNative(mContext);

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mAdParams.getPostId())
                .setSupportDeepLink(mAdParams.isSupportDeepLink())
                .setAdCount(getLoadCount(mAdParams.getAdCount()))
                .setImageAcceptedSize(mAdParams.getImageWidth(), mAdParams.getImageHeight())
                .setExpressViewAcceptedSize(mAdParams.getExpressViewWidth(), mAdParams.getExpressViewHeight())
                .build();

        mTTAdNative.loadNativeExpressAd(adSlot, this);
    }


    @Override
    public void onError(int code, String message) {

//        if (mViewContainer != null) {
//            mViewContainer.removeAllViews();
//        }

        localExecFail(mAdProvider, code, message);

    }

    @Override
    public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {

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

//        CsjExpressAdViewImpl expressAdView = null;
//        if (mAdParams.isAutoShowAd()) {
//            for (TTNativeExpressAd ttNativeExpressAd : list) {
//                ttNativeExpressAd.setExpressInteractionListener(this);
//                //这里default
//                if (ttNativeExpressAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
//                    setAppDownloadListener(mListener);
//                    ttNativeExpressAd.setDownloadListener(this);
//                }
//                ttNativeExpressAd.render();
//            }
//        } else {
//            LogUtils.e(TAG, "CsjNativeExpressAd load not show");
//            for (TTNativeExpressAd ttNativeExpressAd : list) {
//                ttNativeExpressAd.setExpressInteractionListener(this);
//                //这里default
//                if (ttNativeExpressAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
//                    setAppDownloadListener(mListener);
//                    ttNativeExpressAd.setDownloadListener(this);
//                }
//            }
//            expressAdView = new CsjExpressAdViewImpl(list);
//        }
        mList = list;

        for (TTNativeExpressAd ttNativeExpressAd : list) {
            ttNativeExpressAd.setExpressInteractionListener(this);
            //这里default
            if (ttNativeExpressAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                setAppDownloadListener(mListener);
                ttNativeExpressAd.setDownloadListener(this);
            }
        }

        if (mAdProvider != null) {
            mAdProvider.callbackExpressLoad(mListener, this, mAdParams.isAutoShowAd());
        }
    }

//    @Override
//    public void onAdDismiss() {
//        if (mAdProvider != null) {
//            mAdProvider.trackEventClose(getStyleType());
//            mAdProvider.callbackExpressDismissed(mListener);
//        }
//    }

    @Override
    public void onAdClicked(View view, int i) {
        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick();
            mAdProvider.callbackExpressClick(mListener);
        }
    }

    @Override
    public void onAdShow(View view, int i) {
        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow();
            mAdProvider.callbackExpressShow(mListener);
        }
    }

    @Override
    public void onRenderFail(View view, String message, int code) {
//        localExecFail(mAdProvider, code, message);
        localRenderFail(mAdProvider, code, message);
    }

    @Override
    public void onRenderSuccess(View view, float v, float v1) {

        if (mViewContainer != null) {
            if (mViewContainer.getVisibility() != View.VISIBLE) {
                mViewContainer.setVisibility(View.VISIBLE);
            }
            //防止广告重叠显示
            if (mViewContainer.getChildCount() > 0) {
                mViewContainer.removeAllViews();
            }
            mViewContainer.addView(view);
        }

        if (mAdProvider != null) {
            mAdProvider.callbackExpressRenderSuccess(mListener);
            mAdProvider.trackRenderSuccess();
        }
    }


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
        for (TTNativeExpressAd view : mList) {
            ((TTNativeExpressAd) view).render();
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
}
