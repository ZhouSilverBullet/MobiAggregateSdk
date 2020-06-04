package com.mobi.gdt.wrapper;

import android.app.Activity;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.listener.IInteractionAdListener;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.util.AdError;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 21:18
 * @Dec 略
 */
public class UnifiedInterstitialADWrapper extends BaseAdWrapper implements UnifiedInterstitialADListener {
    private String mProviderType;
    private BaseAdProvider mBaseAdProvider;
    private Activity mActivity;
    private String mCodeId;
    private IInteractionAdListener mListener;

    private UnifiedInterstitialAD iad;

    public UnifiedInterstitialADWrapper(BaseAdProvider baseAdProvider,
                                        Activity activity,
                                        String codeId,
                                        IInteractionAdListener listener) {
        mBaseAdProvider = baseAdProvider;
        mActivity = activity;
        mCodeId = codeId;
        mListener = listener;
        if (mBaseAdProvider != null) {
            mProviderType = mBaseAdProvider.getProviderType();
        }
    }

    public void createInterstitialAD() {
        iad = new UnifiedInterstitialAD(mActivity, getAppId(), mCodeId, this);
        iad.loadAD();
    }

    @Override
    public void onADReceive() {
        if (iad != null) {
            iad.showAsPopupWindow();
        }

        if (mBaseAdProvider != null) {
            mBaseAdProvider.callbackInteractionReceive(mListener);
        }
    }

    @Override
    public void onVideoCached() {

        if (iad != null) {
            iad.showAsPopupWindow();
        }

        if (mBaseAdProvider != null) {
            mBaseAdProvider.callbackInteractionCached(mListener);
        }
    }

    @Override
    public void onNoAD(AdError adError) {
        if (mBaseAdProvider != null) {
            if (adError == null) {
                mBaseAdProvider.callbackInteractionFail(-100, "onNoAD 没有数据 adError == null", mListener);
            } else {
                mBaseAdProvider.callbackInteractionFail(adError.getErrorCode(), adError.getErrorMsg(), mListener);
            }
        }
    }

    @Override
    public void onADOpened() {

        if (mBaseAdProvider != null) {
            mBaseAdProvider.callbackInteractionOpened(mListener);
        }

    }

    @Override
    public void onADExposure() {
        if (mBaseAdProvider != null) {
            mBaseAdProvider.callbackInteractionExposure(mListener);
        }
    }

    @Override
    public void onADClicked() {
        if (mBaseAdProvider != null) {
            mBaseAdProvider.callbackInteractionClick(mListener);
        }
    }

    @Override
    public void onADLeftApplication() {
        if (mListener != null) {
            mListener.onADLeftApplication(mProviderType);
        }
    }

    @Override
    public void onADClosed() {
        if (mBaseAdProvider != null) {
            mBaseAdProvider.callbackInteractionClose(mListener);
        }
    }
}
