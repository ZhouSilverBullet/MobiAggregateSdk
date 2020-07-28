package com.mobi.ks;

import android.app.ActivityManager;
import android.content.Context;

import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.SdkConfig;
import com.mobi.core.AdProviderManager;
import com.mobi.core.IAdSession;
import com.mobi.core.utils.LogUtils;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/7/13 17:20
 * @Dec 略
 */
public class KsSession implements IAdSession {
    public static final String TAG = "KsSession";

    private boolean mIsDebug;
    private Context mContext;

    private KsSession() {
    }

    public static KsSession get() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void init(Context context, String appId, String appName, boolean isDebug) {
        if (mContext != null) {
            return;
        }

        String currentProcessName = getCurrentProcessName(context);
        if(currentProcessName.equals(context.getPackageName())) {
            // 建议只在需要的进程初始化SDK即可，如主进程
            KsAdSDK.init(context, new SdkConfig.Builder()
                    .appId(appId) // 测试aapId，请联系快手平台申请正式AppId，必填
                    .appName(appName) // 测试appName，请填写您应用的名称，非必填
                    .showNotification(true) // 是否展示下载通知栏
                    .debug(isDebug)
                    .build());

            AdProviderManager.get().putCreateProvider(AdProviderManager.TYPE_KS,
                    () -> new KsProvider(AdProviderManager.TYPE_KS));

            mIsDebug = isDebug;
            mContext = context;
            LogUtils.e(TAG, "ks init success");
        }

    }

    @Override
    public boolean isInit() {
        return mContext != null;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private static class SingletonHolder {
        private static final KsSession INSTANCE = new KsSession();
    }

    private String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        String currentProcessName = "";
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
            if (pid == processInfo.pid) {
                currentProcessName = processInfo.processName;
            }
        }
        return currentProcessName;
    }
}
