package com.mobi.core.listener;

import android.support.annotation.NonNull;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 18:48
 * @Dec 略
 */
public interface ISplashAdListener {
    /**
     * 开始
     *
     * @param providerType
     */
    void onAdStartRequest(@NonNull String providerType);

    /**
     * csj # onTimeout , onError
     *
     * @param type
     * @param code
     * @param errorMsg
     */
    void onAdFail(String type, int code, String errorMsg);

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
