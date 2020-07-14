package com.mobi.core.feature;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/16 14:22
 * @Dec 略
 */
public interface IAdView {
    /**
     * 调用显示
     */
    void show();

    /**
     * 销毁回调
     */
    default void onDestroy() {

    }

}
