package com.mobi.gdt;

import android.app.Activity;
import android.view.ViewGroup;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.IAdProvider;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.ISplashAdListener;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

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
}
