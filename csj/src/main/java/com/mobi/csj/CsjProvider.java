package com.mobi.csj;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.mobi.core.IAdProvider;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.utils.LogUtils;
import com.mobi.core.utils.ScreenUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 18:12
 * @Dec 略
 */
public class CsjProvider implements IAdProvider {
    public static final String TAG = "CsjProvider";
    public String mProviderType;

    public CsjProvider(String providerType) {
        mProviderType = providerType;
    }

    public void splash(final Activity activity,
                       final String codeId,
                       final ViewGroup splashContainer,
                       final ISplashAdListener listener) {

        TTAdNative adNative = CsjSession.get().getAdManager().createAdNative(activity);

        int appHeight = ScreenUtils.getAppHeight();
        int appWidth = ScreenUtils.getAppWidth();

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(appWidth, appHeight)
                .build();

        adNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.e(TAG, "csj splash:" + errorMsg);
                if (listener != null) {
                    listener.onAdFail(mProviderType, "错误码：" + codeId + "，errorMsg：" + errorMsg);
                }
            }

            @Override
            public void onTimeout() {
                LogUtils.e(TAG, "csj splash: onTimeout");
                if (listener != null) {
                    listener.onAdFail(mProviderType, "请求超时");
                }
            }

            @Override
            public void onSplashAdLoad(TTSplashAd ad) {
                LogUtils.d(TAG, "csj开屏广告请求成功");
                if (ad == null) {
                    if (listener != null) {
                        listener.onAdFail(mProviderType, "请求成功，但是返回的广告为null");
                    }
                    return;
                }

                if (listener != null) {
                    listener.onAdLoaded(mProviderType);
                }

                //获取SplashView
                View view = ad.getSplashView();
                if (view != null && splashContainer != null && !activity.isFinishing()) {
                    splashContainer.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                    splashContainer.addView(view);
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    // todo 自定义的splash
//                    ad.setNotAllowSdkCountdown();
                } else {
//                    goToMainActivity();
                    //activity已经销毁了，或者传进来的container为null
                    if (listener != null) {
                        listener.onAdDismissed(mProviderType);
                    }
                }

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.d(TAG, "onAdClicked");
//                        showToast("开屏广告点击");
                        if (listener != null) {
                            listener.onAdClicked(mProviderType);
                        }
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Log.d(TAG, "onAdShow");
//                        showToast("开屏广告展示");
                        if (listener != null) {
                            listener.onAdExposure(mProviderType);
                        }
                    }

                    @Override
                    public void onAdSkip() {
                        Log.d(TAG, "onAdSkip");
//                        showToast("开屏广告跳过");
//                        goToMainActivity();

                        if (listener != null) {
                            listener.onAdDismissed(mProviderType);
                        }

                    }

                    @Override
                    public void onAdTimeOver() {
                        Log.d(TAG, "onAdTimeOver");
//                        showToast("开屏广告倒计时结束");
//                        goToMainActivity();
                        if (listener != null) {
                            listener.onAdDismissed(mProviderType);
                        }
                    }
                });
            }
        });
    }
}
