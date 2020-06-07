package com.mobi.core.strategy.impl;

import android.os.Handler;
import android.os.Looper;

import com.mobi.core.strategy.AdRunnable;
import com.mobi.core.strategy.IShowAdStrategy;
import com.mobi.core.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 11:57
 * @Dec 略
 */
public abstract class BaseShowAdStrategy implements IShowAdStrategy, AdRunnable.ExecCallback {
    public static final String TAG = "BaseShowAdStrategy";

    private final Handler mHandler;
    /**
     * 本地的广告任务
     */
    private List<AdRunnable> mAdRunnableSyncList;

    public BaseShowAdStrategy() {
        mHandler = new Handler(Looper.getMainLooper());
        mAdRunnableSyncList = Collections.synchronizedList(new ArrayList<>());
    }


    @Override
    public void addADTask(List<AdRunnable> runList) {
        if (runList == null || runList.size() == 0) {
            LogUtils.e(TAG, "AdRunnable list add null or isEmpty");
            return;
        }
        mAdRunnableSyncList.addAll(runList);
    }

    @Override
    public void addADTask(AdRunnable run) {
        if (run == null) {
            LogUtils.e(TAG, "AdRunnable add null or isEmpty");
            return;
        }
        mAdRunnableSyncList.add(run);
    }

    @Override
    public final void execShow() {
        registerAdTaskCallBack();

        execRun();
    }

    /**
     * 执行具体策略的逻辑
     */
    public abstract void execRun();

    /**
     * 注册广告监听回调
     */
    private void registerAdTaskCallBack() {
        for (AdRunnable adRunnable : mAdRunnableSyncList) {
            adRunnable.setCallback(this);
        }
    }

    /**
     * 给子类使用
     * @return
     */
    protected Handler getHandler() {
        return mHandler;
    }

    public List<AdRunnable> getAdRunnableSyncList() {
        return mAdRunnableSyncList;
    }
}
