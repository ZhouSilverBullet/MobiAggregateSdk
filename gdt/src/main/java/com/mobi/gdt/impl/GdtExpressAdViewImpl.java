package com.mobi.gdt.impl;

import android.view.View;

import com.mobi.core.feature.IExpressAdView;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/16 14:45
 * @Dec 略
 */
public class GdtExpressAdViewImpl implements IExpressAdView {
    private List<? extends View> mViews;

    public GdtExpressAdViewImpl(List<? extends View> views) {
        mViews = views;
    }

    @Override
    public void render() {
        if (mViews == null) {
            return;
        }
        for (View view : mViews) {
            if (view instanceof NativeExpressADView) {
                ((NativeExpressADView) view).render();
            }
        }
//        mViews instanceof
    }
}
