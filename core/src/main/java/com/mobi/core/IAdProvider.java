package com.mobi.core;

import android.app.Activity;
import android.view.ViewGroup;

import com.mobi.core.listener.IFullScreenVideoAdListener;
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
}
