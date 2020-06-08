package com.mobi.gdt.wrapper;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.splash.BaseSplashSkipView;
import com.mobi.core.utils.LogUtils;
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
    private final LocalAdParams mAdParams;

    BaseAdProvider mAdProvider;
    Activity mActivity;
    ViewGroup mSplashContainer;
    ISplashAdListener mListener;
    private BaseSplashSkipView mBaseSplashSkipView;
    private View mSkipView;
    //com.qq.e.comm.plugin.splash.d{665b416 V.E...... ......I. 0,0-0,0}
    private SplashAD mSplashAD;

    public SplashAdWrapper(BaseAdProvider adProvider,
                           Activity activity,
                           ViewGroup splashContainer,
                           LocalAdParams adParams,
                           ISplashAdListener listener) {
        mAdProvider = adProvider;
        mActivity = activity;
        mAdParams = adParams;
        mSplashContainer = splashContainer;
        mListener = listener;
    }

    public void setSplashSkipView(BaseSplashSkipView splashSkipView) {
        mBaseSplashSkipView = splashSkipView;
    }

    private void createSplashAd() {

        String postId = mAdParams.getPostId();
        if (TextUtils.isEmpty(postId)) {
            localExecFail(mAdProvider, -101,
                    "mobi 后台获取的 postId 不正确 或者 postId == null");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.callbackSplashStartRequest(mListener);
        }

        if (mBaseSplashSkipView != null) {
            mSkipView = mBaseSplashSkipView.createSkipView(mActivity, mSplashContainer);
        }

        mSplashAD = new SplashAD(mActivity, mSkipView, getAppId(), mAdParams.getPostId(), this, 0);
//        mSplashAD.fetchAndShowIn(mSplashContainer);
        mSplashAD.fetchAdOnly();
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
                localExecFail(mAdProvider, adError.getErrorCode(), adError.getErrorMsg());
            } else {
                localExecFail(mAdProvider, -100, "广告加载失败");
            }
        }
    }

    @Override
    public void onADPresent() {
        Log.w(TAG, "onADPresent");
        //show成功前判断一下，是否已经把任务给取消了
//        if (isCancel()) {
//            LogUtils.e(TAG, "Gdt SplashAdWrapper onAdShow isCancel");
//            return;
//        }

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
        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "Gdt SplashAdWrapper load isCancel");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        mSplashAD.showAd(mSplashContainer);

        if (mAdProvider != null) {
            mAdProvider.callbackSplashLoaded(mListener);
        }
    }

    @Override
    public void run() {
        createSplashAd();
    }
}
