package com.mobi.core;

import android.content.Context;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 18:27
 * @Dec 略
 */
public class CoreSession {

    private static boolean isAppDebug;
    private Context mContext;

    private CoreSession() {
    }

    public static CoreSession get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CoreSession INSTANCE = new CoreSession();
    }

    public void init(Context context, boolean isDebug) {
        mContext = context.getApplicationContext();
        isAppDebug = isDebug;
    }

    public Context getContext() {
        return mContext;
    }

    public static boolean isAppDebug() {
        return isAppDebug;
    }
}
