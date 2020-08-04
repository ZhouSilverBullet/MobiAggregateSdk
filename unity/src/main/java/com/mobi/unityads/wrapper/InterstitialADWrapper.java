package com.mobi.unityads.wrapper;

import android.app.Activity;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.listener.IInteractionAdListener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 18:44
 * @Dec ks没有这个实现
 */
public class InterstitialADWrapper extends BaseAdWrapper {

    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    IInteractionAdListener mListener;

    public InterstitialADWrapper(BaseAdProvider baseAdProvider,
                                       Activity activity,
                                       LocalAdParams adParams,
                                       IInteractionAdListener listener) {

        mAdProvider = baseAdProvider;
        mActivity = activity;
        mAdParams = adParams;
        mListener = listener;

        mMobiCodeId = mAdParams.getMobiCodeId();

        if (baseAdProvider != null) {
            mProviderType = baseAdProvider.getProviderType();
        }
    }

    private void createSplashAd() {

        if (isCancel()) {
            return;
        }

        localExecFail(mAdProvider, -102, "unity 没有这个实现");
        //todo 没有这个实现
    }

    @Override
    public void run() {
        if (mAdProvider != null) {
            mAdProvider.trackEventStart();
        }
        createSplashAd();
    }

    @Override
    public int getStyleType() {
        return MobiConstantValue.STYLE.INTERACTION_EXPRESS;
    }

}
