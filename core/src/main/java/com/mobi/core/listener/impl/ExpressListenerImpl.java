package com.mobi.core.listener.impl;

import com.mobi.core.feature.ExpressAdView;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.strategy.StrategyError;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/7/29 19:40
 * @Dec 略
 */
public class ExpressListenerImpl implements IExpressListener {
    @Override
    public void onAdLoad(String providerType, ExpressAdView view) {

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
    public void onAdRenderSuccess(String type) {

    }

    @Override
    public void onADLeftApplication(String type) {

    }

    @Override
    public void onADOpenOverlay(String type) {

    }

    @Override
    public void onADCloseOverlay(String type) {

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
