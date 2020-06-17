package com.mobi.core.listener;

import android.support.annotation.Nullable;

import com.mobi.core.feature.IExpressAdView;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/17 14:04
 * @Dec 略
 */
public interface IAdLoadListener {
    /**
     * @param providerType
     */
    void onAdLoad(String providerType,
                  @Nullable IExpressAdView view,
                  boolean isAutoShow);
}
