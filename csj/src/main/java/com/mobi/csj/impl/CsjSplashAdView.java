package com.mobi.csj.impl;

import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.mobi.core.feature.IExpressAdView;
import com.mobi.csj.wrapper.SplashAdWrapper;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/16 17:26
 * @Dec 略
 */
public class CsjSplashAdView implements IExpressAdView {
    private final SplashAdWrapper mSplashAdWrapper;
    private final TTSplashAd mTtSplashAd;

    public CsjSplashAdView(SplashAdWrapper splashAdWrapper, TTSplashAd ttSplashAd) {
        mSplashAdWrapper = splashAdWrapper;
        mTtSplashAd = ttSplashAd;
    }

    @Override
    public void render() {
        if (mSplashAdWrapper != null) {
            mSplashAdWrapper.showAdView(mTtSplashAd);
        }
    }
}
