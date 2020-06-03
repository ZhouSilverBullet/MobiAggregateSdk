package com.mobi.core;

import com.mobi.core.listener.IExpressListener;
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

    public final void callbackSplashFail(String error, ISplashAdListener listener) {
        if (listener != null) {
            listener.onAdFail(mProviderType, error);
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
}
