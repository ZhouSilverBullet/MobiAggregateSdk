package com.mobi.core;

import com.mobi.core.analysis.AdPushParams;
import com.mobi.core.analysis.event.PushEventTrack;
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
    public static final String TAG = "BaseCallbackProvider";

    protected String mProviderType;
    private AdPushParams mPushParams;

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

    public final void callbackSplashLoaded(ISplashAdListener listener, IExpressAdView view, boolean isAutoShow) {
        if (listener != null) {
            listener.onAdLoad(mProviderType, view, isAutoShow);
        }
    }

    /////// 激励视频广告回调  start //////

    public final void callbackRewardLoad(IRewardAdListener listener, IExpressAdView view, boolean isAutoShow) {
        if (listener != null) {
            listener.onAdLoad(mProviderType, view, isAutoShow);
        }
    }

    public final void callbackRewardGdtShow(IRewardAdListener listener) {
        if (listener != null) {
            listener.onAdShow(mProviderType);
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


    @Override
    public void setPushParams(AdPushParams pushParams) {
        mPushParams = pushParams;
    }

    public String getProviderType() {
        return mProviderType;
    }

    public String getMobiCodeId() {
        return mPushParams.getMobiCodeId();
    }

    public String getMd5() {
        return mPushParams.getMd5();
    }

    public int getSortType() {
        return mPushParams.getSortType();
    }

    public int getStyleType() {
        return mPushParams.getStyleType();
    }

    public boolean isPushOtherEvent() {
        return mPushParams.isPushOtherEvent();
    }

    /**
     * 上报点击统计
     */
    public void trackClick() {
        //统计点击
//        AdAnalysis.trackAD(mProviderType, getMobiCodeId(), AdAnalysis.STATUS_CODE_FALSE, AdAnalysis.STATUS_CODE_TRUE);
    }

    /**
     * 上报展示点击
     */
    public void trackShow() {
        //统计点击
//        AdAnalysis.trackAD(mProviderType, getMobiCodeId(), AdAnalysis.STATUS_CODE_TRUE, AdAnalysis.STATUS_CODE_FALSE);
    }

    /**
     * 上报展示点击
     */
    public void trackFail() {
        //统计点击
//        AdAnalysis.trackAD(mProviderType, getMobiCodeId(), AdAnalysis.STATUS_CODE_FALSE, AdAnalysis.STATUS_CODE_FALSE);
    }

    /**
     * 事件上报
     *
     * @param event 显示 ...
     */
    public void trackEvent(int event) {
//        if (!isPushMessage()) {
//            Log.e(TAG, " 消息拦截 不再上报 ");
//            return;
//        }

        PushEventTrack.trackAD(event,
                getStyleType(),
                getMobiCodeId(),
                getSortType(),
                mProviderType,
                getMd5());
    }

    /**
     * 事件上报开始
     */
    public void trackEventStart() {
        trackEvent(MobiConstantValue.EVENT.START);
    }

    /**
     * 事件上报 填充->下载好了
     */
    public void trackEventLoad() {
        trackEvent(MobiConstantValue.EVENT.LOAD);
    }


    /**
     * 事件上报 填充->下载好了
     */
    public void trackEventShow() {
        trackEvent(MobiConstantValue.EVENT.SHOW);
    }

    /**
     * 事件上报 填充->下载好了
     */
    public void trackEventClick() {
        trackEvent(MobiConstantValue.EVENT.CLICK);
    }

    /**
     * 事件上报 填充->下载好了
     */
    public void trackEventClose() {
        trackEvent(MobiConstantValue.EVENT.CLOSE);
    }

    /**
     * 开始显示
     */
    public void trackStartShow() {
        trackEvent(MobiConstantValue.EVENT.START_SHOW);
    }

    /**
     * 缓存好的一个事件
     */
    public void trackCache() {
        trackEvent(MobiConstantValue.EVENT.CACHE);
    }

    /**
     * 完成了一个事件
     */
    public void trackComplete() {
        trackEvent(MobiConstantValue.EVENT.COMPLETE);
    }

    /**
     * 跳过一个事件
     */
    public void trackSkip() {
        trackEvent(MobiConstantValue.EVENT.SKIP);
    }

    /**
     * 事件渲染成功
     */
    public void trackRenderSuccess() {
        trackEvent(MobiConstantValue.EVENT.RENDER_SUCCESS);
    }

    /**
     * 事件渲染成功
     */
    public void trackRewardVerify() {
        trackEvent(MobiConstantValue.EVENT.REWARD_VERIFY);
    }

    /**
     * 广告展开遮盖时调用
     */
    public void trackGdtOpenOverlay() {
        trackEvent(MobiConstantValue.EVENT.GDT_OPEN_OVERLAY);
    }

    /**
     * 广告关闭遮盖时调用
     */
    public void trackGdtCloseOverlay() {
        trackEvent(MobiConstantValue.EVENT.GDT_CLOSE_OVERLAY);
    }

    /**
     * 广告关闭遮盖时调用
     */
    public void trackGdtShow() {
        trackEvent(MobiConstantValue.EVENT.GDT_SHOW);
    }

    /**
     * gdt离开app的回调
     */
    public void trackGdtLeftApplication() {
        trackEvent(MobiConstantValue.EVENT.GDT_LEFT_APPLICATION);
    }


    /**
     * @param type    错误类型
     * @param code
     * @param message
     * @param debug
     */
    public void trackEventError(int type,
                                int code,
                                String message,
                                String debug) {

//        if (!isPushMessage()) {
//            Log.e(TAG, " 消息拦截 不再上报 ");
//            return;
//        }
        PushEventTrack.trackAD(MobiConstantValue.EVENT.ERROR,
                getStyleType(),
                getMobiCodeId(),
                getSortType(),
                mProviderType,
                getMd5(),
                type,
                code,
                message,
                debug);
    }


    public void trackEventError(int type, int code, String message) {

        trackEventError(type, code, message, "");
    }
}
