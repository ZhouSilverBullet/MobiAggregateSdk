package com.mobi.csj;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.IAdProvider;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.utils.LogUtils;
import com.mobi.core.utils.ScreenUtils;

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

        TTAdNative adNative = CsjSession.get().getAdManager().createAdNative(activity);

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
        TTAdNative adNative = CsjSession.get().getAdManager().createAdNative(activity);
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

        TTAdNative adNative = CsjSession.get().getAdManager().createAdNative(activity);
        AdSlot adSlot = new AdSlot.Builder()
                .setImageAcceptedSize(1080, 1920)
                .setCodeId(codeId)
                .setSupportDeepLink(supportDeepLink)
                .setRewardName("")
                .setRewardAmount(10)
                .setUserID("")
                .setMediaExtra("media-extra")
                .setOrientation(TTAdConstant.VERTICAL)
                .build();
        adNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String errorMsg) {
//                AdStatistical.trackAD(activity, Constants.TT_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
                //加载错误
                if (listener != null) {
                    listener.onAdFail(mProviderType, "code：" + code + " errorMsg: " + errorMsg);
                    // listener.onAdClose(Constants.TT_KEY);
                }
            }

            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd mttRewardVideoAd) {

                if (listener != null) {
                    listener.onAdLoad(mProviderType);
                    // listener.onAdClose(Constants.TT_KEY);
                }

                //加载成功
//                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {
                    @Override
                    public void onAdShow() {
                        //显示广告
                        if (listener != null) {
                            listener.onAdShow(mProviderType);
                        }
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        if (listener != null) {
                            listener.onAdClick(mProviderType);
                        }
//                        AdStatistical.trackAD(activity, Constants.TT_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
                    }

                    @Override
                    public void onAdClose() {
                        //广告关闭
                        if (listener != null) {
                            listener.onAdClose(mProviderType);
                        }
                    }

                    @Override
                    public void onVideoComplete() {
                        //播放完成
                        if (listener != null) {
                            listener.onVideoComplete(mProviderType);
                        }
                    }

                    @Override
                    public void onVideoError() {
                        //播放错误
                        if (listener != null) {
                            listener.onAdFail(mProviderType, "播放错误");
                        }
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        //视频播放完成，奖励回调验证
                        if (listener != null) {
                            listener.onRewardVerify(mProviderType, rewardVerify, rewardAmount, rewardName);
                        }
                    }

                    @Override
                    public void onSkippedVideo() {
                        //跳过广告
                        if (listener != null) {
                            listener.onSkippedVideo(mProviderType);
                        }
                    }
                });
                mttRewardVideoAd.showRewardVideoAd(activity, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
//                if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {
//                    showTTVideo();
//                } else {
//                    recordRenderSuccess(Constants.TT_KEY);
//                    if (firstCome) {
//                        showTTVideo();
//                        firstCome = false;
//                    }
//                }
            }

            @Override
            public void onRewardVideoCached() {
                //缓存在了本地
                if (listener != null) {
                    listener.onCached(mProviderType);
                }
            }
        });
    }
}
