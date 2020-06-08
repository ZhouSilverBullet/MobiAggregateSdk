package com.mobi.core.listener;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/2 21:39
 * @Dec 略
 */
public interface IFullScreenVideoAdListener extends ITTAppDownloadListener, IAdFailListener {
    void onAdShow(String providerType);

    /**
     * @param providerType
     * @param code
     * @param errorMsg
     * {@link IAdFailListener#onAdFail(List)}
     */
    @Deprecated
    default void onAdFail(String providerType, int code, String errorMsg) {

    }

    void onAdLoad(String providerType);

    void onCached(String providerType);

    void onAdClose(String providerType);

    void onVideoComplete(String providerType);

    void onSkippedVideo(String providerType);

    void onAdVideoBarClick(String providerType);

}
