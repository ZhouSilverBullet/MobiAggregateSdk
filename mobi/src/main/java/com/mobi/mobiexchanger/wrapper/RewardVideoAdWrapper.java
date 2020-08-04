package com.mobi.mobiexchanger.wrapper;

import android.app.Activity;

import com.mobi.adsdk.MobiSdk;
import com.mobi.adsdk.ads.MobiAdSlot;
import com.mobi.adsdk.ads.rewardvideo.MobiRewardVideoAdListener;
import com.mobi.adsdk.ads.rewardvideo.MobiRewardVideoListener;
import com.mobi.adsdk.net.ads.rewardVideo.RewardVideoAd;
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
public class RewardVideoAdWrapper extends BaseAdWrapper implements RewardAdView, MobiRewardVideoAdListener, MobiRewardVideoListener {
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    IRewardAdListener mListener;
    private RewardVideoAd mTtRewardVideoAd;

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

        MobiAdSlot slot = new MobiAdSlot.Builder()
                .setCodeId(postId)
                .build();

        MobiSdk.loadRewardAd(mActivity, slot, this);
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
    public void onRewardVideoAdLoad(RewardVideoAd ttRewardVideoAd) {

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad();
        }

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "Mobi RewardVideoAdWrapper load isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "Mobi RewardVideoAdWrapper load isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        mTtRewardVideoAd = ttRewardVideoAd;
        //加载成功
        ttRewardVideoAd.setRewardVideoListener(this);
//
//        if (ttRewardVideoAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
//            //本地接口扔到Base里面去回调
//            setAppDownloadListener(mListener);
//
//            ttRewardVideoAd.setDownloadListener(this);
//        }

//        IExpressAdView expressAdView = null;
//
//        if (mAdParams.isAutoShowAd()) {
//            ttRewardVideoAd.showRewardVideoAd(mActivity);
//        } else {
//            expressAdView = new CsjRewardAdView(mActivity, ttRewardVideoAd);
//        }


        if (mAdProvider != null) {
            mAdProvider.callbackRewardLoad(mListener, this, mAdParams.isAutoShowAd());
        }
    }

//    @Override
//    public void onRewardVideoCached() {
//        //缓存在了本地
//        if (mAdProvider != null) {
//            mAdProvider.callbackRewardCached(mListener);
//            mAdProvider.trackCache();
//        }
//    }


    @Override
    public void onAdShow() {

        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow();
            mAdProvider.callbackRewardExpose(mListener);
        }
    }

    @Override
    public void onAdVideoBarClick() {

        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick();
            mAdProvider.callbackRewardClick(mListener);
        }
    }

    @Override
    public void onAdClose() {
        //广告关闭
        if (mAdProvider != null) {
            mAdProvider.trackEventClose();
            mAdProvider.callbackRewardClose(mListener);
        }
    }

    @Override
    public void onVideoComplete() {
        //播放完成
        if (mAdProvider != null) {
            mAdProvider.callbackRewardVideoComplete(mListener);
            mAdProvider.trackComplete();
        }
    }

    @Override
    public void onVideoError() {
        //播放错误
        localExecFail(mAdProvider, 0, "播放错误 onVideoError");
    }

//    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
//    @Override
//    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
//        //视频播放完成，奖励回调验证
//        if (mAdProvider != null) {
//            mAdProvider.callbackRewardVerify(rewardVerify, rewardAmount, rewardName, mListener);
//            mAdProvider.trackRewardVerify();
//        }
//    }
//
//    @Override
//    public void onSkippedVideo() {
//        //跳过广告
//        if (mAdProvider != null) {
//            mAdProvider.callbackRewardSkippedVideo(mListener);
//            mAdProvider.trackSkip();
//        }
//
//    }

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
        if (mTtRewardVideoAd != null) {
            mTtRewardVideoAd.showRewardVideoAd(mActivity);
        }

        if (mAdProvider != null) {
            mAdProvider.trackStartShow();
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        mTtRewardVideoAd = null;
    }
}
