package com.mobi.gdt.wrapper;

import android.app.Activity;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.IAdView;
import com.mobi.core.feature.InteractionAdView;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.utils.LogUtils;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.util.AdError;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 21:18
 * @Dec 略
 */
public class UnifiedInterstitialADWrapper extends BaseAdWrapper implements InteractionAdView, UnifiedInterstitialADListener {
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    private BaseAdProvider mAdProvider;
    private Activity mActivity;
    private IInteractionAdListener mListener;

    private UnifiedInterstitialAD iad;

    public UnifiedInterstitialADWrapper(BaseAdProvider baseAdProvider,
                                        Activity activity,
                                        LocalAdParams adParams,
                                        IInteractionAdListener listener) {
        mAdProvider = baseAdProvider;
        mActivity = activity;
        mAdParams = adParams;
        mListener = listener;
        mMobiCodeId = mAdParams.getMobiCodeId();
        if (mAdProvider != null) {
            mProviderType = mAdProvider.getProviderType();
        }
    }

    private void createInterstitialAD() {
        String postId = mAdParams.getPostId();
        if (checkPostIdEmpty(mAdProvider, postId)) {
            return;
        }

        iad = new UnifiedInterstitialAD(mActivity, mAdParams.getPostId(), this);

        VideoOption.Builder builder = new VideoOption.Builder();
        VideoOption option = builder.setAutoPlayMuted(true)
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS).build();
        iad.setVideoOption(option);
        iad.setMaxVideoDuration(mAdParams.getMaxVideoDuration());

        /**
         * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前调用setVideoPlayPolicy，有助于提高视频广告的eCPM值 <br/>
         * 如果广告位仅支持图文广告，则无需调用
         */

        /**
         * 设置本次拉取的视频广告，从用户角度看到的视频播放策略<p/>
         *
         * "用户角度"特指用户看到的情况，并非SDK是否自动播放，与自动播放策略AutoPlayPolicy的取值并非一一对应 <br/>
         *
         * 如自动播放策略为AutoPlayPolicy.WIFI，但此时用户网络为4G环境，在用户看来就是手工播放的
         */
        iad.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO);

        iad.loadAD();
    }

    @Override
    public void onADReceive() {

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad();
        }

        if (isCancel()) {
            LogUtils.e(TAG, "Gdt UnifiedInterstitialAD isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "Gdt UnifiedInterstitialAD isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

//        IExpressAdView expressAdView = null;
//        if (mAdParams.isAutoShowAd()) {
//            if (iad != null) {
//                iad.showAsPopupWindow();
//            }
//        } else {
//            expressAdView = new GdtInterstitialAdView(iad);
//        }

        if (mAdProvider != null) {
            mAdProvider.callbackInteractionLoad(mListener, this, mAdParams.isAutoShowAd());
        }
    }

    @Override
    public void onVideoCached() {
        if (mAdProvider != null) {
            mAdProvider.callbackInteractionCached(mListener);
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

        if (mAdProvider != null) {
            mAdProvider.callbackInteractionOpened(mListener);
            mAdProvider.trackGdtShow();
        }

    }

    @Override
    public void onADExposure() {
        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow();
            mAdProvider.callbackInteractionExposure(mListener);
        }
    }

    @Override
    public void onADClicked() {
        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick();
            mAdProvider.callbackInteractionClick(mListener);
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
        if (mAdProvider != null) {
            mAdProvider.callbackInteractionClose(mListener);
            mAdProvider.trackEventClose();
        }
    }

    @Override
    public void run() {
        if (mAdProvider != null) {
            mAdProvider.trackEventStart();
        }
        createInterstitialAD();
    }

    @Override
    public int getStyleType() {
        return MobiConstantValue.STYLE.INTERACTION_EXPRESS;
    }

    @Override
    public void show() {
        if (iad != null) {
            iad.showAsPopupWindow();
        }

        if (mAdProvider != null) {
            mAdProvider.trackStartShow();
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        if (iad != null) {
            iad.destroy();
            iad = null;
        }
    }
}
