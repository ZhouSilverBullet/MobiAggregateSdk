package com.mobi.core.strategy;

import com.mobi.core.strategy.impl.OrderShowAdStrategy;
import com.mobi.core.strategy.impl.ServiceOrderAdStrategy;
import com.mobi.core.strategy.impl.TogetherShowAdStrategy;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 12:10
 * @Dec 略
 */
public class AdStrategyFactory {
    public static final int SORT_TYPE_ORDER = 1;//广告展示顺序类型 按顺序
    public static final int SORT_TYPE_PRICE = 2;//广告展示顺序类型 按价格
    public static final int SORT_TYPE_ORDER_PRICE = 3;//广告展示顺序类型 优先顺序 顺序相同则按价格
    public static final int SORT_TYPE_SERVICE_ORDER = 4;//按照服务器返回的固定顺序加载对应平台的广告
    public static final int SORT_TYPE_TOGETHER = 5;//按照服务器返回的固定顺序加载对应平台的广告


    public static IShowAdStrategy create(int type) {
        if (type == SORT_TYPE_ORDER) {
            return new OrderShowAdStrategy();
        } else if (type == SORT_TYPE_SERVICE_ORDER) {
            return new ServiceOrderAdStrategy();
        } else if (type == SORT_TYPE_TOGETHER) {
            return new TogetherShowAdStrategy();
        }
        return null;
    }


}
