package com.mobi.csj;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.IAdProvider;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.utils.LogUtils;
import com.mobi.core.utils.ScreenUtils;
import com.mobi.csj.wrapper.InteractionExpressAdWrapper;
import com.mobi.csj.wrapper.NativeExpressAdWrapper;
import com.mobi.csj.wrapper.RewardVideoAdWrapper;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 18:12
 * @Dec 略
 */
public class CsjProvider extends BaseAdProvider {
    public static final String TAG = "CsjProvider";

    public CsjProvider(String providerType) {
        super(providerType);
    }

    public void splash(final Activity activity,
                       final String codeId,
                       final ViewGroup splashContainer,
                       final ISplashAdListener listener) {

        TTAdNative adNative = createAdNative(activity.getApplicationContext());

        int appHeight = ScreenUtils.getAppHeight();
        int appWidth = ScreenUtils.getAppWidth();

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(appWidth, appHeight)
                .build();

        callbackSplashStartRequest(listener);

        adNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            public void onError(int code, String errorMsg) {
                LogUtils.e(TAG, "csj splash:" + errorMsg);

                callbackSplashFail("错误码：" + codeId + "，errorMsg：" + errorMsg, listener);
            }

            @Override
            public void onTimeout() {
                LogUtils.e(TAG, "csj splash: onTimeout");

                callbackSplashFail("请求超时", listener);
            }

            @Override
            public void onSplashAdLoad(TTSplashAd ad) {
                LogUtils.d(TAG, "csj开屏广告请求成功");
                if (ad == null) {

                    callbackSplashFail("请求成功，但是返回的广告为null", listener);
                    return;
                }

                callbackSplashLoaded(listener);

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

                    callbackSplashDismissed(listener);
                }

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.d(TAG, "onAdClicked");

                        callbackSplashClicked(listener);
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Log.d(TAG, "onAdShow");
//                        showToast("开屏广告展示");

                        callbackSplashExposure(listener);
                    }

                    @Override
                    public void onAdSkip() {
                        Log.d(TAG, "onAdSkip");
//                        showToast("开屏广告跳过");
//                        goToMainActivity();

                        callbackSplashDismissed(listener);

                    }

                    @Override
                    public void onAdTimeOver() {
                        Log.d(TAG, "onAdTimeOver");
//                        showToast("开屏广告倒计时结束");
//                        goToMainActivity();
                        callbackSplashDismissed(listener);

                    }
                });
            }
        });
    }

    public void fullscreen(final Activity activity,
                           final String codeId,
                           int orientation, boolean supportDeepLink, final IFullScreenVideoAdListener listener) {
        TTAdNative adNative = createAdNative(activity.getApplicationContext());

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                //模板广告需要设置期望个性化模板广告的大小,单位dp,全屏视频场景，只要设置的值大于0即可
                .setExpressViewAcceptedSize(500, 500)
                .setSupportDeepLink(supportDeepLink)
                .setOrientation(orientation)//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();

        adNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                if (listener != null) {
                    listener.onAdFail(mProviderType, "code: " + i + ", errorMsg: " + s);
                }
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ttFullScreenVideoAd) {
                if (listener != null) {
                    listener.onAdLoad(mProviderType);
                }

                ttFullScreenVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
                    @Override
                    public void onAdShow() {
                        if (listener != null) {
                            listener.onAdShow(mProviderType);
                        }
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        if (listener != null) {
                            listener.onAdVideoBarClick(mProviderType);
                        }
                    }

                    @Override
                    public void onAdClose() {
                        if (listener != null) {
                            listener.onAdClose(mProviderType);
                        }
                    }

                    @Override
                    public void onVideoComplete() {
                        if (listener != null) {
                            listener.onVideoComplete(mProviderType);
                        }
                    }

                    @Override
                    public void onSkippedVideo() {
                        if (listener != null) {
                            listener.onSkippedVideo(mProviderType);
                        }
                    }
                });

                ttFullScreenVideoAd.showFullScreenVideoAd(activity);
            }

            @Override
            public void onFullScreenVideoCached() {
                if (listener != null) {
                    listener.onCached(mProviderType);
                }
            }
        });
    }

    public void rewardVideo(final Activity activity,
                            final String codeId,
                            boolean supportDeepLink,
                            final IRewardAdListener listener) {

        RewardVideoAdWrapper rewardVideoAdWrapper = new RewardVideoAdWrapper(this,
                activity, codeId, supportDeepLink, listener);

        rewardVideoAdWrapper.createRewardVideoAd();

    }

    public void interactionExpress(Activity activity,
                                   String codeId,
                                   boolean supportDeepLink,
                                   ViewGroup viewContainer,
                                   float expressViewWidth,
                                   float expressViewHeight,
                                   IInteractionAdListener listener) {

        InteractionExpressAdWrapper interactionExpressAdWrapper = new InteractionExpressAdWrapper(this,
                activity, codeId, supportDeepLink, viewContainer, 1, expressViewWidth, expressViewHeight, listener);

        interactionExpressAdWrapper.createInteractionAd();

    }


    private TTAdNative createAdNative(Context context) {
        return CsjSession.get().getAdManager().createAdNative(context);
    }

    @Override
    public void express(Activity activity,
                        String codeId,
                        boolean supportDeepLink,
                        ViewGroup viewContainer,
                        int aDViewWidth,
                        int aDViewHeight,
                        int loadCount,
                        IExpressListener mListener) {

        NativeExpressAdWrapper nativeExpressAdWrap = new NativeExpressAdWrapper(
                activity,
                this,
                codeId,
                supportDeepLink,
                viewContainer,
                aDViewWidth,
                aDViewHeight,
                loadCount,
                mListener);

        nativeExpressAdWrap.createNativeExpressAD();


    }

//    private void bindTTAdListener(TTNativeExpressAd ad, ViewGroup viewContainer, IExpressListener mListener) {
//        ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
//            @Override
//            public void onAdDismiss() {
//                if (mListener != null) {
//                    mListener.onAdDismissed(mProviderType);
//                }
//            }
//
//            @Override
//            public void onAdClicked(View view, int i) {
//                //广告被点击
//                if (mListener != null) {
//                    mListener.onAdClick(mProviderType);
//                }
////                AdStatistical.trackAD(mContext, mProviderType, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
//            }
//
//            @Override
//            public void onAdShow(View view, int i) {
//                //广告显示
//                if (mListener != null) {
//                    mListener.onAdShow(mProviderType);
//                }
//            }
//
//            @Override
//            public void onRenderFail(View view, String s, int i) {
//                //广告渲染失败
//                if (mListener != null) {
//                    mListener.onLoadFailed(mProviderType, i, s);
//                }
//            }
//
//            @Override
//            public void onRenderSuccess(View view, float v, float v1) {
//                //广告渲染成功
////                if (firstCome) {
////                    renderTTAD();
////                    firstCome = false;
////                }
//                //render上去
//                viewContainer.addView(ad.getExpressAdView());
//                ad.render();
//
//                if (mListener != null) {
//                    mListener.onAdRenderSuccess(mProviderType);
//                }
//
//            }
//        });
//
//        //TODO downLoad 后面可以提供出去
//        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
//            return;
//        }
//
//
//    }
}
