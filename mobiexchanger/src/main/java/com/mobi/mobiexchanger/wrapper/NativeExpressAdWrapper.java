package com.mobi.mobiexchanger.wrapper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.adsdk.MobiSdk;
import com.mobi.adsdk.ads.MobiAdSlot;
import com.mobi.adsdk.ads.nativeexpress.MobiNativeExpressAd;
import com.mobi.adsdk.ads.nativeexpress.MobiNativeExpressAdListener;
import com.mobi.adsdk.net.ads.nativead.MobiNativeExpressAD;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.ExpressAdView;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.utils.LogUtils;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 18:03
 * @Dec csj wrapper
 */
public class NativeExpressAdWrapper extends BaseAdWrapper implements ExpressAdView, MobiNativeExpressAdListener, MobiNativeExpressAd {
    public static final String TAG = "CsjNativeExpressAd";
    private final BaseAdProvider mAdProvider;
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    Context mContext;
    ViewGroup mViewContainer;
    IExpressListener mListener;
    private List<MobiNativeExpressAD> mList;

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

        MobiAdSlot slot = new MobiAdSlot.Builder()
                .setCodeId(postId)
                .setExpressViewAcceptedSize(mAdParams.getExpressViewWidth(), mAdParams.getExpressViewHeight())
                .build();

        MobiSdk.loadMobiNativeExpressAd(mContext, slot, this);

    }


    @Override
    public void onError(int code, String message) {

//        if (mViewContainer != null) {
//            mViewContainer.removeAllViews();
//        }

        localExecFail(mAdProvider, code, message);

    }

    @Override
    public void onNativeExpressAdLoad(List<MobiNativeExpressAD> list) {

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
        for (MobiNativeExpressAD mobiNativeExpressAD : mList) {
            mobiNativeExpressAD.setMobiNativeExpressAdListenner(this);
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

//
//    @Override
//    public void onRenderFail(View view, String message, int code) {
////        localExecFail(mAdProvider, code, message);
//
//    }
//
//    @Override
//    public void onRenderSuccess(View view, float v, float v1) {
//
//
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
        for (MobiNativeExpressAD view : mList) {
            ((MobiNativeExpressAD) view).render();
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
    public void onRenderFail() {
        localRenderFail(mAdProvider, 0, "mobi渲染失败");
    }

    @Override
    public void onRenderSuccess(View expressAdView) {
        if (mViewContainer != null) {
            if (mViewContainer.getVisibility() != View.VISIBLE) {
                mViewContainer.setVisibility(View.VISIBLE);
            }
            //防止广告重叠显示
            if (mViewContainer.getChildCount() > 0) {
                mViewContainer.removeAllViews();
            }
            if (expressAdView != null) {
                mViewContainer.addView(expressAdView);
            }
        }

        if (mAdProvider != null) {
            mAdProvider.callbackExpressRenderSuccess(mListener);
            mAdProvider.trackRenderSuccess();
        }
    }
}
