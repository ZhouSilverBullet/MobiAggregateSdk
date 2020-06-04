package com.mobi.gdt.wrapper;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.splash.BaseSplashSkipView;
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
    private BaseSplashSkipView mBaseSplashSkipView;
    private View mSkipView;

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

    public void setSplashSkipView(BaseSplashSkipView splashSkipView) {
        mBaseSplashSkipView = splashSkipView;
    }

    public void createSplashAd() {

        if (mAdProvider != null) {
            mAdProvider.callbackSplashStartRequest(mListener);
        }

        if (mBaseSplashSkipView != null) {
            mSkipView = mBaseSplashSkipView.createSkipView(mActivity, mSplashContainer);
        }

        SplashAD splashAD = new SplashAD(mActivity, mSkipView, getAppId(), mCodeId, this, 0);
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

        if (mBaseSplashSkipView != null) {
            mSplashContainer.addView(mSkipView,
                    mBaseSplashSkipView.getLayoutParams());
        }
    }

    @Override
    public void onADClicked() {
        Log.w(TAG, "onADClicked");
        if (mAdProvider != null) {
            mAdProvider.callbackSplashClicked(mListener);
        }

    }

    @Override
    public void onADTick(long millisUntilFinished) {
        Log.w(TAG, "onADTick millisUntilFinished :" + millisUntilFinished);
        int second = (int) (millisUntilFinished / 1000f);
        if (mBaseSplashSkipView != null) {
            mBaseSplashSkipView.onTime(second);
        }
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
