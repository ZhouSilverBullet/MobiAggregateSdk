package com.mobi.mobiexchanger.wrapper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.adsdk.MobiSdk;
import com.mobi.adsdk.ads.MobiAdSlot;
import com.mobi.adsdk.ads.splash.MobiSplashAdListener;
import com.mobi.adsdk.ads.splash.MobiSplashListener;
import com.mobi.adsdk.net.ads.splash.MobiSplashAd;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.SplashAdView;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.splash.BaseSplashSkipView;
import com.mobi.core.utils.LogUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 14:07
 * @Dec 略
 */
public class SplashAdWrapper extends BaseAdWrapper implements SplashAdView, MobiSplashAdListener, MobiSplashListener {
    public static final String TAG = "SplashAdWrapper";
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;

    private String mProviderType;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    ViewGroup mSplashContainer;
    ISplashAdListener mListener;
    private BaseSplashSkipView mBaseSplashSkipView;
    private MobiSplashAd mTtSplashAd;

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

    public void setSplashSkipView(BaseSplashSkipView splashSkipView) {
        mBaseSplashSkipView = splashSkipView;
    }

    private void createSplashAd() {
        String postId = mAdParams.getPostId();
        if (checkPostIdEmpty(mAdProvider, postId)) {
            return;
        }

        MobiAdSlot slot = new MobiAdSlot.Builder()
                .setCodeId(postId)
                .build();

        MobiSdk.loadMobiSplashAd(mActivity, slot, this);

        if (mAdProvider != null) {
            mAdProvider.callbackSplashStartRequest(mListener);
        }

    }

    @Override
    public void onError(int code, String errorMsg) {

        localExecFail(mAdProvider, code, errorMsg);

    }

    @Override
    public void onSplashAdLoad(MobiSplashAd ttSplashAd) {
        if (ttSplashAd == null) {
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_LOAD_EMPTY_ERROR, "请求成功，没有返回的广告");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad();
        }

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "mobi SplashAdWrapper load isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "mobi SplashAdWrapper load isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }

        if (mSplashContainer.getChildCount() >= 1) {
            LogUtils.e(TAG, "mobi SplashAdWrapper mSplashContainer.getChildCount() >= 1 isCancel");
            localExecFail(mAdProvider, -105, "Csj splashContainer.getChildCount() >= 1 isCancel");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        mTtSplashAd = ttSplashAd;
        mTtSplashAd.setMobiSplashListener(this);

        if (mAdProvider != null) {
            mAdProvider.callbackSplashLoaded(mListener, this, mAdParams.isAutoShowAd());
        }
    }

//    @Override
//    public void onTimeout() {
//        //todo
//        localExecFail(mAdProvider, 0, "请求超时");
//    }


//    public void showAdView(TTSplashAd ttSplashAd) {
//        if (ttSplashAd == null) {
//            LogUtils.e(TAG, "ttSplashAd == null");
//            return;
//        }
//        //获取SplashView
//        View view = ttSplashAd.getSplashView();
//        if (view != null && mSplashContainer != null && !mActivity.isFinishing()) {
//
//            mSplashContainer.removeAllViews();
//            //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
//            mSplashContainer.addView(view);
//            //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
//            if (mBaseSplashSkipView != null) {
//                handleSplashSkipView(mSplashContainer);
//            }
//        } else {
////          goToMainActivity();
//            //activity已经销毁了，或者传进来的container为null
//            LogUtils.e(TAG, "goToMainActivity");
//            if (mAdProvider != null) {
//                mAdProvider.callbackSplashDismissed(mListener);
//            }
//
//        }
//    }

//    public void handleSplashSkipView(ViewGroup splashContainer) {
//
//        CsjSplashSkipViewControl csjSplashSkipViewControl = new CsjSplashSkipViewControl(mBaseSplashSkipView);
//        //里面做跳过的逻辑
//        csjSplashSkipViewControl.setSkipCallback(this::onAdSkip);
//        csjSplashSkipViewControl.handleSplashSkipView(splashContainer);
//
//    }


//    @Override
//    public void onAdClicked(View view, int type) {
//
//    }
//
//    @Override
//    public void onAdShow(View view, int type) {
//
//    }

    @Override
    public void onAdClicked() {
        LogUtils.e(TAG, "onAdClicked");

        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick();
            mAdProvider.callbackSplashClicked(mListener);
        }
    }

    @Override
    public void onAdShow() {
        LogUtils.e(TAG, "onAdShow");

        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow();
            mAdProvider.callbackSplashExposure(mListener);
        }
    }

    @Override
    public void onAdSkip() {
        LogUtils.e(TAG, "onAdSkip");

        if (mAdProvider != null) {
//            mAdProvider.trackEventClose(getStyleType());
            mAdProvider.callbackSplashDismissed(mListener);
            mAdProvider.trackSkip();
        }

    }

    @Override
    public void onAdTimeOver() {
        LogUtils.e(TAG, "onAdTimeOver");
        if (mAdProvider != null) {
            mAdProvider.callbackSplashDismissed(mListener);
        }

    }

    @Override
    public void onError(String s, String s1) {
        int code = 0;
        try {
            code = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        localExecFail(mAdProvider, code, s1);
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

    @Override
    public void show() {
        mSplashContainer.removeAllViews();
        if (mTtSplashAd != null) {
//            showAdView(mTtSplashAd);
            View splashView = mTtSplashAd.getSplashView();
            if (splashView != null) {
                mSplashContainer.addView(splashView);
            }
        }

        if (mAdProvider != null) {
            mAdProvider.trackStartShow();
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        mTtSplashAd = null;
    }
}
