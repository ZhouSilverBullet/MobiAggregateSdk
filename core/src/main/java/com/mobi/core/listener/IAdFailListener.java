package com.mobi.core.listener;

import com.mobi.core.strategy.StrategyError;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 13:58
 * @Dec
 * 统一的加载错误类 直到整个策略执行完毕，且错误了
 * 告知外面 or 者上报数据
 */
public interface IAdFailListener {
    void onAdFail(List<StrategyError> strategyErrorList);
}
