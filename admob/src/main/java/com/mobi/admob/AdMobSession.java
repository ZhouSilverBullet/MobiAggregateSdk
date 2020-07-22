package com.mobi.admob;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.mobi.core.AdProviderManager;
import com.mobi.core.IAdSession;
import com.mobi.core.utils.LogUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/7/13 17:20
 * @Dec 略
 */
public class AdMobSession implements IAdSession {
    public static final String TAG = "AdMobSession";

    private boolean mIsDebug;
    private Context mContext;

    private AdMobSession() {
    }

    public static AdMobSession get() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void init(Context context, String appId, String appName, boolean isDebug) {
        if (mContext != null) {
            return;
        }

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.e(TAG, initializationStatus.toString());
                LogUtils.e(TAG, "admob init success");
            }
        });


        AdProviderManager.get().putCreateProvider(AdProviderManager.TYPE_ADMOB,
                () -> new AdMobProvider(AdProviderManager.TYPE_ADMOB));

        mIsDebug = isDebug;
        mContext = context;
        LogUtils.e(TAG, "admob init start");
    }

    @Override
    public boolean isInit() {
        return mContext != null;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private static class SingletonHolder {
        private static final AdMobSession INSTANCE = new AdMobSession();
    }
}
