package com.mobi.gdt.impl;

import com.mobi.core.feature.IExpressAdView;
import com.qq.e.ads.rewardvideo.RewardVideoAD;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/16 14:45
 * @Dec 略
 */
public class GdtRewardAdView implements IExpressAdView {
    private  RewardVideoAD rewardVideoAD;

    public GdtRewardAdView(RewardVideoAD rewardVideoAD) {
        this.rewardVideoAD = rewardVideoAD;
    }

    @Override
    public void render() {
        if (rewardVideoAD != null) {
            rewardVideoAD.showAD();
        }
    }
}
