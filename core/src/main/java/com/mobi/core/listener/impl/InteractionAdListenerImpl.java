package com.mobi.core.listener.impl;

import com.mobi.core.feature.InteractionAdView;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.strategy.StrategyError;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/7/29 19:50
 * @Dec 略
 */
public class InteractionAdListenerImpl implements IInteractionAdListener {

    @Override
    public void onAdLoad(String providerType, InteractionAdView view) {

    }

    @Override
    public void onAdExposure(String type) {

    }

    @Override
    public void onAdClick(String type) {

    }

    @Override
    public void onAdClose(String providerType) {

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
