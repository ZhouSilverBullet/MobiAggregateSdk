package com.mobi.core.listener;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/2 21:39
 * @Dec 略
 */
public interface IFullScreenVideoAdListener extends ITTAppDownloadListener, IAdFailListener {
    void onAdExposure(String providerType);

    void onAdClick(String providerType);

    void onAdClose(String providerType);

    void onAdLoad(String providerType);

    void onCached(String providerType);

    void onVideoComplete(String providerType);

    void onSkippedVideo(String providerType);

}
