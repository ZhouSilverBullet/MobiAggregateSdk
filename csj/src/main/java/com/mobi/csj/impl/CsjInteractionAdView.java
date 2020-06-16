package com.mobi.csj.impl;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.mobi.core.feature.IExpressAdView;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/16 17:26
 * @Dec 略
 */
public class CsjInteractionAdView implements IExpressAdView {
    private final TTNativeExpressAd mTtFullScreenVideoAd;

    public CsjInteractionAdView(TTNativeExpressAd ttFullScreenVideoAd) {
        mTtFullScreenVideoAd = ttFullScreenVideoAd;
    }

    @Override
    public void render() {
        if (mTtFullScreenVideoAd != null) {
            mTtFullScreenVideoAd.render();
        }
    }
}
