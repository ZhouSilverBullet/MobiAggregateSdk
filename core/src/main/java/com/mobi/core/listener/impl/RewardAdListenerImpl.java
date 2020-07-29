package com.mobi.core.listener.impl;

import com.mobi.core.feature.RewardAdView;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.strategy.StrategyError;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/7/29 19:42
 * @Dec 略
 */
public class RewardAdListenerImpl implements IRewardAdListener {
    @Override
    public void onAdLoad(String providerType, RewardAdView view) {

    }

    @Override
    public void onAdExpose(String type) {

    }

    @Override
    public void onAdClick(String type) {

    }

    @Override
    public void onAdClose(String providerType) {

    }

    @Override
    public void onVideoComplete(String providerType) {

    }

    @Override
    public void onSkippedVideo(String providerType) {

    }

    @Override
    public void onRewardVerify(String providerType, boolean rewardVerify, int rewardAmount, String rewardName) {

    }

    @Override
    public void onCached(String type) {

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
