package com.mobi.common;

import android.content.Context;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 11:27
 * @Dec 略
 */
public class CommonSession {
    private static boolean isAppDebug = true;
    private Context mContext;

    private CommonSession() {
    }

    public static CommonSession get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CommonSession INSTANCE = new CommonSession();
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();

    }

    public void setIsAppDebug(boolean isDebug) {
        isAppDebug = isDebug;
    }

    public Context getContext() {
        return mContext;
    }

    public static boolean isAppDebug() {
        return isAppDebug;
    }
}
