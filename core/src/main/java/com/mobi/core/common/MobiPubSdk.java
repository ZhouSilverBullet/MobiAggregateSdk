package com.mobi.core.common;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.mobi.core.AdParams;
import com.mobi.core.AdProviderManager;
import com.mobi.core.IAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.analysis.AdPushParams;
import com.mobi.core.bean.LocalAdBean;
import com.mobi.core.bean.ShowAdBean;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.splash.BaseSplashSkipView;
import com.mobi.core.strategy.AdRunnable;
import com.mobi.core.strategy.IShowAdStrategy;

import java.util.List;

import static com.mobi.core.MobiConstantValue.*;
import static com.mobi.core.common.SdkUtils.callOnFail;
import static com.mobi.core.common.SdkUtils.checkSafe;
import static com.mobi.core.common.SdkUtils.findsShowAdBean;
import static com.mobi.core.common.SdkUtils.getMd5Value;
import static com.mobi.core.common.SdkUtils.isAdInvalid;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 11:30
 * @Dec 略
 */
public class MobiPubSdk {

    public static final String TAG = "MobiPubSdk";

    public static boolean mIsDebug = true;

    /**
     * @param context
     * @param appId
     */
    public static void init(Context context, String appId) {
        init(context, appId, mIsDebug);
    }

    /**
     * @param context
     * @param appId
     */
    public static void init(Context context, String appId, boolean isDebug) {
        SdkUtils.init(context, appId);
        SdkUtils.setDebug(isDebug);
    }

    /**
     * setDebug
     *
     * @param isDebug
     */
    public static void setDebug(boolean isDebug) {
        mIsDebug = isDebug;

        //给本地的CommonSession进行设置值
        SdkUtils.setDebug(isDebug);
    }

    @MainThread
    public static void loadSplash(Activity activity,
                                  ViewGroup splashContainer,
                                  @Nullable BaseSplashSkipView skipView,
                                  AdParams adParams,
                                  ISplashAdListener listener) {

        if (!checkSafe(activity)) {
            return;
        }
        checkSafe(splashContainer);
        //清除splash里面的子View
        splashContainer.removeAllViews();
        checkSafe(adParams);

        LocalAdBean localAdBean = findsShowAdBean(activity.getApplicationContext(), adParams.getCodeId());

        if (isAdInvalid(localAdBean)) {
            callOnFail(adParams.getCodeId(), 0, STYLE.SPLASH, TYPE_LOCAL_MOBI, SDK_CODE_10005, SDK_MESSAGE_10005, listener);
            return;
        }


        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = localAdBean.getAdStrategy();
        if (strategy == null) {
            callOnFail(adParams.getCodeId(), sortType, STYLE.SPLASH, TYPE_LOCAL_MOBI, SDK_CODE_10006, SDK_MESSAGE_10006, listener);
            return;
        }

        strategy.setAdFailListener(listener);

        String md5Value = getMd5Value(adParams.getCodeId(), sortType);

        for (ShowAdBean showAdBean : adBeans) {

            LocalAdParams localAdParams = LocalAdParams.create(sortType, adParams, showAdBean, md5Value);
            IAdProvider provider = AdProviderManager.get().getProvider(showAdBean.getProviderType());
            if (provider != null) {
                AdPushParams adPushParams = AdPushParams.create(localAdParams.getMobiCodeId(),
                        localAdParams.getMd5(),
                        sortType,
                        STYLE.SPLASH,
                        showAdBean.isPushOtherEvent());

                provider.setPushParams(adPushParams);

                AdRunnable runnable = provider
                        .splash(activity,
                                splashContainer,
                                skipView,
                                localAdParams,
                                listener);
                strategy.addADTask(runnable);
            }

        }

        strategy.execShow();

    }

    /**
     * 信息流
     *
     * @param context
     * @param viewContainer
     * @param adParams
     * @param listener
     */
    @MainThread
    public static void loadNativeExpress(Context context,
                                         ViewGroup viewContainer,
                                         AdParams adParams,
                                         IExpressListener listener) {
        checkSafe(context);
        checkSafe(viewContainer);
        checkSafe(adParams);


//        FragmentManager fragmentManager =
//                activity.getFragmentManager();
//        fragmentManager.beginTransaction()
//                .add(new AdFragment(), "mobiad")
//                .commitAllowingStateLoss();
        LocalAdBean localAdBean = findsShowAdBean(context.getApplicationContext(), adParams.getCodeId());

        if (isAdInvalid(localAdBean)) {
            callOnFail(adParams.getCodeId(), 0, STYLE.NATIVE_EXPRESS, TYPE_LOCAL_MOBI, SDK_CODE_10005, SDK_MESSAGE_10005, listener);
            return;
        }

        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = localAdBean.getAdStrategy();
        if (strategy == null) {
            callOnFail(adParams.getCodeId(), sortType, STYLE.NATIVE_EXPRESS, TYPE_LOCAL_MOBI, SDK_CODE_10006, SDK_MESSAGE_10006, listener);
            return;
        }

        strategy.setAdFailListener(listener);

        String md5Value = getMd5Value(adParams.getCodeId(), sortType);

        for (ShowAdBean showAdBean : adBeans) {

            LocalAdParams localAdParams = LocalAdParams.create(sortType, adParams, showAdBean, md5Value);

            IAdProvider provider = AdProviderManager.get().getProvider(showAdBean.getProviderType());
            if (provider != null) {
                AdPushParams adPushParams = AdPushParams.create(localAdParams.getMobiCodeId(),
                        localAdParams.getMd5(),
                        sortType,
                        STYLE.NATIVE_EXPRESS,
                        showAdBean.isPushOtherEvent());

                provider.setPushParams(adPushParams);
                AdRunnable runnable = provider
                        .nativeExpress(context,
                                viewContainer,
                                localAdParams,
                                listener);

                strategy.addADTask(runnable);
            }
        }

        strategy.execShow();
    }

