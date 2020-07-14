package com.mobi.core.listener;

import com.mobi.core.feature.IAdView;

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
    void onAdLoad(String providerType, IAdView view);
}
