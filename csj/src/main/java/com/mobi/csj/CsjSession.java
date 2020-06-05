package com.mobi.csj;

import android.content.Context;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.mobi.core.AdProviderManager;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 18:04
 * @Dec 略
 */
public class CsjSession {
    //默认为true
    private static boolean isAppDebug = true;
    private TTAdManager mTTAdManager;
    private Context mContext;

    private CsjSession() {
    }

    public static CsjSession get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CsjSession INSTANCE = new CsjSession();
    }

    public void init(Context context, String appId, String appName, boolean isDebug) {
        if (mTTAdManager != null) {
            return;
        }

        isAppDebug = isDebug;
        mContext = context.getApplicationContext();
        mTTAdManager = TTAdSdk.init(context.getApplicationContext(),
                buildConfig(context, appId, appName));

        AdProviderManager.get().putProvider(AdProviderManager.TYPE_CSJ,
                new CsjProvider(AdProviderManager.TYPE_CSJ));

    }

    public TTAdManager getAdManager() {
        if (mTTAdManager == null) {
            mTTAdManager = TTAdSdk.getAdManager();
        }
        return mTTAdManager;
    }

    public Context getContext() {
        return mContext;
    }

    public static boolean isAppDebug() {
        return isAppDebug;
    }

    private static TTAdConfig buildConfig(Context context, String appId, String appName) {
        return new TTAdConfig.Builder()
                .appId(appId)
                .appName(appName)
                .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(isAppDebug) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI,
                        TTAdConstant.NETWORK_STATE_MOBILE) //允许直接下载的网络状态集合
                .supportMultiProcess(true)//是否支持多进程
                .needClearTaskReset()
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build();
    }
}
