package com.mobi.core.listener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 11:17
 * @Dec 略
 */
public interface IInteractionAdListener extends ITTAppDownloadListener, IAdFailListener {

    /**
     * csj 的show 和 gdt的曝光，通过为曝光
     *
     * @param type
     */
    void onAdExposure(String type);

    void onAdClick(String type);

    void onAdClose(String providerType);

    /**
     * csj 独有
     *
     * @param type
     */
    default void onCsjLoad(String type) {

    }

    /**
     * gdt
     *
     * @param type
     */
    default void onAdGdtShow(String type) {

    }

    /**
     * gdt
     *
     * @param type
     */
    default void onGdtOpened(String type) {

    }

    /**
     * gdt 独有
     *
     * @param type
     */
    default void onGdtCached(String type) {

    }

    /**
     * gdt 独有
     *
     * @param type
     */
    default void onGdtLeftApplication(String type) {

    }

}
