package com.mobi.csj;

import android.content.Context;

import com.bytedance.sdk.openadsdk.TTAdConfig;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/12 16:29
 * @Dec 用于初始化穿山甲sdk
 */
public class TTSdkInit {
    /**
     * 通过这方法，通知已经初始化
     *
     * @param context
     * @param ttAdConfig
     * @param isDebug
     */
    public static void init(Context context,
                            TTAdConfig ttAdConfig,
                            boolean isDebug) {
        CsjSession.get().initTTSdk(context, ttAdConfig, isDebug);
    }

}
