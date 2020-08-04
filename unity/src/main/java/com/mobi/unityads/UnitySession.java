package com.mobi.unityads;

import android.content.Context;

import com.mobi.core.AdProviderManager;
import com.mobi.core.IAdSession;
import com.mobi.core.utils.LogUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/7/13 17:20
 * @Dec 略
 */
public class UnitySession implements IAdSession {
    public static final String TAG = "KsSession";

    private boolean mIsDebug;
    private Context mContext;

    private UnitySession() {
    }

    public static UnitySession get() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void init(Context context, String appId, String appName, boolean isDebug) {
        if (mContext != null) {
            return;
        }

        mIsDebug = isDebug;
        mContext = context;

        AdProviderManager.get().putCreateProvider(AdProviderManager.TYPE_UNITY_SDK,
                () -> new UnityProvider(AdProviderManager.TYPE_UNITY_SDK));

        LogUtils.e(TAG, "UnitySession init call");
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
        private static final UnitySession INSTANCE = new UnitySession();
    }


}
