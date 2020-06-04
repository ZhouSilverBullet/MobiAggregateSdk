package com.mobi.core;

import android.app.Activity;
import android.view.ViewGroup;

import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.splash.BaseSplashSkipView;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/2 20:47
 * @Dec 略
 */
public interface IAdProvider {
    /**
     *
     * @param activity
     * @param codeId  对应的广告id
     *
     * @param expressViewWidth  如果传 <=0 就是全屏 csj需要
     * @param expressViewHeight 如果传 <=0 就是全屏 csj需要
     *   对应的是 AdSlot.Builder setExpressViewAcceptedSize(width, height)
     *
     * @param supportDeepLink 一般传true即可 csj需要
     *
     * @param splashContainer
     * @param listener
     */
    void splash(Activity activity,
                String codeId,
                int expressViewWidth,
                int expressViewHeight,
                boolean supportDeepLink,
                BaseSplashSkipView skipView,
                ViewGroup splashContainer,
                ISplashAdListener listener);

    void fullscreen(final Activity activity,
                    final String codeId,
                    int orientation,
                    boolean supportDeepLink,
                    final IFullScreenVideoAdListener listener);

    default void rewardVideo(final Activity activity,
                             final String codeId,
                             boolean supportDeepLink,
                             final IRewardAdListener listener) {

    }

    /**
     * expressViewWidth ,expressViewHeight csj使用  gdt不需要
     *
     * @param activity
     * @param codeId
     * @param supportDeepLink
     * @param viewContainer
     * @param expressViewWidth
     * @param expressViewHeight
     * @param listener
     */
    default void interactionExpress(Activity activity,
                                    String codeId,
                                    boolean supportDeepLink,
                                    ViewGroup viewContainer,
                                    float expressViewWidth,
                                    float expressViewHeight,
                                    IInteractionAdListener listener) {
    }

    /**
     * 激励视频
     *
     * @param activity
     * @param codeId
     * @param supportDeepLink
     * @param viewContainer
     * @param aDViewWidth
     * @param aDViewHeight
     * @param listener
     */
    default void express(Activity activity,
                         String codeId,
                         boolean supportDeepLink,
                         ViewGroup viewContainer,
                         int aDViewWidth,
                         int aDViewHeight,
                         int loadCount,
                         IExpressListener listener) {
    }
}
