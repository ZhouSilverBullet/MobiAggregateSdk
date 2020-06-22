package com.mobi.csj.wrapper;

import android.app.Activity;
import android.text.TextUtils;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.IExpressAdView;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.utils.LogUtils;
import com.mobi.csj.impl.CsjAdView;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 18:29
 * @Dec 略
 */
public class FullScreenVideoAdWrapper extends BaseAdWrapper implements TTAdNative.FullScreenVideoAdListener, TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {
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
        String postId = mAdParams.getPostId();
        if (checkPostIdEmpty(mAdProvider, postId)) {
            return;
        }

        TTAdNative adNative = createAdNative(mActivity.getApplicationContext());

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mAdParams.getPostId())
                //模板广告需要设置期望个性化模板广告的大小,单位dp,全屏视频场景，只要设置的值大于0即可
                .setExpressViewAcceptedSize(mAdParams.getExpressViewWidth(), mAdParams.getExpressViewHeight())
                .setSupportDeepLink(mAdParams.isSupportDeepLink())
                .setOrientation(mAdParams.getOrientation())//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();

        adNative.loadFullScreenVideoAd(adSlot, this);
    }


    @Override
    public void onError(int i, String s) {
        localExecFail(mAdProvider, i, s);
    }

    @Override
    public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ttFullScreenVideoAd) {

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "Csj FullScreenVideoAdWrapper load isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "Csj FullScreenVideoAdWrapper load isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        IExpressAdView expressAdView = null;
        if (mAdParams.isAutoShowAd()) {
            ttFullScreenVideoAd.setFullScreenVideoAdInteractionListener(this);
            if (ttFullScreenVideoAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                setAppDownloadListener(mListener);
                ttFullScreenVideoAd.setDownloadListener(this);
            }

            ttFullScreenVideoAd.showFullScreenVideoAd(mActivity);
        } else {
            ttFullScreenVideoAd.setFullScreenVideoAdInteractionListener(this);
            if (ttFullScreenVideoAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                setAppDownloadListener(mListener);
                ttFullScreenVideoAd.setDownloadListener(this);
            }
            expressAdView = new CsjAdView(mActivity, ttFullScreenVideoAd);
        }

        if (mListener != null) {
            mListener.onAdLoad(mProviderType, expressAdView, mAdParams.isAutoShowAd());
        }
    }

    @Override
    public void onFullScreenVideoCached() {
        if (mListener != null) {
            mListener.onCached(mProviderType);
        }
    }

    @Override
    public void onAdShow() {
        if (mListener != null) {
            mAdProvider.trackShow();
            mListener.onAdExposure(mProviderType);
        }
    }

    @Override
    public void onAdVideoBarClick() {
        if (mListener != null) {
            mAdProvider.trackClick();
            mListener.onAdClick(mProviderType);
        }
    }

    @Override
    public void onAdClose() {
        if (mListener != null) {
            mListener.onAdClose(mProviderType);
        }
    }

    @Override
    public void onVideoComplete() {
        if (mListener != null) {
            mListener.onVideoComplete(mProviderType);
        }
    }

    @Override
    public void onSkippedVideo() {
        if (mListener != null) {
            mListener.onSkippedVideo(mProviderType);
        }
    }

    @Override
    public void run() {
        createFullScreenVideoAd();
    }

    @Override
    public int getStyleType() {
        return MobiConstantValue.STYLE.FULL_SCREEN;
    }
}
