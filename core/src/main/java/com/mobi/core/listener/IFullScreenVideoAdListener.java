package com.mobi.core.listener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/2 21:39
 * @Dec 略
 */
public interface IFullScreenVideoAdListener extends ITTAppDownloadListener, IAdFailListener {
    void onAdShow(String type);

    void onAdFail(String type, int code, String errorMsg);

    void onAdLoad(String type);

    void onCached(String type);

    void onAdClose(String providerType);

    void onVideoComplete(String providerType);

    void onSkippedVideo(String providerType);

    void onAdVideoBarClick(String providerType);

}
