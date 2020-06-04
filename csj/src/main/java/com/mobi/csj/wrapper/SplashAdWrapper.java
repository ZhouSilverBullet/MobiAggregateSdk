package com.mobi.csj.wrapper;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.listener.ISplashAdListener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 14:07
 * @Dec 略
 */
public class SplashAdWrapper extends BaseAdWrapper implements TTAdNative.SplashAdListener, TTSplashAd.AdInteractionListener, TTAppDownloadListener {
    public static final String TAG = "SplashAdWrapper";

    private String mProviderType;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    String mCodeId;
    int mExpressViewWidth;
    int mExpressViewHeight;
    boolean mSupportDeepLink;
    ViewGroup mSplashContainer;
    ISplashAdListener mListener;

    public SplashAdWrapper(BaseAdProvider adProvider,
                           Activity activity,
                           String codeId,
                           int expressViewWidth,
                           int expressViewHeight,
                           boolean supportDeepLink,
                           ViewGroup splashContainer,
                           ISplashAdListener listener) {
        mAdProvider = adProvider;
        mActivity = activity;
        mCodeId = codeId;
        mExpressViewWidth = expressViewWidth;
        mExpressViewHeight = expressViewHeight;
        mSupportDeepLink = supportDeepLink;
        mSplashContainer = splashContainer;
        mListener = listener;

        if (mAdProvider != null) {
            mProviderType = mAdProvider.getProviderType();
        }
    }

    public void createSplashAd() {
        TTAdNative adNative = createAdNative(mActivity.getApplicationContext());

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mCodeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setExpressViewAcceptedSize(mExpressViewWidth, mExpressViewHeight)
                .build();

        if (mAdProvider != null) {
            mAdProvider.callbackSplashStartRequest(mListener);
        }

        adNative.loadSplashAd(adSlot, this);
    }

    @Override
    public void onError(int code, String errorMsg) {

        if (mAdProvider != null) {
            mAdProvider.callbackSplashFail(code, errorMsg, mListener);
        }

    }

    @Override
    public void onTimeout() {
        if (mAdProvider != null) {
            mAdProvider.callbackSplashFail(-100, "请求超时", mListener);
        }
    }

    @Override
    public void onSplashAdLoad(TTSplashAd ttSplashAd) {
        if (ttSplashAd == null) {
            if (mAdProvider != null) {
                mAdProvider.callbackSplashFail(-100, "请求成功，但是返回的广告为null", mListener);
            }
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.callbackSplashLoaded(mListener);
        }


        //获取SplashView
        View view = ttSplashAd.getSplashView();
        if (view != null && mSplashContainer != null && !mActivity.isFinishing()) {
            mSplashContainer.removeAllViews();
            //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
            mSplashContainer.addView(view);
            //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
            // todo 自定义的splash
            //  ad.setNotAllowSdkCountdown();
        } else {
//          goToMainActivity();
            //activity已经销毁了，或者传进来的container为null
            Log.e(TAG, "goToMainActivity");
            if (mAdProvider != null) {
                mAdProvider.callbackSplashDismissed(mListener);
            }

        }

        ttSplashAd.setSplashInteractionListener(this);

        //下载相关的
        if (ttSplashAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            //本地接口扔到Base里面去回调
            setAppDownloadListener(mListener);

            ttSplashAd.setDownloadListener(this);
        }

    }


    @Override
    public void onAdClicked(View view, int type) {
        Log.e(TAG, "onAdClicked");

        if (mAdProvider != null) {
            mAdProvider.callbackSplashClicked(mListener);
        }
    }

    @Override
    public void onAdShow(View view, int type) {
        Log.e(TAG, "onAdShow");
//                        showToast("开屏广告展示");

        if (mAdProvider != null) {
            mAdProvider.callbackSplashExposure(mListener);
        }
    }

    @Override
    public void onAdSkip() {
        Log.e(TAG, "onAdSkip");
//                        showToast("开屏广告跳过");
//                        goToMainActivity();

        if (mAdProvider != null) {
            mAdProvider.callbackSplashDismissed(mListener);
        }

    }

    @Override
    public void onAdTimeOver() {
        Log.e(TAG, "onAdTimeOver");
//                        showToast("开屏广告倒计时结束");
//                        goToMainActivity();
        if (mAdProvider != null) {
            mAdProvider.callbackSplashDismissed(mListener);
        }

    }
}
