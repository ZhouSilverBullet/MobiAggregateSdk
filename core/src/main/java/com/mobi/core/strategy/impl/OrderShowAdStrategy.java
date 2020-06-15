package com.mobi.core.strategy.impl;

import com.mobi.core.CoreSession;
import com.mobi.core.strategy.TimeOutRunnable;
import com.mobi.core.utils.LogUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 13:20
 * @Dec 略
 */
public class OrderShowAdStrategy extends BaseShowAdStrategy implements TimeOutRunnable.TimeOutCallback {

    private int mSize;
    /**
     * 当前执行的下标
     * 默认开始是从0开始
     */
    private int execIndex;
    private TimeOutRunnable mTimeOutRunnable;

    @Override
    public void execRun() {
        mSize = getAdRunnableSyncList().size();
        execIndex = 0;
        if (mSize > 0) {
            getHandler().post(getAdRunnableSyncList().get(execIndex));
            mTimeOutRunnable = new TimeOutRunnable(execIndex, this);
            getHandler().postDelayed(mTimeOutRunnable, CoreSession.get().getTimeOut());
        }
    }

    @Override
    public void onSuccess(Runnable runnable, String provideType) {
        getHandler().removeCallbacks(mTimeOutRunnable);
        LogUtils.e("onSuccess provideType : " + provideType);
    }

    @Override
    public void onRenderFail(Runnable runnable, String provideType) {
        LogUtils.e(TAG, "AdRunnable onRenderFail provideType = " + provideType);
        onFail(runnable, provideType);
    }

    @Override
    public void onFail(Runnable runnable, String provideType) {
        LogUtils.e("onFail provideType : " + provideType);
        if (mSize > 0) {
            execIndex++;
            getHandler().removeCallbacks(mTimeOutRunnable);

            if (execIndex >= mSize) {
                LogUtils.e("onFail runnable 执行完成 provideType : " + provideType);
                //处理错误
                handleAdFail();
                return;
            }

            getHandler().post(getAdRunnableSyncList().get(execIndex % mSize));
            //new 一个新的timeout判断
            mTimeOutRunnable = new TimeOutRunnable(execIndex % mSize, this);
            getHandler().postDelayed(mTimeOutRunnable, CoreSession.get().getTimeOut());
        }
    }


    @Override
    public void onTimeOut(int type) {
        LogUtils.e(TAG, "OrderShowAdStrategy 请求超时");
        getAdRunnableSyncList().get(type).setTimeOut(true);
    }
}
