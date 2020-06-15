package com.mobi.core.listener;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 18:48
 * @Dec 略
 */
public interface ISplashAdListener extends ITTAppDownloadListener, IAdFailListener {
    /**
     * 开始
     *
     * @param providerType
     */
    void onAdStartRequest(@NonNull String providerType);

    /**
     * csj # onAdClicked
     *
     * @param type
     */
    void onAdClicked(String type);

    /**
     * csj # onAdShow
     *
     * @param type
     */
    void onAdExposure(String type);

    /**
     * csj # onAdSkip , onAdTimeOver
     *
     * @param type
     */
    void onAdDismissed(String type);

    /**
     * @param providerType
     */
    void onAdLoaded(String providerType);
}
