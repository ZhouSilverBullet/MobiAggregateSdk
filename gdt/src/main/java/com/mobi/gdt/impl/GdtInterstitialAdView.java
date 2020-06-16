package com.mobi.gdt.impl;

import android.view.View;

import com.mobi.core.feature.IExpressAdView;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/16 14:45
 * @Dec 略
 */
public class GdtInterstitialAdView implements IExpressAdView {
    private UnifiedInterstitialAD mView;

    public GdtInterstitialAdView(UnifiedInterstitialAD views) {
        mView = views;
    }

    @Override
    public void render() {
        if (mView != null) {
            mView.showAsPopupWindow();
        }
    }
}
