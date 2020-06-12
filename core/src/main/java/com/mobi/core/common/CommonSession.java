package com.mobi.core.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.mobi.core.CoreSession;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 11:27
 * @Dec 略
 */
public class CommonSession {
    private static boolean isAppDebug = true;
    private Context mContext;
    /**
     * 这是我们自己的appId， 用于访问配置使用的
     */
    private String mAppId;


    private CommonSession() {

    }

    public static CommonSession get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CommonSession INSTANCE = new CommonSession();
    }

    public void init(Context context, String appId) {
        mContext = context.getApplicationContext();
        mAppId = appId;

        CoreSession.get().init(context, true);

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
