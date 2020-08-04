package com.mobi.csj.wrapper;

import android.app.Activity;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.IAdView;
import com.mobi.core.feature.InteractionAdView;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.utils.LogUtils;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 22:27
 * @Dec 略
 */
public class InteractionExpressAdWrapper extends BaseAdWrapper implements InteractionAdView, TTAdNative.NativeExpressAdListener, TTNativeExpressAd.AdInteractionListener {
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    private BaseAdProvider mAdProvider;
    private Activity mActivity;
    private IInteractionAdListener mListener;
    private List<TTNativeExpressAd> mTTNativeExpressAds;

    public InteractionExpressAdWrapper(BaseAdProvider baseAdProvider,
                                       Activity activity,
                                       LocalAdParams adParams,
                                       IInteractionAdListener listener) {

        mAdProvider = baseAdProvider;
        mActivity = activity;
        mAdParams = adParams;
        mListener = listener;

        mMobiCodeId = mAdParams.getMobiCodeId();

        if (baseAdProvider != null) {
            mProviderType = baseAdProvider.getProviderType();
        }
    }

    private void createInteractionAd() {
        String postId = mAdParams.getPostId();
        if (checkPostIdEmpty(mAdProvider, postId)) {
            return;
        }


        TTAdNative adNative = createAdNative(mActivity.getApplicationContext());
        //设置广告参数
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mAdParams.getPostId()) //广告位id
                .setSupportDeepLink(mAdParams.isSupportDeepLink())
                .setAdCount(getLoadCount(mAdParams.getAdCount())) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(mAdParams.getExpressViewWidth(), mAdParams.getExpressViewHeight()) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(mAdParams.getImageWidth(), mAdParams.getImageHeight())//这个参数设置即可，不影响个性化模板广告的size
                .build();

        //加载广告
        adNative.loadInteractionExpressAd(adSlot, this);

    }

    @Override
    public void onError(int code, String message) {
        localExecFail(mAdProvider, code, message);
    }

    @Override
    public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
        if (list == null || list.size() == 0) {
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_LOAD_EMPTY_ERROR, "type TTNativeExpressAd  == null || type TTNativeExpressAd list.size() == 0");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad();
        }

        if (isCancel()) {
            LogUtils.e(TAG, "Csj InteractionExpressAd isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "Csj InteractionExpressAd isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }


        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        mTTNativeExpressAds = list;

        for (TTNativeExpressAd ttNativeExpressAd : list) {
            ttNativeExpressAd.setExpressInteractionListener(this);
            if (ttNativeExpressAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                //本地接口扔到Base里面去回调
                setAppDownloadListener(mListener);

                ttNativeExpressAd.setDownloadListener(this);
            }

//            if (mAdParams.isAutoShowAd()) {
//                ttNativeExpressAd.render();//调用render开始渲染广告
//            }

        }

//        IExpressAdView expressAdView = null;
//        if (!mAdParams.isAutoShowAd()) {
//            expressAdView = new CsjInteractionAdView(list);
//        }

        if (mAdProvider != null) {
            mAdProvider.callbackInteractionLoad(mListener, this, mAdParams.isAutoShowAd());
        }

    }

    @Override
    public void onAdDismiss() {
        if (mAdProvider != null) {
            mAdProvider.trackEventClose();
            mAdProvider.callbackInteractionClose(mListener);
        }
    }

    @Override
    public void onAdClicked(View view, int i) {
        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick();
            mAdProvider.callbackInteractionClick(mListener);
        }
    }

    @Override
    public void onAdShow(View view, int i) {
        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow();
            mAdProvider.callbackInteractionExposure(mListener);
        }
    }

    @Override
    public void onRenderFail(View view, String s, int i) {
//        localExecFail(mAdProvider, i, s);
        localRenderFail(mAdProvider, i, s);
    }

    @Override
    public void onRenderSuccess(View view, float v, float v1) {
        if (mTTNativeExpressAds != null) {
            for (TTNativeExpressAd ttNativeExpressAd : mTTNativeExpressAds) {
                ttNativeExpressAd.showInteractionExpressAd(mActivity);
            }
        }

        if (mAdProvider != null) {
            mAdProvider.trackRenderSuccess();
        }
    }

    @Override
    public void run() {
        if (mAdProvider != null) {
            mAdProvider.trackEventStart();
        }
        createInteractionAd();
    }

    @Override
    public int getStyleType() {
        return MobiConstantValue.STYLE.INTERACTION_EXPRESS;
    }

    @Override
    public void show() {
        if (mTTNativeExpressAds == null) {
            return;
        }

        for (TTNativeExpressAd ttFullScreenVideoAd : mTTNativeExpressAds) {
            ttFullScreenVideoAd.render();
        }

        if (mAdProvider != null) {
            mAdProvider.trackStartShow();
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        if (mTTNativeExpressAds != null) {
            mTTNativeExpressAds.clear();
        }
    }
}
