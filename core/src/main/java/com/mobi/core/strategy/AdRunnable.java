package com.mobi.core.strategy;

import android.text.TextUtils;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.mobi.core.MobiConstantValue.*;

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

    /**
     * 服务器返回的时候，给了一个广告超时时间
     */
    private boolean isTimeOut;

    public boolean isTimeOut() {
        return isTimeOut;
    }

    public void setTimeOut(boolean timeOut) {
        isTimeOut = timeOut;
    }

    /**
     * if
     * 错误时，收集策略，直到策略执行完成后
     * 再往后台或者 user 发送错误消息
     * else
     * 只要策略正确的占位执行，那就证明，user 不需要知道错误原因
     */
    private List<StrategyError> mStrategyErrorList;

    public List<StrategyError> getStrategyErrorList() {
        return mStrategyErrorList;
    }

    /**
     * 广告显示成功后，广告需要回调一下，让外界的策略知晓
     *
     * @param provider
     */
    protected void localExecSuccess(BaseAdProvider provider) {
        if (mCallback != null) {
            mCallback.onSuccess(this, getProviderType(provider));
        }
    }


    /**
     * 执行任务失败的回调
     *
     * @param provider
     */
    protected void localExecFail(BaseAdProvider provider, int code, String message) {

        String providerType = getProviderType(provider);

        saveFailMessage(provider, providerType, code, message);

        LogUtils.e(TAG, " localExecFail type: " + providerType + " code: " + code + ", message: " + message);
        if (isCancel) {
            LogUtils.e(TAG, " isCancel is true localExecFail no callback type: " + providerType);
            return;
        }
        if (mCallback != null) {
            mCallback.onFail(this, providerType);
        }
    }

    private void saveFailMessage(BaseAdProvider provider, String providerType, int code, String message) {
        if (provider != null) {
            provider.trackFail();
        }

        if (mStrategyErrorList == null) {
            mStrategyErrorList = new ArrayList<>();
        }

        mStrategyErrorList.add(new StrategyError(providerType, code, message));
    }

    /**
     * 让策略类知道，做一些上传错误操作
     *
     * @param provider
     * @param code
     * @param message
     */
    protected void localRenderFail(BaseAdProvider provider, int code, String message) {
        String providerType = getProviderType(provider);

        saveFailMessage(provider, providerType, code, message);

        LogUtils.e(TAG, " localRenderFail type: " + providerType + " code: " + code + ", message: " + message);
//        if (isCancel) {
//            LogUtils.e(TAG, " isCancel is true localExecFail no callback type: " + providerType);
//            return;
//        }

        if (mCallback != null) {
            mCallback.onRenderFail(this, providerType);
        }
    }

    private String getProviderType(BaseAdProvider provider) {
        String providerType = "";
        if (provider != null && !TextUtils.isEmpty(provider.getProviderType())) {
            providerType = provider.getProviderType();
        }
        return providerType;
    }

    protected boolean checkPostIdEmpty(BaseAdProvider provider, String postId) {
        if (TextUtils.isEmpty(postId)) {
            localExecFail(provider, SDK_CODE_2004, SDK_MESSAGE_2004);
            return true;
        }
        return false;
    }

    /**
     * 用于判断是否执行完毕
     */
    protected ExecCallback mCallback;

    public void setCallback(ExecCallback callback) {
        mCallback = callback;
    }

    public interface ExecCallback {
        /**
         * 加载成功，有可能会渲染失败
         *
         * @param runnable
         * @param provideType
         */
        void onSuccess(Runnable runnable, String provideType);

        /**
         * 加载成功后，渲染失败回调
         */
        void onRenderFail(Runnable runnable, String provideType);

        void onFail(Runnable runnable, String provideType);
    }
}
