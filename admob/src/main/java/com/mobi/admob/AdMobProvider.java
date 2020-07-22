package com.mobi.admob;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.mobi.admob.wrapper.FullScreenVideoAdWrapper;
import com.mobi.admob.wrapper.InterstitialADWrapper;
import com.mobi.admob.wrapper.NativeExpressAdWrapper;
import com.mobi.admob.wrapper.RewardVideoAdWrapper;
import com.mobi.admob.wrapper.SplashAdWrapper;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.splash.BaseSplashSkipView;
import com.mobi.core.strategy.AdRunnable;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 18:12
 * @Dec 略
 */
public class AdMobProvider extends BaseAdProvider {
    public static final String TAG = "AdMobProvider";

    public AdMobProvider(String providerType) {
        super(providerType);
    }

    @Override
    public AdRunnable splash(Activity activity,
                             ViewGroup splashContainer,
                             BaseSplashSkipView skipView,
                             LocalAdParams adParams,
                             ISplashAdListener listener) {

        SplashAdWrapper splashAdWrapper = new SplashAdWrapper(this,
                activity,
                splashContainer,
                adParams,
                listener);

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

        InterstitialADWrapper interactionExpressAdWrapper = new InterstitialADWrapper(this,
                activity, adParams, listener);

        return interactionExpressAdWrapper;
    }

    @Override
    public AdRunnable nativeExpress(Context context,
                                    ViewGroup viewContainer,
                                    LocalAdParams adParams,
                                    IExpressListener mListener) {

        NativeExpressAdWrapper nativeExpressAdWrap = new NativeExpressAdWrapper(
                this,
                context,
                viewContainer,
                adParams,
                mListener);

        return nativeExpressAdWrap;
    }

}
