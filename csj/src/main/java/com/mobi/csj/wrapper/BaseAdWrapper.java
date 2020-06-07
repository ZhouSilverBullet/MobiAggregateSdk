package com.mobi.csj.wrapper;

import android.content.Context;
import android.text.TextUtils;

import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.ITTAppDownloadListener;
import com.mobi.core.strategy.AdRunnable;
import com.mobi.csj.CsjSession;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 10:56
 * @Dec 略
 */
public abstract class BaseAdWrapper extends AdRunnable implements TTAppDownloadListener {
    
    private ITTAppDownloadListener mAppDownloadListener;

    protected int getLoadCount(int loadCount) {
        int count = 1;

        if (loadCount > 0) {
            count = loadCount;
        }
        return count;
    }

    protected TTAdNative createAdNative(Context context) {
        return CsjSession.get().getAdManager().createAdNative(context);
    }

    public void setAppDownloadListener(ITTAppDownloadListener appDownloadListener) {
        this.mAppDownloadListener = appDownloadListener;
    }

    @Override
    public void onIdle() {
        if (mAppDownloadListener != null) {
            mAppDownloadListener.onIdle();
        }
    }

    @Override
    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
        if (mAppDownloadListener != null) {
            mAppDownloadListener.onDownloadActive(totalBytes, currBytes, fileName, appName);
        }
    }

    @Override
    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
        if (mAppDownloadListener != null) {
            mAppDownloadListener.onDownloadPaused(totalBytes, currBytes, fileName, appName);
        }
    }

    @Override
    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
        if (mAppDownloadListener != null) {
            mAppDownloadListener.onDownloadFailed(totalBytes, currBytes, fileName, appName);
        }
    }

    @Override
    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
        if (mAppDownloadListener != null) {
            mAppDownloadListener.onDownloadFinished(totalBytes, fileName, appName);
        }
    }

    @Override
    public void onInstalled(String fileName, String appName) {
        if (mAppDownloadListener != null) {
            mAppDownloadListener.onInstalled(fileName, appName);
        }
    }

    protected void localExecFail(BaseAdProvider provider) {
        callExecFail(provider);
    }

    /**
     * 执行任务失败的回调
     * @param provider
     */
    private void callExecFail(BaseAdProvider provider) {
        String providerType = "";
        if (provider != null && !TextUtils.isEmpty(provider.getProviderType())) {
            providerType = provider.getProviderType();
        }
        if (mCallback != null) {
            mCallback.onFail(this, providerType);
        }
    }

    @Override
    public void run() {

    }
}
