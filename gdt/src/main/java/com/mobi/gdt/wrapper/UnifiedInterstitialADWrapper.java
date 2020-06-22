package com.mobi.gdt.wrapper;

import android.app.Activity;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.IExpressAdView;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.utils.LogUtils;
import com.mobi.gdt.impl.GdtInterstitialAdView;
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
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    private BaseAdProvider mAdProvider;
    private Activity mActivity;
    private IInteractionAdListener mListener;

    private UnifiedInterstitialAD iad;

    public UnifiedInterstitialADWrapper(BaseAdProvider baseAdProvider,
                                        Activity activity,
                                        LocalAdParams adParams,
                                        IInteractionAdListener listener) {
        mAdProvider = baseAdProvider;
        mActivity = activity;
        mAdParams = adParams;
        mListener = listener;
        mMobiCodeId = mAdParams.getMobiCodeId();
        if (mAdProvider != null) {
            mProviderType = mAdProvider.getProviderType();
        }
    }

    private void createInterstitialAD() {
        String postId = mAdParams.getPostId();
        if (checkPostIdEmpty(mAdProvider, postId)) {
            return;
        }

        iad = new UnifiedInterstitialAD(mActivity, mAdParams.getPostId(), this);
        iad.loadAD();
    }

    @Override
    public void onADReceive() {

        if (isCancel()) {
            LogUtils.e(TAG, "Gdt UnifiedInterstitialAD isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "Gdt UnifiedInterstitialAD isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        IExpressAdView expressAdView = null;
        if (mAdParams.isAutoShowAd()) {
            if (iad != null) {
                iad.showAsPopupWindow();
            }
        } else {
            expressAdView = new GdtInterstitialAdView(iad);
        }

        if (mAdProvider != null) {
            mAdProvider.callbackInteractionLoad(mListener, expressAdView, mAdParams.isAutoShowAd());
        }
    }

    @Override
    public void onVideoCached() {

//        if (iad != null) {
//            iad.showAsPopupWindow();
//        }

        if (mAdProvider != null) {
            mAdProvider.callbackInteractionCached(mListener);
        }
    }

    @Override
    public void onNoAD(AdError adError) {
        if (mAdProvider != null) {
            if (adError == null) {
                localExecFail(mAdProvider, -100, "onNoAD 没有数据 adError == null");
            } else {
                localExecFail(mAdProvider, adError.getErrorCode(), adError.getErrorMsg());
            }
        }
    }

    @Override
    public void onADOpened() {

        if (mAdProvider != null) {
            mAdProvider.callbackInteractionOpened(mListener);
        }

    }

    @Override
    public void onADExposure() {
        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.callbackInteractionExposure(mListener);
        }
    }

    @Override
    public void onADClicked() {
        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.callbackInteractionClick(mListener);
        }
    }

    @Override
    public void onADLeftApplication() {
        if (mListener != null) {
            mListener.onGdtLeftApplication(mProviderType);
        }
    }

    @Override
    public void onADClosed() {
        if (mAdProvider != null) {
            mAdProvider.callbackInteractionClose(mListener);
        }
    }

    @Override
    public void run() {
        createInterstitialAD();
    }

    @Override
    public int getStyleType() {
        return MobiConstantValue.STYLE.INTERACTION_EXPRESS;
    }
}
