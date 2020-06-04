package com.mobi.csj.wrapper;

import android.app.Activity;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.listener.IRewardAdListener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 10:53
 * @Dec 略
 */
public class RewardVideoAdWrapper extends BaseAdWrapper implements TTAdNative.RewardVideoAdListener, TTRewardVideoAd.RewardAdInteractionListener {
    BaseAdProvider mAdProvider;
    Activity mActivity;
    String mCodeId;
    boolean mSupportDeepLink;
    IRewardAdListener mListener;
    private TTRewardVideoAd mttRewardVideoAd;

    public RewardVideoAdWrapper(BaseAdProvider adProvider,
                                Activity activity,
                                String codeId,
                                boolean supportDeepLink,
                                IRewardAdListener listener) {
        mAdProvider = adProvider;
        mActivity = activity;
        mCodeId = codeId;
        mSupportDeepLink = supportDeepLink;
        mListener = listener;
    }

    public void createRewardVideoAd() {
        TTAdNative adNative = createAdNative(mActivity.getApplicationContext());
        AdSlot adSlot = new AdSlot.Builder()
                .setImageAcceptedSize(1080, 1920)
                .setCodeId(mCodeId)
                .setSupportDeepLink(mSupportDeepLink)
                .setRewardName("")
                .setRewardAmount(10)
                .setUserID("")
                .setMediaExtra("media-extra")
                .setOrientation(TTAdConstant.VERTICAL)
                .build();

        adNative.loadRewardVideoAd(adSlot, this);
    }

    @Override
    public void onError(int code, String message) {
//                AdStatistical.trackAD(activity, mProviderType, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
        //加载错误
//        if (mListener != null) {
//            mListener.onAdFail(mProviderType, "code：" + code + " errorMsg: " + message);
//            // listener.onAdClose(mProviderType);
//        }
        if (mAdProvider != null) {
            mAdProvider.callbackRewardFail(code, message, mListener);
        }


    }

    @Override
    public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
//        if (mListener != null) {
//            mListener.onAdLoad(mProviderType);
//            // listener.onAdClose(mProviderType);
//        }
        if (mAdProvider != null) {
            mAdProvider.callbackRewardLoad(mListener);
        }

        //加载成功
        mttRewardVideoAd = ttRewardVideoAd;
        mttRewardVideoAd.setRewardAdInteractionListener(this);
        mttRewardVideoAd.showRewardVideoAd(mActivity, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
//                if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {
//                    showTTVideo();
//                } else {
//                    recordRenderSuccess(mProviderType);
//                    if (firstCome) {
//                        showTTVideo();
//                        firstCome = false;
//                    }
//                }
        if (mttRewardVideoAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            //本地接口扔到Base里面去回调
            setAppDownloadListener(mListener);

            mttRewardVideoAd.setDownloadListener(this);
        }
    }

    @Override
    public void onRewardVideoCached() {
        //缓存在了本地
//        if (mListener != null) {
//            mListener.onCached(mProviderType);
//        }

        if (mAdProvider != null) {
            mAdProvider.callbackRewardCached(mListener);
        }
    }


    @Override
    public void onAdShow() {
        //显示广告
//        if (mListener != null) {
//            mListener.onAdShow(mProviderType);
//        }

        if (mAdProvider != null) {
            mAdProvider.callbackRewardExpose(mListener);
        }
    }

    @Override
    public void onAdVideoBarClick() {
//        if (mListener != null) {
//            mListener.onAdClick(mProviderType);
//        }
//     AdStatistical.trackAD(activity, mProviderType, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
        if (mAdProvider != null) {
            mAdProvider.callbackRewardClick(mListener);
        }
    }

    @Override
    public void onAdClose() {
        //广告关闭
//        if (mListener != null) {
//            mListener.onAdClose(mProviderType);
//        }

        if (mAdProvider != null) {
            mAdProvider.callbackRewardClose(mListener);
        }
    }

    @Override
    public void onVideoComplete() {
        //播放完成
//        if (mListener != null) {
//            mListener.onVideoComplete(mProviderType);
//        }
        if (mAdProvider != null) {
            mAdProvider.callbackRewardVideoComplete(mListener);
        }
    }

    @Override
    public void onVideoError() {
        //播放错误
//        if (mListener != null) {
//            mListener.onAdFail(mProviderType, "播放错误");
//        }

        if (mAdProvider != null) {
            mAdProvider.callbackRewardFail(-100, "播放错误 onVideoError", mListener);
        }
    }

    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
    @Override
    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
        //视频播放完成，奖励回调验证
//        if (mListener != null) {
//            mListener.onRewardVerify(mProviderType, rewardVerify, rewardAmount, rewardName);
//        }

        if (mAdProvider != null) {
            mAdProvider.callbackRewardVerify(rewardVerify, rewardAmount, rewardName, mListener);
        }
    }

    @Override
    public void onSkippedVideo() {
        //跳过广告
//        if (mListener != null) {
//            mListener.onSkippedVideo(mProviderType);
//        }
        if (mAdProvider != null) {
            mAdProvider.callbackRewardSkippedVideo(mListener);
        }
    }
}
