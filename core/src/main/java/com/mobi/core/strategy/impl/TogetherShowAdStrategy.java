package com.mobi.core.strategy.impl;

import com.mobi.core.CoreSession;
import com.mobi.core.strategy.AdRunnable;
import com.mobi.core.strategy.TimeOutRunnable;
import com.mobi.core.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 22:06
 * @Dec {@link com.mobi.core.strategy.AdStrategyFactory#SORT_TYPE_TOGETHER}
 * 同时请求
 */
public class TogetherShowAdStrategy extends BaseShowAdStrategy implements TimeOutRunnable.TimeOutCallback {
    public static final String TAG = TogetherShowAdStrategy.class.getSimpleName();
    AtomicInteger mFailCount;
    private List<Runnable> mRunnableList;

    @Override
    public void execRun() {
        mFailCount = new AtomicInteger();
        mRunnableList = new ArrayList<>();
        //执行
        int index = 0;
        for (AdRunnable runnable : getAdRunnableSyncList()) {
            getHandler().post(runnable);

            //启动超时策略和timeOut
            TimeOutRunnable timeOutRunnable = new TimeOutRunnable(index++, this);
            getHandler().postDelayed(timeOutRunnable, CoreSession.get().getTimeOut());
            mRunnableList.add(timeOutRunnable);

        }
        LogUtils.e(TAG, " timeOut: " + CoreSession.get().getTimeOut());
    }

    @Override
    public void onSuccess(Runnable runnable, String provideType) {
        //成功后去除所有的超时回调
        for (Runnable timeOutRunnable : mRunnableList) {
            getHandler().removeCallbacks(timeOutRunnable);
        }

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
    public void onRenderFail(Runnable runnable, String provideType) {
        LogUtils.e(TAG, "AdRunnable onRenderFail provideType = " + provideType);
        onFail(runnable, provideType);
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

    @Override
    public void onTimeOut(int type) {
        if (type >= 0) {
            int index = type % getAdRunnableSyncList().size();
            getAdRunnableSyncList().get(index).setTimeOut(true);
        }
        LogUtils.e(TAG, "超时了 type：" + type + " --> " +  type % getAdRunnableSyncList().size());
    }
}
