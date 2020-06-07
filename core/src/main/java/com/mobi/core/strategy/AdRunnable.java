package com.mobi.core.strategy;

import android.text.TextUtils;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.utils.LogUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 11:02
 * @Dec 略
 */
public abstract class AdRunnable implements Runnable {
    public static final String TAG = "AdRunnable";
    /**
     * 是否执行成功
     */
    private boolean isExecSuccess;

    public void setExecSuccess(boolean execSuccess) {
        isExecSuccess = execSuccess;
    }

    public boolean isExecSuccess() {
        return isExecSuccess;
    }

    /**
     * 是否需要取消任务，根绝策略来定
     */
    private boolean isCancel;

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    protected void localExecSuccess(BaseAdProvider provider) {
        if (mCallback != null) {
            mCallback.onSuccess(this, getProviderType(provider));
        }
    }

    /**
     * 执行任务失败的回调
     * @param provider
     */
    protected void localExecFail(BaseAdProvider provider) {
        if (isCancel) {
            LogUtils.e(TAG, " isCancel is true localExecFail no callback type: " + getProviderType(provider));
            return;
        }
        if (mCallback != null) {
            mCallback.onFail(this, getProviderType(provider));
        }
    }

    private String getProviderType(BaseAdProvider provider) {
        String providerType = "";
        if (provider != null && !TextUtils.isEmpty(provider.getProviderType())) {
            providerType = provider.getProviderType();
        }
        return providerType;
    }

    /**
     * 用于判断是否执行完毕
     */
    protected ExecCallback mCallback;

    public void setCallback(ExecCallback callback) {
        mCallback = callback;
    }

    public interface ExecCallback {
        void onSuccess(Runnable runnable, String provideType);
        void onFail(Runnable runnable, String provideType);
    }
}
