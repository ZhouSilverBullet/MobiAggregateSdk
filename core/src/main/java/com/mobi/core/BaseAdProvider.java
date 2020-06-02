package com.mobi.core;

import com.mobi.core.listener.ISplashAdListener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/2 21:11
 * @Dec 略
 */
public abstract class BaseAdProvider implements IAdProvider {
    protected String mProviderType;

    public BaseAdProvider(String providerType) {
        mProviderType = providerType;
    }

    protected void callbackSplashStartRequest(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdStartRequest(mProviderType);
        }
    }

    protected void callbackSplashFail(String error, ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdFail(mProviderType, error);
        }
    }

    protected void callbackSplashClicked(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdClicked(mProviderType);
        }
    }

    protected void callbackSplashExposure(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdExposure(mProviderType);
        }
    }

    protected void callbackSplashDismissed(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdDismissed(mProviderType);
        }
    }

    protected void callbackSplashLoaded(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdLoaded(mProviderType);
        }
    }


}
