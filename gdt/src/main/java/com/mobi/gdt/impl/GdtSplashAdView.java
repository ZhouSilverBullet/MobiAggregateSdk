package com.mobi.gdt.impl;

import android.view.ViewGroup;

import com.mobi.core.feature.IExpressAdView;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.splash.SplashAD;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/16 14:45
 * @Dec 略
 */
public class GdtSplashAdView implements IExpressAdView {
    private SplashAD mView;
    private ViewGroup mSplashContainer;

    public GdtSplashAdView(SplashAD views, ViewGroup splashContainer) {
        mView = views;
        mSplashContainer = splashContainer;
    }

    @Override
    public void render() {
        if (mView != null) {
            mView.showAd(mSplashContainer);
        }
    }
}
