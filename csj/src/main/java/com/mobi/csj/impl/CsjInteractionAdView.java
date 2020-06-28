package com.mobi.csj.impl;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.mobi.core.feature.IExpressAdView;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/16 17:26
 * @Dec 略
 */
public class CsjInteractionAdView implements IExpressAdView {
    private final List<TTNativeExpressAd> mTtFullScreenVideoAds;

    public CsjInteractionAdView(List<TTNativeExpressAd> list) {
        mTtFullScreenVideoAds = list;
    }

    @Override
    public void render() {
        if (mTtFullScreenVideoAds == null) {
            return;
        }
        for (TTNativeExpressAd ttFullScreenVideoAd : mTtFullScreenVideoAds) {
            ttFullScreenVideoAd.render();
        }
    }
}
