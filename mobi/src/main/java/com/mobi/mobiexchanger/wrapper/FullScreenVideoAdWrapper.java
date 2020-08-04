package com.mobi.mobiexchanger.wrapper;

import android.app.Activity;

import com.mobi.adsdk.MobiSdk;
import com.mobi.adsdk.ads.MobiAdSlot;
import com.mobi.adsdk.ads.fullrewardvideo.MobiFullScreenVideoAdListenner;
import com.mobi.adsdk.ads.fullrewardvideo.MobiFullScreenVideoListenner;
import com.mobi.adsdk.net.ads.fullscreenvideo.FullScreenVideoAd;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.FullscreenAdView;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.utils.LogUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 18:29
 * @Dec 略
 */
public class FullScreenVideoAdWrapper extends BaseAdWrapper implements FullscreenAdView, MobiFullScreenVideoAdListenner, MobiFullScreenVideoListenner {
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    IFullScreenVideoAdListener mListener;
    private FullScreenVideoAd mTtFullScreenVideoAd;

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
        String postId = mAdParams.getPostId();
        if (checkPostIdEmpty(mAdProvider, postId)) {
            return;
        }

        MobiAdSlot slot = new MobiAdSlot.Builder()
                .setCodeId(postId)
                .build();

        MobiSdk.loadFullScreenVideoAd(mActivity, slot, this);
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
    public void onFullScreenVideoAdLoad(FullScreenVideoAd ttFullScreenVideoAd) {

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad();
        }

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "Mobi FullScreenVideoAdWrapper load isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "Mobi FullScreenVideoAdWrapper load isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        mTtFullScreenVideoAd = ttFullScreenVideoAd;

        mTtFullScreenVideoAd.setFullScreenVideoListenner(this);

        if (mListener != null) {
            mListener.onAdLoad(mProviderType, this);
        }
    }

//    @Override
//    public void onFullScreenVideoCached() {
//        if (mListener != null) {
//            mListener.onCached(mProviderType);
//        }
//
//        if (mAdProvider != null) {
//            mAdProvider.trackCache();
//        }
//    }

    @Override
    public void onAdShow() {
        if (mListener != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow();
            mListener.onAdExposure(mProviderType);
        }
    }

    @Override
    public void onAdVideoBarClick() {
        if (mListener != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick();
            mListener.onAdClick(mProviderType);
        }
    }

    @Override
    public void onAdClose() {
        if (mListener != null) {
            mAdProvider.trackEventClose();
            mListener.onAdClose(mProviderType);
        }
    }

    @Override
    public void onVideoComplete() {
        if (mListener != null) {
            mListener.onVideoComplete(mProviderType);
        }

        if (mAdProvider != null) {
            mAdProvider.trackComplete();
        }
    }

    @Override
    public void onVideoError() {
        localExecFail(mAdProvider, 0, "视频渲染失败");
    }

    @Override
    public void onSkippedVideo() {
        if (mListener != null) {
            mListener.onSkippedVideo(mProviderType);
        }

        if (mAdProvider != null) {
            mAdProvider.trackSkip();
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
        if (mTtFullScreenVideoAd != null) {
            mTtFullScreenVideoAd.showFullScreenVideoAd(mActivity);
        }

        if (mAdProvider != null) {
            mAdProvider.trackStartShow();
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        mTtFullScreenVideoAd = null;
    }

}
