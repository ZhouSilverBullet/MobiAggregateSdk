package com.mobi.unityads.wrapper;

import android.app.Activity;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.RewardAdView;
import com.mobi.core.listener.IRewardAdListener;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

import static com.mobi.unityads.UnityInit.SDK_CODE_10009;
import static com.mobi.unityads.UnityInit.SDK_MESSAGE_10009;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 10:53
 * @Dec 略
 */
public class RewardVideoAdWrapper extends BaseAdWrapper implements RewardAdView, IUnityAdsListener {

    public static final String REWARD_PLACEMENT_ID = "incentivizedZone";

    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    IRewardAdListener mListener;

    public RewardVideoAdWrapper(BaseAdProvider adProvider,
                                Activity activity,
                                LocalAdParams adParams,
                                IRewardAdListener listener) {
        mAdProvider = adProvider;
        mActivity = activity;
        mAdParams = adParams;
        mListener = listener;
        mMobiCodeId = mAdParams.getMobiCodeId();
    }

    private void createRewardVideoAd() {
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
        createRewardVideoAd();
    }

    @Override
    public int getStyleType() {
        return MobiConstantValue.STYLE.REWARD;
    }

    @Override
    public void show() {

        UnityAds.addListener(this);
        UnityAds.show(mActivity, REWARD_PLACEMENT_ID);

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
        if (UnityAds.isReady(placementId)) {
            ready();
        }
    }

    @Override
    public void onUnityAdsStart(String placementId) {
        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow();
            mAdProvider.callbackRewardExpose(mListener);
        }
    }

    @Override
    public void onUnityAdsFinish(String placementId, UnityAds.FinishState result) {
        UnityAds.removeListener(this);

        if (mAdProvider != null) {
            mAdProvider.trackEventClose();
            mAdProvider.callbackRewardClose(mListener);
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
        if (mAdProvider != null) {
            mAdProvider.callbackRewardLoad(mListener, this, mAdParams.isAutoShowAd());
        }
    }
}
