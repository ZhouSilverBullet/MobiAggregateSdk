package com.mobi.unityads;

import android.app.Activity;

import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

/**
 * unit 调用这个进行初始化
 */
public class UnityInit {

    public static final int SDK_CODE_10009 = 10009;
    public static final String SDK_MESSAGE_10009 = "unity is not ready ad";

    public static void init(Activity activity, String gameId, boolean isDebug) {
        UnityAds.initialize(activity, gameId, isDebug);
        UnitySession.get().init(activity.getApplicationContext(), gameId, "", isDebug);
    }

    public static void setDebugMode(boolean isDebugMode) {
        UnityAds.setDebugMode(isDebugMode);
    }

    public static void addListener(IUnityAdsListener listener) {
        UnityAds.addListener(listener);
    }

    public static void removeListener(IUnityAdsListener listener) {
        UnityAds.removeListener(listener);
    }
}
