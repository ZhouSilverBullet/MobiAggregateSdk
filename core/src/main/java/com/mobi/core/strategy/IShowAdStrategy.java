package com.mobi.core.strategy;

import com.mobi.core.listener.IAdFailListener;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 22:04
 * @Dec 广告策略
 */
public interface IShowAdStrategy {

    /**
     *
     * @param runList
     */
    void addADTask(List<AdRunnable> runList);

    /**
     *
     * @param run
     */
    void addADTask(AdRunnable run);

    /**
     * 执行显示策略
     */
    void execShow();


    /**
     * 判断整个策略是否已经完成
     *
     * @return
     */
    boolean isStrategyFinished();

    /**
     * 执行任务完成后，如果全部失败了，那就往外面传递失败的消息
     * @param adFailListener
     */
    void setAdFailListener(IAdFailListener adFailListener);

}
