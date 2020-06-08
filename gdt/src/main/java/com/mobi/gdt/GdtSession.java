package com.mobi.gdt;

import android.content.Context;

import com.mobi.core.AdProviderManager;
import com.mobi.core.IAdSession;
import com.qq.e.comm.managers.GDTADManager;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 19:53
 * @Dec 略
 */
public class GdtSession implements IAdSession {

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

    @Override
    public void init(Context context, String gdtAdAppId, String appName, boolean isDebug) {
        //给予多次调用，防止多次进行初始化
        if (mContext != null) {
            return;
        }

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

