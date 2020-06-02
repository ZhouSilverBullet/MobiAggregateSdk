package com.mobi.gdt;

import android.content.Context;

import com.mobi.core.AdProviderManager;
import com.qq.e.comm.managers.GDTADManager;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 19:53
 * @Dec 略
 */
public class GdtSession {

    private String appId;

    private GdtSession() {
    }

    public static GdtSession get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final GdtSession INSTANCE = new GdtSession();
    }

    //默认为true
    private static boolean isAppDebug = true;
    private Context mContext;

    public void init(Context context, String gdtAdAppId, boolean isDebug) {
        isAppDebug = isDebug;
        mContext = context.getApplicationContext();

        this.appId = gdtAdAppId;

        GDTADManager.getInstance().initWith(context, gdtAdAppId);

        AdProviderManager.get().putProvider(AdProviderManager.TYPE_GDT,
                new GdtProvider(AdProviderManager.TYPE_GDT));
    }

    public String getAppId() {
        return appId;
    }

    public Context getContext() {
        return mContext;
    }

    public static boolean isAppDebug() {
        return isAppDebug;

    }
}

