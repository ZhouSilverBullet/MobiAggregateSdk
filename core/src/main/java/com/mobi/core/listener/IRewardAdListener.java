package com.mobi.core.listener;

import com.mobi.core.feature.IAdView;
import com.mobi.core.feature.RewardAdView;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 11:17
 * @Dec 略
 */
public interface IRewardAdListener extends ITTAppDownloadListener, IAdFailListener {

    void onAdLoad(String providerType, RewardAdView view);

    void onAdExpose(String type);

    void onAdClick(String type);

    void onAdClose(String providerType);

    /**
     * gdt 的 show 和 Expose 不一样，所以这里把后面
     * csj 的 show 变成 Expose {@link IRewardAdListener#onAdExpose(String)}
     * 回调，这个show用来gdt使用
     *
     * @param type
     */
    default void onAdShow(String type) {

    }
    void onVideoComplete(String providerType);
    void onSkippedVideo(String providerType);
    void onRewardVerify(String providerType, boolean rewardVerify, int rewardAmount, String rewardName);
    void onCached(String type);
}
