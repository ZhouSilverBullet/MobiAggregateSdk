package com.mobi.core;

import android.content.Context;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/8 15:32
 * @Dec 本地弄个假的做占位使用的
 */
public class FakeAdSession implements IAdSession {


    private FakeAdSession() {
    }

    public static FakeAdSession get() {
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
        private static final FakeAdSession INSTANCE = new FakeAdSession();
    }
}
