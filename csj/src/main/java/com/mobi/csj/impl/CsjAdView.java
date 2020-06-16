package com.mobi.csj.impl;

import android.app.Activity;

import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.mobi.core.feature.IExpressAdView;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/16 17:26
 * @Dec 略
 */
public class CsjAdView implements IExpressAdView {
    private final TTFullScreenVideoAd mTtFullScreenVideoAd;
    private Activity mActivity;

    public CsjAdView(Activity activity, TTFullScreenVideoAd ttFullScreenVideoAd) {
        mActivity = activity;
        mTtFullScreenVideoAd = ttFullScreenVideoAd;
    }

    @Override
    public void render() {
        if (mTtFullScreenVideoAd != null) {
            mTtFullScreenVideoAd.showFullScreenVideoAd(mActivity);
        }
    }
}
