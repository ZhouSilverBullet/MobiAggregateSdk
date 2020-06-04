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
import com.mobi.csj.wrapper.SplashAdWrapper;

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

    public void splash(Activity activity,
                       String codeId,
                       int expressViewWidth,
                       int expressViewHeight,
                       boolean supportDeepLink,
                       ViewGroup splashContainer,
                       ISplashAdListener listener) {

        if (expressViewWidth <= 0) {
            expressViewWidth = ScreenUtils.getAppWidth();
        }

        if (expressViewHeight <= 0) {
            expressViewHeight = ScreenUtils.getAppHeight();
        }

        SplashAdWrapper splashAdWrapper = new SplashAdWrapper(this,
                activity,
                codeId,
                expressViewWidth,
                expressViewHeight,
                supportDeepLink,
                splashContainer,
                listener);

        splashAdWrapper.createSplashAd();

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
