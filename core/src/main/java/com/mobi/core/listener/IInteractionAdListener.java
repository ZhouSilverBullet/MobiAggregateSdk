package com.mobi.core.listener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 11:17
 * @Dec 略
 */
public interface IInteractionAdListener extends ITTAppDownloadListener, IAdFailListener {
    void onAdFail(String type, int code, String errorMsg);
    void onADReceive(String type);
    void onADOpened(String type);
    void onADExposure(String type);
    void onAdLoad(String type);
    void onAdShow(String type);
    void onAdClick(String type);
    void onAdClose(String providerType);
    void onVideoComplete(String providerType);
    void onSkippedVideo(String providerType);
    void onRewardVerify(String providerType, boolean rewardVerify, int rewardAmount, String rewardName);
    void onCached(String type);

    default void onADLeftApplication(String type) {

    }




}
