package com.mobi.csj.wrapper;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.IExpressAdView;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.splash.BaseSplashSkipView;
import com.mobi.core.utils.LogUtils;
import com.mobi.csj.impl.CsjSplashAdView;
import com.mobi.csj.splash.CsjSplashSkipViewControl;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 14:07
 * @Dec 略
 */
public class SplashAdWrapper extends BaseAdWrapper implements TTAdNative.SplashAdListener, TTSplashAd.AdInteractionListener, TTAppDownloadListener {
    public static final String TAG = "SplashAdWrapper";
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;

    private String mProviderType;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    ViewGroup mSplashContainer;
    ISplashAdListener mListener;
    private BaseSplashSkipView mBaseSplashSkipView;

    public SplashAdWrapper(BaseAdProvider adProvider,
                           Activity activity,
                           ViewGroup splashContainer,
                           LocalAdParams adParams,
                           ISplashAdListener listener) {
        mAdProvider = adProvider;
        mActivity = activity;
        mSplashContainer = splashContainer;
        mAdParams = adParams;
        mListener = listener;

        if (mAdProvider != null) {
            mProviderType = mAdProvider.getProviderType();
        }
        mMobiCodeId = mAdParams.getMobiCodeId();
    }

    public void setSplashSkipView(BaseSplashSkipView splashSkipView) {
        mBaseSplashSkipView = splashSkipView;
    }

    private void createSplashAd() {
        String postId = mAdParams.getPostId();
        if (checkPostIdEmpty(mAdProvider, postId)) {
            return;
        }

        TTAdNative adNative = createAdNative(mActivity.getApplicationContext());

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mAdParams.getPostId())
                .setSupportDeepLink(mAdParams.isSupportDeepLink())
                .setImageAcceptedSize(mAdParams.getImageWidth(), mAdParams.getImageHeight())
                /**
                 * code: 40029, message: 两种情况：
                 * 1. SDK版本低；
                 * 使用的sdk版本过低，还不支持个性化模板渲染功能。解决办法：升级到平台最新版本sdk。
                 * 2. 接口使用错误；创建的代码位类型是模板渲染/非模板渲染，
                 * 但是请求方法是非模板渲染/模板渲染的方法。解决办法：使用模板
                 * 渲染的方法去请求模板渲染类型或者使用非模板渲染的方法去请求非模板类型的广告，
                 * 如果代码位在平台上是模板渲染，
                 * 可以参考文档中个性化模板XX广告的部分，demo中参考带有express部分的代码。
                 * 如果代码位不是模板渲染，则不要调用含有express字样的接口。
                 * 参考文档：https://partner.oceanengine.com/doc?id=5dd0fe716b181e00112e3eb8
                 */
//                .setExpressViewAcceptedSize(mExpressViewWidth, mExpressViewHeight)
                .build();

        if (mAdProvider != null) {
            mAdProvider.callbackSplashStartRequest(mListener);
        }

        adNative.loadSplashAd(adSlot, this, /*超时时间*/2000);
    }

    @Override
    public void onError(int code, String errorMsg) {

        localExecFail(mAdProvider, code, errorMsg);

    }

    @Override
    public void onTimeout() {
        localExecFail(mAdProvider, -100, "请求超时");
    }

    @Override
    public void onSplashAdLoad(TTSplashAd ttSplashAd) {
        if (ttSplashAd == null) {
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_LOAD_EMPTY_ERROR, "请求成功，没有返回的广告");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad(getStyleType());
        }

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "Csj SplashAdWrapper load isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "Csj SplashAdWrapper load isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }

        if (mSplashContainer.getChildCount() >= 1) {
            LogUtils.e(TAG, "Csj SplashAdWrapper mSplashContainer.getChildCount() >= 1 isCancel");
            localExecFail(mAdProvider, -105, "Csj splashContainer.getChildCount() >= 1 isCancel");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        if (mAdParams.isSplashNotAllowSdkCountdown()) {
            ttSplashAd.setNotAllowSdkCountdown();
        }
        ttSplashAd.setSplashInteractionListener(this);

        //下载相关的
        if (ttSplashAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            //本地接口扔到Base里面去回调
            setAppDownloadListener(mListener);

            ttSplashAd.setDownloadListener(this);
        }

        IExpressAdView expressAdView = null;
        if (mAdParams.isAutoShowAd()) {
            showAdView(ttSplashAd);
        } else {
            expressAdView = new CsjSplashAdView(this, ttSplashAd);
        }

        if (mAdProvider != null) {
            mAdProvider.callbackSplashLoaded(mListener, expressAdView, mAdParams.isAutoShowAd());
        }

    }

    public void showAdView(TTSplashAd ttSplashAd) {
        if (ttSplashAd == null) {
            LogUtils.e(TAG, "ttSplashAd == null");
            return;
        }
        //获取SplashView
        View view = ttSplashAd.getSplashView();
        if (view != null && mSplashContainer != null && !mActivity.isFinishing()) {

            mSplashContainer.removeAllViews();
            //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
            mSplashContainer.addView(view);
            //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
            // todo 自定义的splash
            if (mBaseSplashSkipView != null) {
                handleSplashSkipView(mSplashContainer);
            }
        } else {
//          goToMainActivity();
            //activity已经销毁了，或者传进来的container为null
            LogUtils.e(TAG, "goToMainActivity");
            if (mAdProvider != null) {
                mAdProvider.callbackSplashDismissed(mListener);
            }

        }
    }

    public void handleSplashSkipView(ViewGroup splashContainer) {

        CsjSplashSkipViewControl csjSplashSkipViewControl = new CsjSplashSkipViewControl(mBaseSplashSkipView);
        //里面做跳过的逻辑
        csjSplashSkipViewControl.setSkipCallback(this::onAdSkip);
        csjSplashSkipViewControl.handleSplashSkipView(splashContainer);

    }


    @Override
    public void onAdClicked(View view, int type) {
        LogUtils.e(TAG, "onAdClicked");

        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick(getStyleType());
            mAdProvider.callbackSplashClicked(mListener);
        }
    }

    @Override
    public void onAdShow(View view, int type) {
        LogUtils.e(TAG, "onAdShow");

        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow(getStyleType());
            mAdProvider.callbackSplashExposure(mListener);
        }
    }

    @Override
    public void onAdSkip() {
        LogUtils.e(TAG, "onAdSkip");

        if (mAdProvider != null) {
            mAdProvider.trackEventClose(getStyleType());
            mAdProvider.callbackSplashDismissed(mListener);
        }

    }

    @Override
    public void onAdTimeOver() {
        LogUtils.e(TAG, "onAdTimeOver");
        if (mAdProvider != null) {
            mAdProvider.callbackSplashDismissed(mListener);
        }

    }

    @Override
    public void run() {
        if (mAdProvider != null) {
            mAdProvider.trackEventStart(getStyleType());
        }
        createSplashAd();
    }


    @Override
    public int getStyleType() {
        return MobiConstantValue.STYLE.SPLASH;
    }
}
