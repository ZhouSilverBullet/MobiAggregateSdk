package com.mobi.aggsdk;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.mobi.core.common.MobiPubSdk;
import com.mobi.csj.TTSdkInit;

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


        MobiPubSdk.init(this, "mobiAppIdxxxxxx");

        //初始化穿山甲
        TTSdkInit.init(this,
                buildConfig(this, Const.CSJ_APPID, Const.CSJ_APPNAME),
                BuildConfig.DEBUG);

    }

    private TTAdConfig buildConfig(Context context, String appId, String appName) {
        return new TTAdConfig.Builder()
                .appId(appId)
                .appName(appName)
                .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(false) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI,
                        TTAdConstant.NETWORK_STATE_MOBILE) //允许直接下载的网络状态集合
                .supportMultiProcess(true)//是否支持多进程
//                .needClearTaskReset()
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
