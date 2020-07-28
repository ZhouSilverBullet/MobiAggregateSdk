package com.mobi.ks.wrapper;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.KsLoadManager;
import com.kwad.sdk.api.KsScene;
import com.kwad.sdk.api.KsSplashScreenAd;
import com.kwad.sdk.splashscreen.KsSplashScreenFragment;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.SplashAdView;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.utils.LogUtils;
import com.mobi.ks.R;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 18:44
 * @Dec ks没有这个实现
 */
public class SplashAdWrapper extends BaseAdWrapper implements KsLoadManager.SplashScreenAdListener, KsSplashScreenAd.SplashScreenAdInteractionListener, SplashAdView {

    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private final ViewGroup mSplashContainer;
    private String mProviderType;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    ISplashAdListener mListener;
    private KsSplashScreenAd mKsSplashScreenAd;
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

        String postId = mAdParams.getPostId();
        if (checkPostIdEmpty(mAdProvider, postId)) {
            return;
        }

        KsScene scene = new KsScene.Builder(getPostId(postId)).build();
        KsAdSDK.getLoadManager().loadSplashScreenAd(scene, this);
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
    public void onError(int code, String errorMsg) {
        localExecFail(mAdProvider, code, errorMsg);
    }

    @Override
    public void onSplashScreenAdLoad(@Nullable KsSplashScreenAd ksSplashScreenAd) {
        if (ksSplashScreenAd == null) {
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_LOAD_EMPTY_ERROR, "请求成功，没有返回的广告");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad();
        }

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "Ks SplashAdWrapper load isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "Ks SplashAdWrapper load isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }

        if (mSplashContainer.getChildCount() >= 1) {
            LogUtils.e(TAG, "Ks SplashAdWrapper mSplashContainer.getChildCount() >= 1 isCancel");
            localExecFail(mAdProvider, -105, "Ks splashContainer.getChildCount() >= 1 isCancel");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        mKsSplashScreenAd = ksSplashScreenAd;
        mFragment = mKsSplashScreenAd.getFragment(this);

        if (mAdProvider != null) {
            mAdProvider.callbackSplashLoaded(mListener, this, mAdParams.isAutoShowAd());
        }
    }

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
    public void onAdShowError(int code, String errorMsg) {
        localExecFail(mAdProvider, code, errorMsg);
    }

    @Override
    public void onAdShowEnd() {
        LogUtils.e(TAG, "onAdTimeOver");
        if (mAdProvider != null) {
            mAdProvider.trackEventClose();
            mAdProvider.callbackSplashDismissed(mListener);
        }
    }

    @Override
    public void onAdShowStart() {
        LogUtils.e(TAG, "onAdShow");

        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow();
            mAdProvider.callbackSplashExposure(mListener);
        }
    }

    @Override
    public void onSkippedAd() {
        LogUtils.e(TAG, "onAdSkip");

        if (mAdProvider != null) {
//            mAdProvider.trackEventClose(getStyleType());
            mAdProvider.trackSkip();
            mAdProvider.callbackSplashDismissed(mListener);
        }
    }

    @Override
    public void show() {
        if (mActivity instanceof FragmentActivity && mFragment != null) {
            ((FragmentActivity) mActivity).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(mSplashContainer.getId(), mFragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onDestroy() {
        if (mFragment != null) {
            mFragment = null;
        }
        if (mKsSplashScreenAd != null) {
            mKsSplashScreenAd = null;
        }
    }
}
