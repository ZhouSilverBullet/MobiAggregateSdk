package com.mobi.core.strategy.impl;

import com.mobi.core.utils.LogUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 13:20
 * @Dec 略
 */
public class ServiceOrderAdStrategy extends BaseShowAdStrategy {

    private int mSize;
    /**
     * 当前执行的下标
     * 默认开始是从0开始
     */
    private int execIndex;

    @Override
    public void execRun() {
        mSize = getAdRunnableSyncList().size();
        execIndex = 0;
        if (mSize > 0) {
            getHandler().post(getAdRunnableSyncList().get(execIndex));
        }
    }

    @Override
    public void onSuccess(Runnable runnable, String provideType) {
        LogUtils.e("onSuccess provideType : " + provideType);
    }

    @Override
    public void onRenderFail(Runnable runnable, String provideType) {
        handleAdFail();
    }

    @Override
    public void onFail(Runnable runnable, String provideType) {
        LogUtils.e("onFail provideType : " + provideType);
        if (mSize > 0) {
            execIndex++;
            if (execIndex == mSize) {
                LogUtils.e("onFail runnable 执行完成 provideType : " + provideType);
                //处理错误
                handleAdFail();
                return;
            }
            getHandler().post(getAdRunnableSyncList().get(execIndex % mSize));
        }
    }


}
