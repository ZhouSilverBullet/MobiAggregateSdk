package com.mobi.unityads.wrapper;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.listener.ISplashAdListener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 18:44
 * @Dec ks没有这个实现
 */
public class SplashAdWrapper extends BaseAdWrapper {

    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private final ViewGroup mSplashContainer;
    private String mProviderType;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    ISplashAdListener mListener;
    private Fragment mFragment;

    public SplashAdWrapper(BaseAdProvider adProvider,
                           Activity activity,
                           ViewGroup splashContainer,
                           LocalAdParams adParams,
                           ISplashAdListener listener) {
        mAdProvider = adProvider;
        mActivity = activity;
        mSplashContainer = splashContainer;
        mAdParams = adParams;
        mListener = listener;

        if (mAdProvider != null) {
            mProviderType = mAdProvider.getProviderType();
        }
        mMobiCodeId = mAdParams.getMobiCodeId();
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
        return MobiConstantValue.STYLE.SPLASH;
    }
}
