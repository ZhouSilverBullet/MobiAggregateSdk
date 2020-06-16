package com.mobi.csj.impl;

import android.app.Activity;

import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.mobi.core.feature.IExpressAdView;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/16 17:26
 * @Dec 略
 */
public class CsjRewardAdView implements IExpressAdView {
    private final TTRewardVideoAd mTTRewardVideoAd;
    private Activity mActivity;

    public CsjRewardAdView(Activity activity, TTRewardVideoAd ttRewardVideoAd) {
        mActivity = activity;
        mTTRewardVideoAd = ttRewardVideoAd;
    }

    @Override
    public void render() {
        if (mTTRewardVideoAd != null) {
            mTTRewardVideoAd.showRewardVideoAd(mActivity);
        }
    }
}
