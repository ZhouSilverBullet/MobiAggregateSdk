package com.mobi.core;

import android.app.Activity;
import android.view.ViewGroup;

import com.mobi.core.listener.ISplashAdListener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/2 20:56
 * @Dec 略
 */
public class MobiAggregateSdk {

    public static final String CSJ_SPLASH_ID = "801121648";
    public static final String GDT_SPLASH_ID = "8863364436303842593";

    public static void showSplash(final Activity activity,
                                  final ViewGroup splashContainer,
                                  final ISplashAdListener listener) {
        String providerKey = AdProviderManager.get().getProviderKey();

        String codeId = "";
        switch (providerKey) {
            case AdProviderManager.TYPE_CSJ:
                codeId = CSJ_SPLASH_ID;
                break;
            default:
                codeId = GDT_SPLASH_ID;
                break;
        }
        AdProviderManager.get().getProvider(providerKey)
                .splash(activity, codeId, splashContainer, listener);
    }
}
