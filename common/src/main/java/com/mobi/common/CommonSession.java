package com.mobi.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.mobi.core.CoreSession;
import com.mobi.core.bean.ConfigBean;
import com.mobi.core.network.NetworkClient;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 11:27
 * @Dec 略
 */
public class CommonSession {
    private static boolean isAppDebug = true;
    private final Handler mHandler;
    private Context mContext;
    /**
     * 这是我们自己的appId， 用于访问配置使用的
     */
    private String mAppId;


    private CommonSession() {
        mHandler = new Handler(Looper.getMainLooper());
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

        CoreSession.get().init(context, true, new NetworkClient.InitCallback() {
            @Override
            public void onSuccess(ConfigBean configBean) {

//                CsjSession.get().init(context,
//                        Const.CSJ_APPID,
//                        Const.CSJ_APPNAME, false);
//
//                GdtSession.get().init(this, Const.GDT_APPID,
//                        BuildConfig.DEBUG);
//
//                CoreSession.get().init(this, BuildConfig.DEBUG);
            }

            @Override
            public void onFailure(int code, String message) {

            }
        });

    }

    public void runOnUiThread(Runnable runnable) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
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