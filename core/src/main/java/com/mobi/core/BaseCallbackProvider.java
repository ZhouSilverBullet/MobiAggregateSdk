package com.mobi.core;

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

    public BaseCallbackProvider(String providerType) {
        mProviderType = providerType;
    }

    public final void callbackSplashStartRequest(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdStartRequest(mProviderType);
        }
    }

    public final void callbackSplashFail(int code, String error, ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdFail(mProviderType, code, error);
        }
    }

    public final void callbackSplashClicked(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdClicked(mProviderType);
        }
    }

    public final void callbackSplashExposure(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdExposure(mProviderType);
        }
    }

    public final void callbackSplashDismissed(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdDismissed(mProviderType);
        }
    }

    public final void callbackSplashLoaded(ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdLoaded(mProviderType);
        }
    }

    /////// 激励视频广告回调  start //////
    public final void callbackRewardFail(int code, String errorMsg, IRewardAdListener listener) {
        if (listener != null) {
            listener.onAdFail(mProviderType, code, errorMsg);
        }
    }

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

    public final void callbackInteractionFail(int code, String errorMsg, IInteractionAdListener listener) {
        if (listener != null) {
            listener.onAdFail(mProviderType, code, errorMsg);
        }
    }

    public final void callbackInteractionReceive(IInteractionAdListener listener) {
        if (listener != null) {
            listener.onADReceive(mProviderType);
        }
    }

    public final void callbackInteractionOpened(IInteractionAdListener listener) {
        if (listener != null) {
            listener.onADOpened(mProviderType);
        }
    }

    public final void callbackInteractionExposure(IInteractionAdListener listener) {
        if (listener != null) {
            listener.onADExposure(mProviderType);
        }
    }

    public final void callbackInteractionShow(IInteractionAdListener listener) {
        if (listener != null) {
            listener.onAdShow(mProviderType);
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
            listener.onCached(mProviderType);
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

    public final void callbackExpressLoadFailed(int code, String errorMsg, IExpressListener listener) {
        if (listener != null) {
            listener.onLoadFailed(mProviderType, code, errorMsg);
        }
    }

    public final void callbackExpressClick(IExpressListener listener) {
        if (listener != null) {
            listener.onAdClick(mProviderType);
        }
    }

    public final void callbackExpressDismissed(IExpressListener listener) {
        if (listener != null) {
            listener.onAdDismissed(mProviderType);
        }
    }

    public final void callbackExpressShow(IExpressListener listener) {
        if (listener != null) {
            listener.onAdShow(mProviderType);
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
}
