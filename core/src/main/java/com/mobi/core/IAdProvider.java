package com.mobi.core;

import android.app.Activity;
import android.view.ViewGroup;

import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.listener.ISplashAdListener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/2 20:47
 * @Dec 略
 */
public interface IAdProvider {
    void splash(final Activity activity,
                final String codeId,
                final ViewGroup splashContainer,
                final ISplashAdListener listener);

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
