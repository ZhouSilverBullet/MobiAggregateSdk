package com.mobi.core;

import com.mobi.core.analysis.AdAnalysis;
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
    private String mMobiCodeId;
    private int mSortType;
    private String mMd5;
    private boolean mPushMessage;

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


    public String getProviderType() {
        return mProviderType;
    }

    public String getMobiCodeId() {
        return mMobiCodeId;
    }

    public void setMobiCodeId(String mobiCodeId) {
        mMobiCodeId = mobiCodeId;
    }

    @Override
    public void setMd5(String md5) {
        mMd5 = md5;
    }

    public String getMd5() {
        return mMd5;
    }

    @Override
    public void setSortType(int sortType) {
        mSortType = sortType;
    }

    public int getSortType() {
        return mSortType;
    }

    @Override
    public void setPushMessage(boolean pushMessage) {
        mPushMessage = pushMessage;
    }

    public boolean isPushMessage() {
        return mPushMessage;
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
     * @param event     显示 ...
     * @param styleType 类型：插屏 ...
     */
    public void trackEvent(int event, int styleType) {
//        if (!isPushMessage()) {
//            Log.e(TAG, " 消息拦截 不再上报 ");
//            return;
//        }

        PushEventTrack.trackAD(event,
                styleType,
                getMobiCodeId(),
                getSortType(),
                mProviderType,
                getMd5());
    }

    /**
     * 事件上报开始
     *
     * @param styleType 类型：插屏 ...
     */
    public void trackEventStart(int styleType) {
        trackEvent(MobiConstantValue.EVENT.START, styleType);
    }

    /**
     * 事件上报 填充->下载好了
     *
     * @param styleType 类型：插屏 ...
     */
    public void trackEventLoad(int styleType) {
        trackEvent(MobiConstantValue.EVENT.LOAD, styleType);
    }


    /**
     * 事件上报 填充->下载好了
     *
     * @param styleType 类型：插屏 ...
     */
    public void trackEventShow(int styleType) {
        trackEvent(MobiConstantValue.EVENT.SHOW, styleType);
    }

    /**
     * 事件上报 填充->下载好了
     *
     * @param styleType 类型：插屏 ...
     */
    public void trackEventClick(int styleType) {
        trackEvent(MobiConstantValue.EVENT.CLICK, styleType);
    }

    /**
     * 事件上报 填充->下载好了
     *
     * @param styleType 类型：插屏 ...
     */
    public void trackEventClose(int styleType) {
        trackEvent(MobiConstantValue.EVENT.CLOSE, styleType);
    }

    /**
     * 开始显示
     *
     * @param styleType
     */
    public void trackStartShow(int styleType) {
        trackEvent(MobiConstantValue.EVENT.START_SHOW, styleType);
    }

    /**
     * 缓存好的一个事件
     *
     * @param styleType
     */
    public void trackCache(int styleType) {
        trackEvent(MobiConstantValue.EVENT.CACHE, styleType);
    }

    /**
     * 完成了一个事件
     *
     * @param styleType
     */
    public void trackComplete(int styleType) {
        trackEvent(MobiConstantValue.EVENT.COMPLETE, styleType);
    }

    /**
     * 跳过一个事件
     *
     * @param styleType
     */
    public void trackSkip(int styleType) {
        trackEvent(MobiConstantValue.EVENT.SKIP, styleType);
    }

    /**
     * 事件渲染成功
     *
     * @param styleType
     */
    public void trackRenderSuccess(int styleType) {
        trackEvent(MobiConstantValue.EVENT.RENDER_SUCCESS, styleType);
    }

    /**
     * 事件渲染成功
     *
     * @param styleType
     */
    public void trackRewardVerify(int styleType) {
        trackEvent(MobiConstantValue.EVENT.REWARD_VERIFY, styleType);
    }

    /**
     * 广告展开遮盖时调用
     *
     * @param styleType
     */
    public void trackGdtOpenOverlay(int styleType) {
        trackEvent(MobiConstantValue.EVENT.GDT_OPEN_OVERLAY, styleType);
    }

    /**
     * 广告关闭遮盖时调用
     *
     * @param styleType
     */
    public void trackGdtCloseOverlay(int styleType) {
        trackEvent(MobiConstantValue.EVENT.GDT_CLOSE_OVERLAY, styleType);
    }

    /**
     * 广告关闭遮盖时调用
     *
     * @param styleType
     */
    public void trackGdtShow(int styleType) {
        trackEvent(MobiConstantValue.EVENT.GDT_SHOW, styleType);
    }

    /**
     * gdt离开app的回调
     *
     * @param styleType
     */
    public void trackGdtLeftApplication(int styleType) {
        trackEvent(MobiConstantValue.EVENT.GDT_LEFT_APPLICATION, styleType);
    }


    /**
     * @param styleType
     * @param type      错误类型
     * @param code
     * @param message
     * @param debug
     */
    public void trackEventError(int styleType, int type,
                                int code,
                                String message,
                                String debug) {

//        if (!isPushMessage()) {
//            Log.e(TAG, " 消息拦截 不再上报 ");
//            return;
//        }
        PushEventTrack.trackAD(MobiConstantValue.EVENT.ERROR,
                styleType,
                getMobiCodeId(),
                getSortType(),
                mProviderType,
                getMd5(),
                type,
                code,
                message,
                debug);
    }


    public void trackEventError(int styleType,
                                int type,
                                int code,
                                String message) {

        trackEventError(styleType, type, code, message, "");
    }
}
