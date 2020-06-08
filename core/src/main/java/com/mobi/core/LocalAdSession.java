package com.mobi.core;

import android.content.Context;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/8 15:32
 * @Dec 略
 */
public class LocalAdSession implements IAdSession {


    private LocalAdSession() {
    }

    public static LocalAdSession get() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void init(Context context, String appId, String appName, boolean isDebug) {

    }

    @Override
    public boolean isInit() {
        return true;
    }

    @Override
    public Context getContext() {
        return null;
    }

    private static class SingletonHolder {
        private static final LocalAdSession INSTANCE = new LocalAdSession();
    }
}
