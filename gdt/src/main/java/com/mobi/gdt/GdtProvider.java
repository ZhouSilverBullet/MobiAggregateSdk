package com.mobi.gdt;

import android.app.Activity;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.gdt.wrapper.NativeExpressAdWrapper;
import com.mobi.gdt.wrapper.RewardVideoAdWrapper;
import com.mobi.gdt.wrapper.UnifiedInterstitialADWrapper;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.List;

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
                       final ViewGroup splashContainer,
                       final ISplashAdListener listener) {


        callbackSplashStartRequest(listener);

        SplashAD splashAD = new SplashAD(activity, GdtSession.get().getAppId(), codeId, new SplashADListener() {
            @Override
            public void onADDismissed() {

                callbackSplashDismissed(listener);
            }

            @Override
            public void onNoAD(AdError adError) {
                if (listener != null) {
                    if (adError != null) {
                        callbackSplashFail("code: " + adError.getErrorCode() + ", errorMsg: " + adError.getErrorMsg(), listener);
                    } else {
                        callbackSplashFail("广告加载失败", listener);
                    }
                }
            }

            @Override
            public void onADPresent() {

            }

            @Override
            public void onADClicked() {

                callbackSplashClicked(listener);
            }

            @Override
            public void onADTick(long l) {

            }

            @Override
            public void onADExposure() {
                callbackSplashExposure(listener);
            }

            @Override
            public void onADLoaded(long l) {

                callbackSplashLoaded(listener);
            }
        });

        splashAD.fetchAndShowIn(splashContainer);

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
    public void express(Activity mContext,
                        String codeId,
                        boolean supportDeepLink,
                        ViewGroup viewContainer,
                        int aDViewWidth,
                        int aDViewHeight,
                        int loadCount,
                        IExpressListener mListener) {

        NativeExpressAdWrapper nativeExpressAdWrap = new NativeExpressAdWrapper(
                mContext,
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


    private String getAppId() {
        return GdtSession.get().getAppId();
    }
}
