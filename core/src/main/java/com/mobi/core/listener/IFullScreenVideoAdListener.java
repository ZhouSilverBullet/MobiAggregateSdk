package com.mobi.core.listener;

import com.mobi.core.feature.FullscreenAdView;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/2 21:39
 * @Dec 略
 */
public interface IFullScreenVideoAdListener extends ITTAppDownloadListener, IAdFailListener {

    void onAdLoad(String providerType, FullscreenAdView view);

    /**
     * 全屏广告显示
     *
     * @param providerType
     */
    void onAdExposure(String providerType);

    /**
     * 全屏广告点击
     *
     * @param providerType
     */
    void onAdClick(String providerType);

    /**
     * 全屏广告关闭
     *
     * @param providerType
     */
    void onAdClose(String providerType);

    /**
     * 全屏广告缓存
     *
     * @param providerType
     */
    void onCached(String providerType);

    /**
     * 全屏广告播放完成
     *
     * @param providerType
     */
    void onVideoComplete(String providerType);

    /**
     * 全屏广告跳过
     *
     * @param providerType
     */
    void onSkippedVideo(String providerType);

}
