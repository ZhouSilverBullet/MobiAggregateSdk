package com.mobi.core.strategy.impl;

import com.mobi.core.CoreSession;
import com.mobi.core.strategy.AdRunnable;
import com.mobi.core.utils.LogUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 22:06
 * @Dec 略
 */
public class ServiceOrderShowAdStrategy extends BaseShowAdStrategy implements TimeOutRunnable.TimeOutCallback {
    public static final String TAG = "OrderShowAdStrategy";
    AtomicInteger mFailCount;
    private TimeOutRunnable mTimeOutRunnable;

    @Override
    public void execRun() {
        mFailCount = new AtomicInteger();

        //执行第一个，已经筛选好的
        AdRunnable r = getAdRunnableSyncList().get(0);
        getHandler().post(r);
        mTimeOutRunnable = new TimeOutRunnable(0, this);
        getHandler().postDelayed(mTimeOutRunnable,
                CoreSession.get().getTimeOut());
    }

    @Override
    public void onSuccess(Runnable runnable, String provideType) {
        //成功了第一时间吧这个TimeOutRunnable移除掉
        removeTimeOutRunnable();

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
        //执行超时了
//        AdRunnable r = getAdRunnableSyncList().get(0);
        removeTimeOutRunnable();

        if (type == 0) {
            LogUtils.e(TAG, " 超时了 超时了 timeOut: " + CoreSession.get().getTimeOut());

            for (int i = 1; i < getAdRunnableSyncList().size(); i++) {
                //所有都执行一遍，如果超时了还没显示的话
                getHandler().post(getAdRunnableSyncList().get(i));
            }

            mTimeOutRunnable = new TimeOutRunnable(1, this);
            getHandler().postDelayed(mTimeOutRunnable, CoreSession.get().getTimeOut());
        } else {
            LogUtils.e(TAG, "所有的请求都 超时了 ");
            //全部设置超时
            for (AdRunnable runnable : getAdRunnableSyncList()) {
                runnable.setTimeOut(true);
            }
        }
    }

    private void removeTimeOutRunnable() {
        if (mTimeOutRunnable != null) {
            getHandler().removeCallbacks(mTimeOutRunnable);
            //改变mTimeOutRunnable的指向
            mTimeOutRunnable = null;
        }
    }
}
