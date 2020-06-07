package com.mobi.core.strategy.impl;

import com.mobi.core.strategy.AdRunnable;
import com.mobi.core.utils.LogUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 22:06
 * @Dec 略
 */
public class OrderShowAdStrategy extends BaseShowAdStrategy {
    public static final String TAG = "OrderShowAdStrategy";
    AtomicInteger mFailCount;

    @Override
    public void execRun() {
        mFailCount = new AtomicInteger();
        //执行
        for (AdRunnable runnable : getAdRunnableSyncList()) {
            getHandler().post(runnable);
        }
    }

    @Override
    public void onSuccess(Runnable runnable, String provideType) {
        LogUtils.e(TAG, "AdRunnable onSuccess provideType = " + provideType);
        //一旦有任务完成就进行cancel
        //是这个策略
        for (AdRunnable adRunnable : getAdRunnableSyncList()) {
            adRunnable.setCancel(true);
        }
        //策略执行完毕
        setStrategyFinished(true);
    }

    @Override
    public void onFail(Runnable runnable, String provideType) {
        // todo 渲染失败，重新渲染，还是渲染下一个的问题，任务取消了，不会走这个方法
        LogUtils.e(TAG, "AdRunnable onFail provideType = " + provideType);
        //策略执行完毕
        if (mFailCount.incrementAndGet() == getAdRunnableSyncList().size()) {
            setStrategyFinished(true);
            //处理错误
            handleAdFail();
        }

    }
}