    @MainThread
    public static void loadFullscreen(Activity activity,
                                      AdParams adParams,
                                      IFullScreenVideoAdListener listener) {

        if (!checkSafe(activity)) {
            return;
        }
        checkSafe(adParams);

        LocalAdBean localAdBean = findsShowAdBean(activity.getApplicationContext(), adParams.getCodeId());

        if (isAdInvalid(localAdBean)) {
            callOnFail(adParams.getCodeId(), 0, STYLE.FULL_SCREEN, TYPE_LOCAL_MOBI, SDK_CODE_10005, SDK_MESSAGE_10005, listener);
            return;
        }

        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = localAdBean.getAdStrategy();
        if (strategy == null) {
            callOnFail(adParams.getCodeId(), sortType, STYLE.FULL_SCREEN, TYPE_LOCAL_MOBI, SDK_CODE_10006, SDK_MESSAGE_10006, listener);
            return;
        }

        strategy.setAdFailListener(listener);

        String md5Value = getMd5Value(adParams.getCodeId(), sortType);

        for (ShowAdBean showAdBean : adBeans) {

            LocalAdParams localAdParams = LocalAdParams.create(sortType, adParams, showAdBean, md5Value);

            IAdProvider provider = AdProviderManager.get().getProvider(showAdBean.getProviderType());
            if (provider != null) {
                AdPushParams adPushParams = AdPushParams.create(localAdParams.getMobiCodeId(),
                        localAdParams.getMd5(),
                        sortType,
                        STYLE.FULL_SCREEN,
                        showAdBean.isPushOtherEvent());

                provider.setPushParams(adPushParams);

                AdRunnable runnable = provider
                        .fullscreen(activity,
                                localAdParams,
                                listener);

                strategy.addADTask(runnable);
            }

        }

        strategy.execShow();

    }

    @MainThread
    public static void loadRewardView(Activity activity,
                                      AdParams adParams,
                                      IRewardAdListener listener) {

        if (!checkSafe(activity)) {
            return;
        }
        checkSafe(adParams);

        LocalAdBean localAdBean = findsShowAdBean(activity.getApplicationContext(), adParams.getCodeId());

        if (isAdInvalid(localAdBean)) {
            callOnFail(adParams.getCodeId(), 0, STYLE.REWARD, TYPE_LOCAL_MOBI, SDK_CODE_10005, SDK_MESSAGE_10005, listener);
            return;
        }

        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = localAdBean.getAdStrategy();
        if (strategy == null) {
            callOnFail(adParams.getCodeId(), sortType, STYLE.REWARD, TYPE_LOCAL_MOBI, SDK_CODE_10006, SDK_MESSAGE_10006, listener);
            return;
        }

        strategy.setAdFailListener(listener);

        String md5Value = getMd5Value(adParams.getCodeId(), sortType);

        for (ShowAdBean showAdBean : adBeans) {

            LocalAdParams localAdParams = LocalAdParams.create(sortType, adParams, showAdBean, md5Value);

            IAdProvider provider = AdProviderManager.get().getProvider(showAdBean.getProviderType());
            if (provider != null) {
                AdPushParams adPushParams = AdPushParams.create(localAdParams.getMobiCodeId(),
                        localAdParams.getMd5(),
                        sortType,
                        STYLE.REWARD,
                        showAdBean.isPushOtherEvent());

                provider.setPushParams(adPushParams);

                AdRunnable runnable = provider
                        .rewardVideo(activity,
                                localAdParams,
                                listener);

                strategy.addADTask(runnable);
            }
        }

        strategy.execShow();
    }

    /**
     * 插屏
     *
     * @param activity
     * @param adParams
     * @param listener
     */
    @MainThread
    public static void loadInteractionExpress(Activity activity,
                                              AdParams adParams,
                                              IInteractionAdListener listener) {

        if (!checkSafe(activity)) {
            return;
        }
        checkSafe(adParams);

        LocalAdBean localAdBean = findsShowAdBean(activity.getApplicationContext(), adParams.getCodeId());

        if (isAdInvalid(localAdBean)) {
            callOnFail(adParams.getCodeId(), 0, STYLE.INTERACTION_EXPRESS, TYPE_LOCAL_MOBI, SDK_CODE_10005, SDK_MESSAGE_10005, listener);
            return;
        }

        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = localAdBean.getAdStrategy();
        if (strategy == null) {
            callOnFail(adParams.getCodeId(), sortType, STYLE.INTERACTION_EXPRESS, TYPE_LOCAL_MOBI, SDK_CODE_10006, SDK_MESSAGE_10006, listener);
            return;
        }

        strategy.setAdFailListener(listener);

        String md5Value = getMd5Value(adParams.getCodeId(), sortType);

        for (ShowAdBean showAdBean : adBeans) {

            LocalAdParams localAdParams = LocalAdParams.create(sortType, adParams, showAdBean, md5Value);
            IAdProvider provider = AdProviderManager.get().getProvider(showAdBean.getProviderType());
            if (provider != null) {
                AdPushParams adPushParams = AdPushParams.create(localAdParams.getMobiCodeId(),
                        localAdParams.getMd5(),
                        sortType,
                        STYLE.INTERACTION_EXPRESS,
                        showAdBean.isPushOtherEvent());

                provider.setPushParams(adPushParams);

                AdRunnable runnable = provider
                        .interactionExpress(activity,
                                localAdParams,
                                listener);

                strategy.addADTask(runnable);
            }
        }

        strategy.execShow();
    }

}
