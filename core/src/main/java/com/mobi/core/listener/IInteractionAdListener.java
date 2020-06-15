package com.mobi.core.listener;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 11:17
 * @Dec 略
 */
public interface IInteractionAdListener extends ITTAppDownloadListener, IAdFailListener {
    /**
     *
     * @param providerType
     * @param code
     * @param errorMsg
     * {@link IAdFailListener#onAdFail(List)}
     */
    @Deprecated
    default void onAdFail(String providerType, int code, String errorMsg) {

    }

    /**
     * gdt
     * @param type
     */
    void onADOpened(String type);

    /**
     * gdt
     * @param type
     */
    void onADExposure(String type);

    /**
     * csj 独有
     * @param type
     */
    void onAdLoad(String type);

    void onAdShow(String type);
    void onAdClick(String type);
    void onAdClose(String providerType);

    /**
     * gdt 独有
     * @param type
     */
    void onCached(String type);

    default void onADLeftApplication(String type) {

    }




}
