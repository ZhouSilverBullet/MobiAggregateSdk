package com.mobi.ks.wrapper;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.KsLoadManager;
import com.kwad.sdk.api.KsRewardVideoAd;
import com.kwad.sdk.api.KsScene;
import com.kwad.sdk.api.KsVideoPlayConfig;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.ConstantValue;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.RewardAdView;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.utils.LogUtils;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 10:53
 * @Dec 略
 */
public class RewardVideoAdWrapper extends BaseAdWrapper implements RewardAdView, KsLoadManager.RewardVideoAdListener, KsRewardVideoAd.RewardAdInteractionListener {
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    IRewardAdListener mListener;
    private KsRewardVideoAd mRewardVideoAd;

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

        KsScene scene = new KsScene.Builder(getPostId(postId)).adNum(mAdParams.getAdCount()).build();
        KsAdSDK.getLoadManager().loadRewardVideoAd(scene, this);
    }

    @Override
    public void onError(int code, String message) {
//      AdStatistical.trackAD(activity, mProviderType, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);

        localExecFail(mAdProvider, code, message);

    }

    @Override
    public void onRewardVideoAdLoad(@Nullable List<KsRewardVideoAd> list) {

        if (list == null || list.size() == 0) {
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_LOAD_EMPTY_ERROR, "type KsRewardVideoAd  == null || type KsRewardVideoAd list.size() == 0");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad();
        }

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "Ks RewardVideoAdWrapper load isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "Ks RewardVideoAdWrapper load isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        //加载成功
        mRewardVideoAd = list.get(0);
        mRewardVideoAd.setRewardAdInteractionListener(this);

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
//
//    @Override
//    public void onAdShow() {
//
//        if (mAdProvider != null) {
//            mAdProvider.trackShow();
//            mAdProvider.trackEventShow();
//            mAdProvider.callbackRewardExpose(mListener);
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
        if (mRewardVideoAd != null) {
            KsVideoPlayConfig videoPlayConfig = new KsVideoPlayConfig.Builder()
                    // true: 横屏播放
                    .showLandscape(mAdParams.getOrientation() == ConstantValue.HORIZONTAL)
                    .build();
            mRewardVideoAd.showRewardVideoAd(mActivity, videoPlayConfig);
        }

        if (mAdProvider != null) {
            mAdProvider.trackStartShow();
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        mRewardVideoAd = null;
    }

    @Override
    public void onAdClicked() {
        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick();
            mAdProvider.callbackRewardClick(mListener);
        }
    }

    @Override
    public void onPageDismiss() {
        //广告关闭
        if (mAdProvider != null) {
            mAdProvider.trackEventClose();
            mAdProvider.callbackRewardClose(mListener);
        }
    }

    @Override
    public void onVideoPlayError(int code, int extra) {
        //播放错误
        localExecFail(mAdProvider, code, String.valueOf(extra));
    }

    @Override
    public void onVideoPlayEnd() {
        //播放完成
        if (mAdProvider != null) {
            mAdProvider.callbackRewardVideoComplete(mListener);
            mAdProvider.trackComplete();
        }
    }

    @Override
    public void onVideoPlayStart() {
        // todo ks 用start来表示show逻辑发送给后台
        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow();
            mAdProvider.callbackRewardExpose(mListener);
        }
    }

    @Override
    public void onRewardVerify() {
        //视频播放完成，奖励回调验证
        if (mAdProvider != null) {
            mAdProvider.callbackRewardVerify(true, 0, "", mListener);
            mAdProvider.trackRewardVerify();
        }
    }

}
