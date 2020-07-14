package com.mobi.ks.wrapper;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.kwad.sdk.KsAdSDK;
import com.kwad.sdk.export.i.IAdRequestManager;
import com.kwad.sdk.export.i.KsFullScreenVideoAd;
import com.kwad.sdk.protocol.model.AdScene;
import com.kwad.sdk.video.VideoPlayConfig;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.ConstantValue;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.FullscreenAdView;
import com.mobi.core.feature.IAdView;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.utils.LogUtils;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 18:29
 * @Dec 略
 */
public class FullScreenVideoAdWrapper extends BaseAdWrapper implements FullscreenAdView, IAdRequestManager.FullScreenVideoAdListener, KsFullScreenVideoAd.FullScreenVideoAdInteractionListener {
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    IFullScreenVideoAdListener mListener;
    private KsFullScreenVideoAd mFullScreenVideoAd;

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

        AdScene scene = new AdScene(getPostId(postId));

        KsAdSDK.getAdManager().loadFullScreenVideoAd(scene, this);
    }

    @Override
    public void onError(int i, String s) {
        localExecFail(mAdProvider, i, s);
    }

    @Override
    public void onFullScreenVideoAdLoad(@Nullable List<KsFullScreenVideoAd> list) {

        if (list == null || list.size() == 0) {
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_LOAD_EMPTY_ERROR, "type KsFullScreenVideoAd  == null || type KsFullScreenVideoAd list.size() == 0");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad();
        }

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "Ks FullScreenVideoAdWrapper load isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "Ks FullScreenVideoAdWrapper load isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        mFullScreenVideoAd = list.get(0);
        mFullScreenVideoAd.setFullScreenVideoAdInteractionListener(this);

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
//
//    @Override
//    public void onAdShow() {
//        if (mListener != null) {
//            mAdProvider.trackShow();
//            mAdProvider.trackEventShow();
//            mListener.onAdExposure(mProviderType);
//        }
//    }


    @Override
    public void onVideoPlayStart() {

    }

    @Override
    public void onAdClicked() {
        if (mListener != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick();
            mListener.onAdClick(mProviderType);
        }
    }

    @Override
    public void onPageDismiss() {
        if (mListener != null) {
            mAdProvider.trackEventClose();
            mListener.onAdClose(mProviderType);
        }
    }

    @Override
    public void onVideoPlayError(int code, int extra) {
        localExecFail(mAdProvider, code, String.valueOf(extra));
    }

    @Override
    public void onVideoPlayEnd() {
        if (mListener != null) {
            mListener.onVideoComplete(mProviderType);
        }

        if (mAdProvider != null) {
            mAdProvider.trackComplete();
        }
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
        if (mFullScreenVideoAd != null) {
            VideoPlayConfig videoPlayConfig = new VideoPlayConfig.Builder()
                    // true: 横屏播放
                    .showLandscape(mAdParams.getOrientation() == ConstantValue.HORIZONTAL)
                    .build();
            mFullScreenVideoAd.showFullScreenVideoAd(mActivity, videoPlayConfig);
        }

        if (mAdProvider != null) {
            mAdProvider.trackStartShow();
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        mFullScreenVideoAd = null;
    }

}
