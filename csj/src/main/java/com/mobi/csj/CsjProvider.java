package com.mobi.csj;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.TTAdNative;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.splash.BaseSplashSkipView;
import com.mobi.core.strategy.AdRunnable;
import com.mobi.csj.wrapper.FullScreenVideoAdWrapper;
import com.mobi.csj.wrapper.InteractionExpressAdWrapper;
import com.mobi.csj.wrapper.NativeExpressAdWrapper;
import com.mobi.csj.wrapper.RewardVideoAdWrapper;
import com.mobi.csj.wrapper.SplashAdWrapper;

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

    @Override
    public AdRunnable splash(Activity activity,
                             ViewGroup splashContainer,
                             BaseSplashSkipView skipView,
                             LocalAdParams adParams,
                             ISplashAdListener listener) {

//        if (expressViewWidth <= 0) {
//            expressViewWidth = ScreenUtils.getAppWidth();
//        }
//
//        if (expressViewHeight <= 0) {
//            expressViewHeight = ScreenUtils.getAppHeight();
//        }

        SplashAdWrapper splashAdWrapper = new SplashAdWrapper(this,
                activity,
                splashContainer,
                adParams,
                listener);

        splashAdWrapper.setSplashSkipView(skipView);

//        splashAdWrapper.createSplashAd();
        return splashAdWrapper;
    }

    public AdRunnable fullscreen(final Activity activity,
                           LocalAdParams adParams,
                           final IFullScreenVideoAdListener listener) {


        FullScreenVideoAdWrapper fullScreenVideoAdWrapper = new FullScreenVideoAdWrapper(this,
                activity, adParams, listener);
        return fullScreenVideoAdWrapper;

    }

    public AdRunnable rewardVideo(final Activity activity,
                            LocalAdParams adParams,
                            final IRewardAdListener listener) {

        RewardVideoAdWrapper rewardVideoAdWrapper = new RewardVideoAdWrapper(this,
                activity, adParams, listener);

//        rewardVideoAdWrapper.createRewardVideoAd();
        return rewardVideoAdWrapper;
    }

    public AdRunnable interactionExpress(Activity activity,
                                         LocalAdParams adParams,
                                         IInteractionAdListener listener) {

        InteractionExpressAdWrapper interactionExpressAdWrapper = new InteractionExpressAdWrapper(this,
                activity, adParams, listener);

//        interactionExpressAdWrapper.createInteractionAd();
        return interactionExpressAdWrapper;
    }


    private TTAdNative createAdNative(Context context) {
        return CsjSession.get().getAdManager().createAdNative(context);
    }

    @Override
    public AdRunnable express(Activity activity,
                              ViewGroup viewContainer,
                              LocalAdParams adParams,
                              IExpressListener mListener) {

        NativeExpressAdWrapper nativeExpressAdWrap = new NativeExpressAdWrapper(
                this,
                activity,
                viewContainer,
                adParams,
                mListener);

//        nativeExpressAdWrap.createNativeExpressAD();

        return nativeExpressAdWrap;
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
