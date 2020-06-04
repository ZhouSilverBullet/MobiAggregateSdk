package com.mobi.gdt.wrapper;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.listener.ISplashAdListener;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 15:02
 * @Dec 略
 */
public class SplashAdWrapper extends BaseAdWrapper implements SplashADListener {
    public static final String TAG = "SplashAdWrapper";

    BaseAdProvider mAdProvider;
    Activity mActivity;
    String mCodeId;
    ViewGroup mSplashContainer;
    ISplashAdListener mListener;

    public SplashAdWrapper(BaseAdProvider adProvider,
                           Activity activity,
                           String codeId,
                           ViewGroup splashContainer,
                           ISplashAdListener listener) {
        mAdProvider = adProvider;
        mActivity = activity;
        mCodeId = codeId;
        mSplashContainer = splashContainer;
        mListener = listener;
    }

    public void createSplashAd() {

        if (mAdProvider != null) {
            mAdProvider.callbackSplashStartRequest(mListener);
        }

        SplashAD splashAD = new SplashAD(mActivity, getAppId(), mCodeId, this);
        splashAD.fetchAndShowIn(mSplashContainer);

    }


    @Override
    public void onADDismissed() {
        Log.w(TAG, "onADDismissed");
        if (mAdProvider != null) {
            mAdProvider.callbackSplashDismissed(mListener);
        }
    }

    @Override
    public void onNoAD(AdError adError) {
        Log.w(TAG, "onNoAD");
        if (mAdProvider != null) {
            if (adError != null) {
                mAdProvider.callbackSplashFail(adError.getErrorCode(), adError.getErrorMsg(), mListener);
            } else {
                mAdProvider.callbackSplashFail(-100, "广告加载失败", mListener);
            }
        }
    }

    @Override
    public void onADPresent() {
        Log.w(TAG, "onADPresent");
    }

    @Override
    public void onADClicked() {
        Log.w(TAG, "onADClicked");
        if (mAdProvider != null) {
            mAdProvider.callbackSplashClicked(mListener);
        }

    }

    @Override
    public void onADTick(long l) {
        Log.w(TAG, "onADTick l :" + l);
    }

    @Override
    public void onADExposure() {
        Log.w(TAG, "onADExposure");
        if (mAdProvider != null) {
            mAdProvider.callbackSplashExposure(mListener);
        }
    }

    @Override
    public void onADLoaded(long l) {
        Log.w(TAG, "onADLoaded");
        if (mAdProvider != null) {
            mAdProvider.callbackSplashLoaded(mListener);
        }
    }

}
