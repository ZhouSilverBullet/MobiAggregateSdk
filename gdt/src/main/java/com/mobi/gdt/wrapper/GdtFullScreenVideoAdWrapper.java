package com.mobi.gdt.wrapper;

import android.app.Activity;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.FullscreenAdView;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.utils.LogUtils;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.ads.interstitial2.UnifiedInterstitialMediaListener;
import com.qq.e.comm.util.AdError;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 18:44
 * @Dec gdt没有这个实现
 */
public class GdtFullScreenVideoAdWrapper extends BaseAdWrapper implements FullscreenAdView, UnifiedInterstitialADListener, UnifiedInterstitialMediaListener {

    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    IFullScreenVideoAdListener mListener;
    private UnifiedInterstitialAD iad;

    public GdtFullScreenVideoAdWrapper(BaseAdProvider adProvider,
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

        iad = new UnifiedInterstitialAD(mActivity, postId, this);
        VideoOption.Builder builder = new VideoOption.Builder();
        VideoOption option = builder.setAutoPlayMuted(true).build();
        iad.setVideoOption(option);
        iad.setMaxVideoDuration(mAdParams.getMaxVideoDuration());
        iad.loadFullScreenAD();
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
    public void onADReceive() {

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad();
        }

        if (isCancel()) {
            LogUtils.e(TAG, "Gdt FUll UnifiedInterstitialAD isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "Gdt FUll UnifiedInterstitialAD isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        if (iad != null) {
            iad.setMediaListener(this);
        }

        if (mListener != null) {
            mListener.onAdLoad(mProviderType, this);
        }
    }

    @Override
    public void onVideoCached() {
        if (mListener != null) {
            mListener.onCached(mProviderType);
        }
        if (mAdProvider != null) {
            mAdProvider.trackCache();
        }
    }

    @Override
    public void onNoAD(AdError adError) {
        if (mAdProvider != null) {
            if (adError == null) {
                localExecFail(mAdProvider, -100, "onNoAD 没有数据 adError == null");
            } else {
                localExecFail(mAdProvider, adError.getErrorCode(), adError.getErrorMsg());
            }
        }
    }

    @Override
    public void onADOpened() {
        if (mListener != null) {
            mListener.onGdtOpened(mProviderType);
        }
        if (mAdProvider != null) {
            mAdProvider.trackGdtShow();
        }

    }

    @Override
    public void onADExposure() {

        if (mListener != null) {
            mListener.onAdExposure(mProviderType);
        }
        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow();
        }
    }

    @Override
    public void onADClicked() {
        if (mListener != null) {
            mListener.onAdClick(mProviderType);
        }
        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick();
        }
    }

    @Override
    public void onADLeftApplication() {
        if (mListener != null) {
            mListener.onGdtLeftApplication(mProviderType);
        }
        if (mAdProvider != null) {
            mAdProvider.trackGdtLeftApplication();
        }
    }

    @Override
    public void onADClosed() {
        if (mListener != null) {
            mListener.onAdClose(mProviderType);
        }
        if (mAdProvider != null) {
            mAdProvider.trackEventClose();
        }
    }

    @Override
    public void show() {
        if (iad != null) {
            iad.showFullScreenAD(mActivity);
        }
    }

    @Override
    public void onDestroy() {
        if (iad != null) {
            iad.destroy();
            iad = null;
        }
    }

    @Override
    public void onVideoInit() {

    }

    @Override
    public void onVideoLoading() {

    }

    @Override
    public void onVideoReady(long l) {

    }

    @Override
    public void onVideoStart() {

    }

    @Override
    public void onVideoPause() {

    }

    @Override
    public void onVideoComplete() {
        if (mListener != null) {
            mListener.onVideoComplete(mProviderType);
        }
        if (mAdProvider != null) {
            mAdProvider.trackEvent(MobiConstantValue.EVENT.COMPLETE);
        }
    }

    @Override
    public void onVideoError(AdError adError) {
        if (mAdProvider != null) {
            if (adError == null) {
                localExecFail(mAdProvider, -100, "onNoAD 视频播放失败 adError == null");
            } else {
                localExecFail(mAdProvider, adError.getErrorCode(), adError.getErrorMsg());
            }
        }
    }

    @Override
    public void onVideoPageOpen() {

    }

    @Override
    public void onVideoPageClose() {

    }
}
