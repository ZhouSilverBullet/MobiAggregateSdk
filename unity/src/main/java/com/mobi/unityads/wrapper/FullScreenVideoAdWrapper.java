package com.mobi.unityads.wrapper;

import android.app.Activity;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.FullscreenAdView;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

import static com.mobi.unityads.UnityInit.SDK_CODE_10009;
import static com.mobi.unityads.UnityInit.SDK_MESSAGE_10009;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 18:29
 * @Dec 略
 */
public class FullScreenVideoAdWrapper extends BaseAdWrapper implements FullscreenAdView, IUnityAdsListener {

    public static final String INTERSTITIAL_PLACEMENT_ID = "defaultVideoAndPictureZone";

    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    IFullScreenVideoAdListener mListener;

    public FullScreenVideoAdWrapper(BaseAdProvider adProvider,
                                    Activity activity,
                                    LocalAdParams adParams,
                                    IFullScreenVideoAdListener listener) {
        mAdProvider = adProvider;
        mActivity = activity;
        mAdParams = adParams;
        mListener = listener;
        mMobiCodeId = mAdParams.getMobiCodeId();
        if (mAdProvider != null) {
            mProviderType = mAdProvider.getProviderType();
        }
    }

    private void createFullScreenVideoAd() {
//        String postId = mAdParams.getPostId();
//        if (checkPostIdEmpty(mAdProvider, postId)) {
//            return;
//        }

        if (UnityAds.isReady()) {

            setExecSuccess(true);
            localExecSuccess(mAdProvider);

            ready();
        } else {
            localExecFail(mAdProvider, SDK_CODE_10009, SDK_MESSAGE_10009);
        }
    }

    @Override
    public void run() {
        if (mAdProvider != null) {
            mAdProvider.trackEventStart();
        }
        createFullScreenVideoAd();
    }

    @Override
    public int getStyleType() {
        return MobiConstantValue.STYLE.FULL_SCREEN;
    }

    @Override
    public void show() {

        UnityAds.addListener(this);
        UnityAds.show(mActivity, INTERSTITIAL_PLACEMENT_ID);

        if (mAdProvider != null) {
            mAdProvider.trackStartShow();
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        UnityAds.removeListener(this);
    }

    @Override
    public void onUnityAdsReady(String placementId) {
        ready();
    }

    @Override
    public void onUnityAdsStart(String placementId) {
        if (mListener != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow();
            mListener.onAdExposure(mProviderType);
        }
    }

    @Override
    public void onUnityAdsFinish(String placementId, UnityAds.FinishState result) {
        UnityAds.removeListener(this);

        if (mListener != null) {
            mAdProvider.trackEventClose();
            mListener.onAdClose(mProviderType);
        }
    }

    @Override
    public void onUnityAdsError(UnityAds.UnityAdsError error, String message) {
        UnityAds.removeListener(this);

        if (error != null) {
            localExecFail(mAdProvider, error.ordinal(), message);
        } else {
            localExecFail(mAdProvider, -1, message);
        }
    }

    private void ready() {
        if (mListener != null) {
            mListener.onAdLoad(mProviderType, this);
        }
    }
}
