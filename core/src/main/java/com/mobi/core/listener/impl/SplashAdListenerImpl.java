package com.mobi.core.listener.impl;

import android.support.annotation.NonNull;

import com.mobi.core.feature.SplashAdView;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.strategy.StrategyError;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/7/29 19:51
 * @Dec 略
 */
public class SplashAdListenerImpl implements ISplashAdListener {
    @Override
    public void onAdStartRequest(@NonNull String providerType) {

    }

    @Override
    public void onAdLoad(String providerType, SplashAdView view) {

    }

    @Override
    public void onAdExposure(String type) {

    }

    @Override
    public void onAdClick(String type) {

    }

    @Override
    public void onAdClose(String type) {

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
