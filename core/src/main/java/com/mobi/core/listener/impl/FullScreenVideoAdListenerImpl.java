package com.mobi.core.listener.impl;

import com.mobi.core.feature.FullscreenAdView;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.strategy.StrategyError;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/7/29 19:50
 * @Dec 略
 */
public class FullScreenVideoAdListenerImpl implements IFullScreenVideoAdListener {
    @Override
    public void onAdLoad(String providerType, FullscreenAdView view) {

    }

    @Override
    public void onAdExposure(String providerType) {

    }

    @Override
    public void onAdClick(String providerType) {

    }

    @Override
    public void onAdClose(String providerType) {

    }

    @Override
    public void onCached(String providerType) {

    }

    @Override
    public void onVideoComplete(String providerType) {

    }

    @Override
    public void onSkippedVideo(String providerType) {

    }

    @Override
    public void onGdtOpened(String type) {

    }

    @Override
    public void onGdtCached(String type) {

    }

    @Override
    public void onGdtLeftApplication(String type) {

    }

    @Override
    public void onIdle() {

    }

    @Override
    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {

    }

    @Override
    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {

    }

    @Override
    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {

    }

    @Override
    public void onDownloadFinished(long totalBytes, String fileName, String appName) {

    }

    @Override
    public void onInstalled(String fileName, String appName) {

    }

    @Override
    public void onAdFail(List<StrategyError> strategyErrorList) {

    }
}
