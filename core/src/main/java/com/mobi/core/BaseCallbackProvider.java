package com.mobi.core;

import com.mobi.core.analysis.AdAnalysis;
import com.mobi.core.feature.IExpressAdView;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.listener.ISplashAdListener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/2 21:11
 * @Dec 略
 */
public abstract class BaseCallbackProvider implements IAdProvider {
    protected String mProviderType;
    private String mMobiCodeId;

    public BaseCallbackProvider(String providerType) {
        mProviderType = providerType;
    }

    public final void callbackSplashStartRequest(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdStartRequest(mProviderType);
        }
    }

    /**
     * @param listener
     */
    public final void callbackSplashClicked(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdClick(mProviderType);
        }
    }

    public final void callbackSplashExposure(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdExposure(mProviderType);
        }
    }

    public final void callbackSplashDismissed(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdClose(mProviderType);
        }
    }

    public final void callbackSplashLoaded(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdLoad(mProviderType);
        }
    }

    /////// 激励视频广告回调  start //////

    public final void callbackRewardLoad(IRewardAdListener listener) {
        if (listener != null) {
            listener.onAdLoad(mProviderType);
        }
    }

    public final void callbackRewardGdtShow(IRewardAdListener listener) {
        if (listener != null) {
            listener.onAdGdtShow(mProviderType);
        }
    }

    public final void callbackRewardExpose(IRewardAdListener listener) {
        if (listener != null) {
            listener.onAdExpose(mProviderType);
        }
    }

    public final void callbackRewardClick(IRewardAdListener listener) {
        if (listener != null) {
            listener.onAdClick(mProviderType);
        }
    }

    public final void callbackRewardClose(IRewardAdListener listener) {
        if (listener != null) {
            listener.onAdClose(mProviderType);
        }
    }

    public final void callbackRewardVideoComplete(IRewardAdListener listener) {
        if (listener != null) {
            listener.onVideoComplete(mProviderType);
        }
    }

    public final void callbackRewardSkippedVideo(IRewardAdListener listener) {
        if (listener != null) {
            listener.onSkippedVideo(mProviderType);
        }
    }

    public final void callbackRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName, IRewardAdListener listener) {
        if (listener != null) {
            listener.onRewardVerify(mProviderType, rewardVerify, rewardAmount, rewardName);
        }
    }

    public final void callbackRewardCached(IRewardAdListener listener) {
        if (listener != null) {
            listener.onCached(mProviderType);
        }
    }
    /////// 激励视频广告回调  end //////


    /////// 插屏广告回调  start //////


    public final void callbackInteractionLoad(IInteractionAdListener listener, IExpressAdView view, boolean isAutoShow) {
        if (listener != null) {
            listener.onAdLoad(mProviderType, view, isAutoShow);
        }
    }

    public final void callbackInteractionOpened(IInteractionAdListener listener) {
        if (listener != null) {
            listener.onGdtOpened(mProviderType);
        }
    }

    public final void callbackInteractionExposure(IInteractionAdListener listener) {
        if (listener != null) {
            listener.onAdExposure(mProviderType);
        }
    }

    public final void callbackInteractionClick(IInteractionAdListener listener) {
        if (listener != null) {
            listener.onAdClick(mProviderType);
        }
    }

    public final void callbackInteractionClose(IInteractionAdListener listener) {
        if (listener != null) {
            listener.onAdClose(mProviderType);
        }
    }

    public final void callbackInteractionCached(IInteractionAdListener listener) {
        if (listener != null) {
            listener.onGdtCached(mProviderType);
        }
    }

    /////// 插屏广告回调  end //////


    /////// 信息流的回调  start //////
    //Express callback
    public final void callbackExpressRenderSuccess(IExpressListener listener) {
        if (listener != null) {
            listener.onAdRenderSuccess(mProviderType);
        }
    }

    public final void callbackExpressLoad(IExpressListener listener, IExpressAdView view, boolean isAutoShow) {
        if (listener != null) {
            listener.onAdLoad(mProviderType, view, isAutoShow);
        }
    }

    public final void callbackExpressClick(IExpressListener listener) {
        if (listener != null) {
            listener.onAdClick(mProviderType);
        }
    }

    public final void callbackExpressDismissed(IExpressListener listener) {
        if (listener != null) {
            listener.onAdClose(mProviderType);
        }
    }

    public final void callbackExpressShow(IExpressListener listener) {
        if (listener != null) {
            listener.onAdExposure(mProviderType);
        }
    }

    public final void callbackExpressLeftApplication(IExpressListener listener) {
        if (listener != null) {
            listener.onADLeftApplication(mProviderType);
        }
    }

    public final void callbackExpressOpenOverlay(IExpressListener listener) {
        if (listener != null) {
            listener.onADOpenOverlay(mProviderType);
        }
    }

    public final void callbackExpressCloseOverlay(IExpressListener listener) {
        if (listener != null) {
            listener.onADCloseOverlay(mProviderType);
        }
    }

    ////////////信息流回调 end //////////


    public String getProviderType() {
        return mProviderType;
    }

    public String getMobiCodeId() {
        return mMobiCodeId;
    }

    public void setMobiCodeId(String mobiCodeId) {
        mMobiCodeId = mobiCodeId;
    }

    /**
     * 上报点击统计
     */
    public void trackClick() {
        //统计点击
        AdAnalysis.trackAD(mProviderType, getMobiCodeId(), AdAnalysis.STATUS_CODE_FALSE, AdAnalysis.STATUS_CODE_TRUE);
    }
    /**
     * 上报展示点击
     */
    public void trackShow() {
        //统计点击
        AdAnalysis.trackAD(mProviderType, getMobiCodeId(), AdAnalysis.STATUS_CODE_TRUE, AdAnalysis.STATUS_CODE_FALSE);
    }

    /**
     * 上报展示点击
     */
    public void trackFail() {
        //统计点击
        AdAnalysis.trackAD(mProviderType, getMobiCodeId(), AdAnalysis.STATUS_CODE_FALSE, AdAnalysis.STATUS_CODE_FALSE);
    }
}
