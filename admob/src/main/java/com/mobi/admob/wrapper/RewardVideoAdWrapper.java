package com.mobi.admob.wrapper;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.RewardAdView;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.utils.LogUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 10:53
 * @Dec 略
 */
public class RewardVideoAdWrapper extends BaseAdWrapper implements RewardAdView {
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    IRewardAdListener mListener;
    private RewardedAd rewardedAd;

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
        String postId = mAdParams.getPostId();
        if (checkPostIdEmpty(mAdProvider, postId)) {
            return;
        }

        rewardedAd = new RewardedAd(mActivity, postId);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                rewardedAdLoaded();
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                if (loadAdError != null) {
                    localExecFail(mAdProvider, loadAdError.getCode(), loadAdError.getMessage());
                }
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    private void rewardedAdLoaded() {

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad();
        }

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "AdMob RewardVideoAdWrapper load isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "AdMob RewardVideoAdWrapper load isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        if (mAdProvider != null) {
            mAdProvider.callbackRewardLoad(mListener, this, mAdParams.isAutoShowAd());
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
        if (rewardedAd != null && rewardedAd.isLoaded()) {
            rewardedAd.show(mActivity, new RewardedAdCallback() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    String type = rewardItem.getType();
                    Log.e(TAG, "onUserEarnedReward " + type);
                    //视频播放完成，奖励回调验证
                    if (mAdProvider != null) {
                        mAdProvider.callbackRewardVerify(true, rewardItem.getAmount(), rewardItem.getType(), mListener);
                        mAdProvider.trackRewardVerify();
                    }
                }

                @Override
                public void onRewardedAdClosed() {
                    //广告关闭
                    if (mAdProvider != null) {
                        mAdProvider.trackEventClose();
                        mAdProvider.callbackRewardClose(mListener);
                    }
                }

                @Override
                public void onRewardedAdOpened() {
                    if (mAdProvider != null) {
                        mAdProvider.trackShow();
                        mAdProvider.trackEventShow();
                        mAdProvider.callbackRewardExpose(mListener);
                    }
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    localExecFail(mAdProvider, adError.getCode(), adError.getMessage());
                }
            });
        }

        if (mAdProvider != null) {
            mAdProvider.trackStartShow();
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        rewardedAd = null;
    }

//    @Override
//    public void onAdClicked() {
//        if (mAdProvider != null) {
//            mAdProvider.trackClick();
//            mAdProvider.trackEventClick();
//            mAdProvider.callbackRewardClick(mListener);
//        }
//    }


}
