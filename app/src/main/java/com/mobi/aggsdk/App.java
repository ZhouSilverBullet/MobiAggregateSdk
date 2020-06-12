package com.mobi.aggsdk;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.mobi.core.common.MobiPubSdk;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 14:53
 * @Dec 略
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化穿山甲
        MobiPubSdk.init(this, "mobiAppIdxxxxxx");

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
