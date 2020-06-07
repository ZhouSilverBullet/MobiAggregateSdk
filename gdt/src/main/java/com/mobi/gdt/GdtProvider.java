package com.mobi.gdt;

import android.app.Activity;
import android.view.ViewGroup;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.splash.BaseSplashSkipView;
import com.mobi.core.strategy.AdRunnable;
import com.mobi.gdt.wrapper.GdtFullScreenVideoAdWrapper;
import com.mobi.gdt.wrapper.NativeExpressAdWrapper;
import com.mobi.gdt.wrapper.RewardVideoAdWrapper;
import com.mobi.gdt.wrapper.SplashAdWrapper;
import com.mobi.gdt.wrapper.UnifiedInterstitialADWrapper;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 18:12
 * @Dec 略
 */
public class GdtProvider extends BaseAdProvider {
    public static final String TAG = "GdtProvider";

    public GdtProvider(String providerType) {
        super(providerType);
    }

    public AdRunnable splash(final Activity activity,
                             ViewGroup splashContainer,
                             BaseSplashSkipView skipView,
                             LocalAdParams adParams,
                             ISplashAdListener listener) {

        SplashAdWrapper splashAdWrapper = new SplashAdWrapper(this,
                activity,
                splashContainer,
                adParams,
                listener);

        splashAdWrapper.setSplashSkipView(skipView);

        return splashAdWrapper;
    }

    @Override
    public AdRunnable fullscreen(Activity activity, LocalAdParams adParams, IFullScreenVideoAdListener listener) {
        return new GdtFullScreenVideoAdWrapper(this, activity, adParams, listener);
    }


    @Override
    public AdRunnable rewardVideo(Activity activity, LocalAdParams adParams, IRewardAdListener listener) {

        RewardVideoAdWrapper rewardVideoAdWrapper = new RewardVideoAdWrapper(this,
                activity,
                adParams,
                listener);
//        rewardVideoAdWrapper.createRewardVideoAd();
        return rewardVideoAdWrapper;
    }

    @Override
    public AdRunnable interactionExpress(Activity activity,
                                         LocalAdParams adParams,
                                         IInteractionAdListener listener) {


        UnifiedInterstitialADWrapper unifiedInterstitialADWrapper =
                new UnifiedInterstitialADWrapper(this, activity, adParams, listener);

//        unifiedInterstitialADWrapper.createInterstitialAD();
        return unifiedInterstitialADWrapper;
    }

    @Override
    public AdRunnable express(Activity mContext,
                              ViewGroup viewContainer,
                              LocalAdParams adParams,
                              IExpressListener mListener) {

        NativeExpressAdWrapper nativeExpressAdWrap = new NativeExpressAdWrapper(this,
                mContext,
                viewContainer,
                adParams,
                mListener);

//        nativeExpressAdWrap.createNativeExpressAD();
        return nativeExpressAdWrap;
    }


    private String getAppId() {
        return GdtSession.get().getAppId();
    }
}
