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

    public void splash(final Activity activity,
                       final String codeId,
                       int expressViewWidth,
                       int expressViewHeight,
                       boolean supportDeepLink,
                       BaseSplashSkipView skipView,
                       ViewGroup splashContainer,
                       ISplashAdListener listener) {

        SplashAdWrapper splashAdWrapper = new SplashAdWrapper(this,
                activity,
                codeId,
                splashContainer,
                listener);

        splashAdWrapper.setSplashSkipView(skipView);

        splashAdWrapper.createSplashAd();
    }

    @Override
    public void fullscreen(Activity activity, String codeId, int orientation, boolean supportDeepLink, IFullScreenVideoAdListener listener) {

    }


    @Override
    public void rewardVideo(Activity activity, String codeId, boolean supportDeepLink, IRewardAdListener listener) {

        RewardVideoAdWrapper rewardVideoAdWrapper = new RewardVideoAdWrapper(this,
                activity, codeId, supportDeepLink, listener);
        rewardVideoAdWrapper.createRewardVideoAd();

    }

    @Override
    public void interactionExpress(Activity activity,
                                   String codeId,
                                   boolean supportDeepLink,
                                   ViewGroup viewContainer,
                                   float expressViewWidth,
                                   float expressViewHeight,
                                   IInteractionAdListener listener) {


        UnifiedInterstitialADWrapper unifiedInterstitialADWrapper =
                new UnifiedInterstitialADWrapper(this, activity, codeId, listener);

        unifiedInterstitialADWrapper.createInterstitialAD();

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
