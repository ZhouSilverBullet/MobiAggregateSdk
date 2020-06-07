package com.mobi.core.strategy;

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
}
