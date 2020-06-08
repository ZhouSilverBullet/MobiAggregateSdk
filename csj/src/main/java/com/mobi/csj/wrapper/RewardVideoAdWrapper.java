package com.mobi.csj.wrapper;

import android.app.Activity;
import android.text.TextUtils;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.mobi.core.AdParams;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.utils.LogUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 10:53
 * @Dec 略
 */
public class RewardVideoAdWrapper extends BaseAdWrapper implements TTAdNative.RewardVideoAdListener, TTRewardVideoAd.RewardAdInteractionListener {
    private final LocalAdParams mAdParams;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    IRewardAdListener mListener;
    private TTRewardVideoAd mttRewardVideoAd;

    public RewardVideoAdWrapper(BaseAdProvider adProvider,
                                Activity activity,
                                LocalAdParams adParams,
                                IRewardAdListener listener) {
        mAdProvider = adProvider;
        mActivity = activity;
        mAdParams = adParams;
        mListener = listener;
    }

    private void createRewardVideoAd() {
        String postId = mAdParams.getPostId();
        if (TextUtils.isEmpty(postId)) {
            localExecFail(mAdProvider, -101,
                    "mobi 后台获取的 postId 不正确 或者 postId == null");
            return;
        }

        TTAdNative adNative = createAdNative(mActivity.getApplicationContext());
        AdSlot adSlot = new AdSlot.Builder()
                .setImageAcceptedSize(mAdParams.getImageWidth(), mAdParams.getImageHeight())
                .setCodeId(mAdParams.getPostId())
                .setSupportDeepLink(mAdParams.isSupportDeepLink())
                .setRewardName(mAdParams.getRewardName())
                .setRewardAmount(mAdParams.getRewardAmount())
                .setUserID(mAdParams.getUserID())
                .setMediaExtra(mAdParams.getMediaExtra())
                .setOrientation(mAdParams.getOrientation())
                .build();

        adNative.loadRewardVideoAd(adSlot, this);
    }

    @Override
    public void onError(int code, String message) {
//      AdStatistical.trackAD(activity, mProviderType, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);

        localExecFail(mAdProvider, code, message);

    }

    @Override
    public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "Csj RewardVideoAdWrapper load isCancel");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.callbackRewardLoad(mListener);
        }

        //加载成功
        mttRewardVideoAd = ttRewardVideoAd;
        mttRewardVideoAd.setRewardAdInteractionListener(this);

        //显示广告
        //show成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "Csj RewardVideoAdWrapper onAdShow isCancel");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        mttRewardVideoAd.showRewardVideoAd(mActivity, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");

        if (mttRewardVideoAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            //本地接口扔到Base里面去回调
            setAppDownloadListener(mListener);

            mttRewardVideoAd.setDownloadListener(this);
        }
    }

    @Override
    public void onRewardVideoCached() {
        //缓存在了本地
        if (mAdProvider != null) {
            mAdProvider.callbackRewardCached(mListener);
        }
    }


    @Override
    public void onAdShow() {

        if (mAdProvider != null) {
            mAdProvider.callbackRewardExpose(mListener);
        }
    }

    @Override
    public void onAdVideoBarClick() {
//     AdStatistical.trackAD(activity, mProviderType, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
        if (mAdProvider != null) {
            mAdProvider.callbackRewardClick(mListener);
        }
    }

    @Override
    public void onAdClose() {
        //广告关闭

        if (mAdProvider != null) {
            mAdProvider.callbackRewardClose(mListener);
        }
    }

    @Override
    public void onVideoComplete() {
        //播放完成
        if (mAdProvider != null) {
            mAdProvider.callbackRewardVideoComplete(mListener);
        }
    }

    @Override
    public void onVideoError() {
        //播放错误
        localExecFail(mAdProvider,-100, "播放错误 onVideoError" );
    }

    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
    @Override
    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
        //视频播放完成，奖励回调验证
        if (mAdProvider != null) {
            mAdProvider.callbackRewardVerify(rewardVerify, rewardAmount, rewardName, mListener);
        }
    }

    @Override
    public void onSkippedVideo() {
        //跳过广告
        if (mAdProvider != null) {
            mAdProvider.callbackRewardSkippedVideo(mListener);
        }
    }

    @Override
    public void run() {
        createRewardVideoAd();
    }
}
