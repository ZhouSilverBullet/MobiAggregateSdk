package com.mobi.mobiexchanger;

import android.content.Context;

import com.mobi.core.AdProviderManager;
import com.mobi.core.IAdSession;
import com.mobi.core.common.MobiPubSdk;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 18:04
 * @Dec 略
 */
public class MobiAdSession implements IAdSession {
    public static final String TAG = "MobiAdSession";
    //默认为true
    private static boolean isAppDebug = true;
    private Context mContext;

    private MobiAdSession() {
    }

    public static MobiAdSession get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final MobiAdSession INSTANCE = new MobiAdSession();
    }

    @Override
    public void init(Context context, String appId, String appName, boolean isDebug) {
        if (mContext != null) {
            return;
        }

        MobiPubSdk.init(context, appId, isDebug);

        AdProviderManager.get().putCreateProvider(AdProviderManager.TYPE_MOBI,
                () -> new MobiAdProvider(AdProviderManager.TYPE_MOBI));

        isAppDebug = isDebug;
        mContext = context;
    }


    @Override
    public Context getContext() {
        return mContext;
    }

    public static boolean isAppDebug() {
        return isAppDebug;
    }

    @Override
    public boolean isInit() {
        return mContext != null;
    }
}
