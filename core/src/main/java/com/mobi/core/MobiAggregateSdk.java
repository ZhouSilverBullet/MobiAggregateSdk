package com.mobi.core;

import android.app.Activity;
import android.app.FragmentManager;
import android.view.ViewGroup;

import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
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

    public static final String CSJ_REWARD_ID = "901121365";
    public static final String GDT_REWARD_ID = "2090845242931421";


    public static final String CSJ_INTERACTION_ID = "901121797";

    public static final String CSJ_EXPRESS_ID = "901121125";
    public static final String GDT_EXPRESS_ID = "2000629911207832";

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

    public static void showFullscreen(final Activity activity, int orientation, final IFullScreenVideoAdListener listener) {
        AdProviderManager.get().getProvider(AdProviderManager.TYPE_CSJ)
                .fullscreen(activity, "901121073", orientation, true, listener);
    }

    public static void showRewardView(final Activity activity,
                                      final String codeId,
                                      boolean supportDeepLink,
                                      final IRewardAdListener listener) {
        AdProviderManager.get().getProvider(AdProviderManager.TYPE_GDT)
                .rewardVideo(activity, codeId, supportDeepLink, listener);
    }

    /**
     * 插屏
     *
     * @param activity
     * @param codeId
     * @param supportDeepLink
     * @param listener
     */
    public static void showInteractionExpress(final Activity activity,
                                              final ViewGroup viewContainer,
                                              final String codeId,
                                              boolean supportDeepLink,
                                              final IInteractionAdListener listener) {

        AdProviderManager.get().getProvider(AdProviderManager.TYPE_GDT)
                .interactionExpress(activity,
                        codeId,
                        supportDeepLink,
                        viewContainer,
                        300,
                        300, listener);
    }


    /**
     * 插屏
     *
     * @param activity
     * @param codeId
     * @param supportDeepLink
     * @param listener
     */
    public static void showExpress(final Activity activity,
                                   final ViewGroup viewContainer,
                                   final String codeId,
                                   boolean supportDeepLink,
                                   int aDViewWidth,
                                   int aDViewHeight,
                                   int loadCount,
                                   final IExpressListener listener) {

//        FragmentManager fragmentManager =
//                activity.getFragmentManager();
//        fragmentManager.beginTransaction()
//                .add(new AdFragment(), "mobiad")
//                .commitAllowingStateLoss();

        AdProviderManager.get().getProvider(AdProviderManager.TYPE_CSJ)
                .express(activity,
                        codeId,
                        supportDeepLink,
                        viewContainer,
                        aDViewWidth,
                        aDViewHeight,
                        loadCount,
                        listener);
    }
}
