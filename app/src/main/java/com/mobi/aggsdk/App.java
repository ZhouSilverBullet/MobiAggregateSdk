package com.mobi.aggsdk;

import android.app.Application;

import com.mobi.common.MobiPubSdk;

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

//        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
//        TTAdSdk.init(this,
//                new TTAdConfig.Builder()
//                        .appId(Const.TT_APPID)
//                        .appName(Const.TT_APPNAME)
//                        //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
//                        .useTextureView(false)
//                        .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
//                        .allowShowNotify(true) //是否允许sdk展示通知栏提示
//                        .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
//                        .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
//                        .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI,
//                                TTAdConstant.NETWORK_STATE_4G) //允许直接下载的网络状态集合
//                        .supportMultiProcess(true) //是否支持多进程，true支持
////                        .httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
//                        .build());

//        TTAdManagerHolder.init(this);

        //初始化穿山甲

        MobiPubSdk.init(this, "mobiAppIdxxxxxx");



    }
}
