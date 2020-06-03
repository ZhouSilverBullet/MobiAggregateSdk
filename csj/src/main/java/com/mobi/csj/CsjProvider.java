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

        TTAdNative adNative = createAdNative(activity.getApplicationContext());
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
//                AdStatistical.trackAD(activity, mProviderType, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
                //加载错误
                if (listener != null) {
                    listener.onAdFail(mProviderType, "code：" + code + " errorMsg: " + errorMsg);
                    // listener.onAdClose(mProviderType);
                }
            }

            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd mttRewardVideoAd) {

                if (listener != null) {
                    listener.onAdLoad(mProviderType);
                    // listener.onAdClose(mProviderType);
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
//                        AdStatistical.trackAD(activity, mProviderType, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
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
//                    recordRenderSuccess(mProviderType);
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

    public void interactionExpress(Activity activity,
                                   String codeId,
                                   boolean supportDeepLink,
                                   ViewGroup viewContainer,
                                   float expressViewWidth,
                                   float expressViewHeight,
                                   IInteractionAdListener listener) {

        TTAdNative adNative = createAdNative(activity.getApplicationContext());
        //设置广告参数
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(supportDeepLink)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响个性化模板广告的size
                .build();
        //加载广告
        adNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
//                TToast.show(NativeExpressActivity.this, "load error : " + code + ", " + message);
                viewContainer.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                TTNativeExpressAd mTTAd = ads.get(0);
                bindAdListener(activity, mTTAd, viewContainer);
                mTTAd.render();//调用render开始渲染广告
            }
        });


//        //在合适的时机，释放广告的资源
//        @Override
//        protected void onDestroy () {
//            super.onDestroy();
//            if (mTTAd != null) {
//                //调用destroy()方法释放
//                mTTAd.destroy();
//            }
//        }
    }

    //绑定广告行为
    private void bindAdListener(final Activity activity, TTNativeExpressAd ad, final ViewGroup viewContainer) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {

            @Override
            public void onAdDismiss() {
//                TToast.show(mContext, "广告关闭");
            }

            @Override
            public void onAdClicked(View view, int type) {
//                TToast.show(mContext, "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
//                TToast.show(mContext, "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
//                Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
//                TToast.show(mContext, msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
//                TToast.show(mContext, "渲染成功");
                //在渲染成功回调时展示广告，提升体验
//                viewContainer.removeAllViews();
//                viewContainer.addView(view);
                ad.showInteractionExpressAd(activity);
            }
        });

        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }

        //可选，下载监听设置
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
//                TToast.show(InteractionExpressActivity.this, "点击开始下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
//                if (!mHasShowDownloadActive) {
//                    mHasShowDownloadActive = true;
//                    TToast.show(InteractionExpressActivity.this, "下载中，点击暂停", Toast.LENGTH_LONG);
//                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(InteractionExpressActivity.this, "下载暂停，点击继续", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
//                TToast.show(InteractionExpressActivity.this, "下载失败，点击重新下载", Toast.LENGTH_LONG);
            }

            @Override
            public void onInstalled(String fileName, String appName) {
//                TToast.show(InteractionExpressActivity.this, "安装完成，点击图片打开", Toast.LENGTH_LONG);
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
//                TToast.show(InteractionExpressActivity.this, "点击安装", Toast.LENGTH_LONG);
            }
        });

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
                        IExpressListener mListener) {
        int DEFAULT_COUNT = 1;

        TTAdNative mTTAdNative = createAdNative(activity.getApplicationContext());

//        if (mHeightAuto) {
//            ADViewHeight = 0;
//        }

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(supportDeepLink)
                .setAdCount(DEFAULT_COUNT)
                .setExpressViewAcceptedSize(aDViewWidth, aDViewHeight)
                .setImageAcceptedSize(640, 320)
                .build();
        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            //加载失败
            @Override
            public void onError(int i, String s) {
//                AdStatistical.trackAD(mContext, mProviderType, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
//                mBearingView.removeAllViews();
                if (mListener != null) {
                    mListener.onLoadFailed(mProviderType, i, s);
                }
            }

            //加载成功
            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
                if (list == null || list.size() <= 0) {
                    return;
                }
                TTNativeExpressAd mTTAd = list.get(0);
                bindTTAdListener(mTTAd, viewContainer, mListener);
                mTTAd.render();
//                recordRenderSuccess(mProviderType);
//                renderTTAD();
            }
        });
    }

    private void bindTTAdListener(TTNativeExpressAd ad, ViewGroup viewContainer, IExpressListener mListener) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
            @Override
            public void onAdDismiss() {
                if (mListener != null) {
                    mListener.onAdDismissed(mProviderType);
                }
            }

            @Override
            public void onAdClicked(View view, int i) {
                //广告被点击
                if (mListener != null) {
                    mListener.onAdClick(mProviderType);
                }
//                AdStatistical.trackAD(mContext, mProviderType, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
            }

            @Override
            public void onAdShow(View view, int i) {
                //广告显示
                if (mListener != null) {
                    mListener.onAdShow(mProviderType);
                }
            }

            @Override
            public void onRenderFail(View view, String s, int i) {
                //广告渲染失败
                if (mListener != null) {
                    mListener.onLoadFailed(mProviderType, i, s);
                }
            }

            @Override
            public void onRenderSuccess(View view, float v, float v1) {
                //广告渲染成功
//                if (firstCome) {
//                    renderTTAD();
//                    firstCome = false;
//                }
                //render上去
                viewContainer.addView(ad.getExpressAdView());
                ad.render();

                if (mListener != null) {
                    mListener.onAdRenderSuccess(mProviderType);
                }

            }
        });

        //TODO downLoad 后面可以提供出去
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }


    }
}
