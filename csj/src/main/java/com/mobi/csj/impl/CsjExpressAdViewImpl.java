package com.mobi.csj.impl;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.mobi.core.feature.IExpressAdView;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/16 14:45
 * @Dec 略
 */
public class CsjExpressAdViewImpl implements IExpressAdView {
    private List<? extends TTNativeExpressAd> mViews;

    public CsjExpressAdViewImpl(List<? extends TTNativeExpressAd> views) {
        mViews = views;
    }

    @Override
    public void render() {
        if (mViews == null) {
            return;
        }
        for (TTNativeExpressAd view : mViews) {
            ((TTNativeExpressAd) view).render();
        }
//        mViews instanceof
    }
}
