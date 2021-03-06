package com.mobi.core;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.mobi.core.analysis.AdPushParams;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.splash.BaseSplashSkipView;
import com.mobi.core.strategy.AdRunnable;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/2 20:47
 * @Dec 略
 */
public interface IAdProvider {

    /**
     * 推送上传所需要的数据
     *
     * @param pushParams
     */
    void setPushParams(AdPushParams pushParams);

//    /**
//     * 用于上报使用
//     *
//     * @param mobiCodeId
//     */
//    void setMobiCodeId(String mobiCodeId);
//
//
//    void setMd5(String md5);
//
//    /**
//     *
//     * @param sortType
//     */
//    void setSortType(int sortType);
//
//    /**
//     * 是否上传消息
//     * @param pushMessage
//     */
//    void setPushMessage(boolean pushMessage);

    /**
     * @param activity
     * @param codeId            对应的广告id
     * @param expressViewWidth  如果传 <=0 就是全屏 csj需要
     * @param expressViewHeight 如果传 <=0 就是全屏 csj需要
     *                          对应的是 AdSlot.Builder setExpressViewAcceptedSize(width, height)
     * @param supportDeepLink   一般传true即可 csj需要
     * @param splashContainer
     * @param listener
     */
    AdRunnable splash(Activity activity,
                      ViewGroup splashContainer,
                      BaseSplashSkipView skipView,
                      LocalAdParams adParams,
                      ISplashAdListener listener);


    AdRunnable fullscreen(Activity activity,
                          LocalAdParams adParams,
                          IFullScreenVideoAdListener listener);

    AdRunnable rewardVideo(Activity activity,
                           LocalAdParams adParams,
                           IRewardAdListener listener);

    /**
     * expressViewWidth ,expressViewHeight csj使用  gdt不需要
     *
     * @param activity
     * @param adParams
     * @param listener
     */
    AdRunnable interactionExpress(Activity activity,
                                  LocalAdParams adParams,
                                  IInteractionAdListener listener);

    /**
     * 信息流
     *
     * @param context
     * @param viewContainer
     * @param adParams
     * @param listener
     */
    AdRunnable nativeExpress(Context context,
                             ViewGroup viewContainer,
                             LocalAdParams adParams,
                             IExpressListener listener);
}
