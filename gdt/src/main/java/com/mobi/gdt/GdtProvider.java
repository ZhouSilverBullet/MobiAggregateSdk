package com.mobi.gdt;

import android.app.Activity;
import android.view.ViewGroup;

import com.mobi.core.IAdProvider;
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
public class GdtProvider implements IAdProvider {
    public static final String TAG = "GdtProvider";
    public String mProviderType;

    public GdtProvider(String providerType) {
        mProviderType = providerType;
    }

    public void splash(final Activity activity,
                       final String codeId,
                       final ViewGroup splashContainer,
                       final ISplashAdListener listener) {

        if (listener != null) {
            listener.onAdStartRequest(mProviderType);
        }

        SplashAD splashAD = new SplashAD(activity, GdtSession.get().getAppId(), codeId, new SplashADListener() {
            @Override
            public void onADDismissed() {
                if (listener != null) {
                    listener.onAdDismissed(mProviderType);
                }
            }

            @Override
            public void onNoAD(AdError adError) {
                if (listener != null) {
                    if (adError != null) {
                        listener.onAdFail(mProviderType, "code: " + adError.getErrorCode() + ", errorMsg: " + adError.getErrorMsg());
                    } else {
                        listener.onAdFail(mProviderType, "广告加载失败");
                    }
                }
            }

            @Override
            public void onADPresent() {

            }

            @Override
            public void onADClicked() {
                if (listener != null) {
                    listener.onAdClicked(mProviderType);
                }
            }

            @Override
            public void onADTick(long l) {

            }

            @Override
            public void onADExposure() {
                if (listener != null) {
                    listener.onAdExposure(mProviderType);
                }
            }

            @Override
            public void onADLoaded(long l) {
                if (listener != null) {
                    listener.onAdLoaded(mProviderType);
                }
            }
        });

        splashAD.fetchAndShowIn(splashContainer);

    }

}
