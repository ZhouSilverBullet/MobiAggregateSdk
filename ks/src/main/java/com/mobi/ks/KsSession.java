package com.mobi.ks;

import android.content.Context;

import com.kwad.sdk.KsAdSDK;
import com.kwad.sdk.SdkConfig;
import com.mobi.core.AdProviderManager;
import com.mobi.core.IAdSession;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/7/13 17:20
 * @Dec 略
 */
public class KsSession implements IAdSession {

    private boolean mIsDebug;
    private Context mContext;

    @Override
    public void init(Context context, String appId, String appName, boolean isDebug) {
        if (mContext != null) {
            return;
        }

        KsAdSDK.init(context, new SdkConfig.Builder()
                .appId(appId) // 测试aapId，请联系快手平台申请正式AppId，必填
                .appName(appName) // 测试appName，请填写您应用的名称，非必填
                .showNotification(true) // 是否展示下载通知栏
                .debug(true)
                .build());

        AdProviderManager.get().putCreateProvider(AdProviderManager.TYPE_KS,
                () -> new KsProvider(AdProviderManager.TYPE_KS));

        mIsDebug = isDebug;
        mContext = context;
    }

    @Override
    public boolean isInit() {
        return mIsDebug;
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
