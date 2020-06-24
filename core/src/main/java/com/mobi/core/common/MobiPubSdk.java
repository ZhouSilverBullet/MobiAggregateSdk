package com.mobi.core.common;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.MainThread;
import android.view.ViewGroup;

import com.mobi.core.AdParams;
import com.mobi.core.AdProviderManager;
import com.mobi.core.IAdProvider;
import com.mobi.core.LocalAdParams;
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

    public static void showSplash(final Activity activity,
                                  final ViewGroup splashContainer,
                                  BaseSplashSkipView skipView,
                                  AdParams adParams,
                                  final ISplashAdListener listener) {

        if (!checkSafe(activity)) {
            return;
        }
        checkSafe(splashContainer);
        //清除splash里面的子View
        splashContainer.removeAllViews();
        checkSafe(adParams);

        LocalAdBean localAdBean = findsShowAdBean(activity.getApplicationContext(), adParams.getCodeId());

        if (isAdInvalid(localAdBean)) {
            callOnFail(adParams.getCodeId(), 0, TYPE_LOCAL_MOBI, SDK_CODE_10005, SDK_MESSAGE_10005, listener);
            return;
        }


        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = localAdBean.getAdStrategy();
        if (strategy == null) {
            callOnFail(adParams.getCodeId(), sortType, TYPE_LOCAL_MOBI, SDK_CODE_10006, SDK_MESSAGE_10006, listener);
            return;
        }

        strategy.setAdFailListener(listener);

        for (ShowAdBean showAdBean : adBeans) {

            LocalAdParams localAdParams = LocalAdParams.create(sortType, adParams, showAdBean);
            IAdProvider provider = AdProviderManager.get().getProvider(showAdBean.getProviderType());
            if (provider != null) {
                provider.setMobiCodeId(localAdParams.getMobiCodeId());
                provider.setMd5(localAdParams.getMd5());
                provider.setSortType(sortType);
                provider.setPushMessage(showAdBean.isPushMessage());
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
     * @param activity
     * @param viewContainer
     * @param adParams
     * @param listener
     */
    @MainThread
    public static void showNativeExpress(final Activity activity,
                                         final ViewGroup viewContainer,
                                         AdParams adParams,
                                         final IExpressListener listener) {
        if (!checkSafe(activity)) {
            return;
        }
        checkSafe(viewContainer);
        checkSafe(adParams);


//        FragmentManager fragmentManager =
//                activity.getFragmentManager();
//        fragmentManager.beginTransaction()
//                .add(new AdFragment(), "mobiad")
//                .commitAllowingStateLoss();
        LocalAdBean localAdBean = findsShowAdBean(activity.getApplicationContext(), adParams.getCodeId());

        if (isAdInvalid(localAdBean)) {
            callOnFail(adParams.getCodeId(), 0, TYPE_LOCAL_MOBI, SDK_CODE_10005, SDK_MESSAGE_10005, listener);
            return;
        }

        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = localAdBean.getAdStrategy();
        if (strategy == null) {
            callOnFail(adParams.getCodeId(), sortType, TYPE_LOCAL_MOBI, SDK_CODE_10006, SDK_MESSAGE_10006, listener);
            return;
        }

        strategy.setAdFailListener(listener);

        for (ShowAdBean showAdBean : adBeans) {

            LocalAdParams localAdParams = LocalAdParams.create(sortType, adParams, showAdBean);

            IAdProvider provider = AdProviderManager.get().getProvider(showAdBean.getProviderType());
            if (provider != null) {
                provider.setMobiCodeId(localAdParams.getMobiCodeId());
                provider.setMd5(localAdParams.getMd5());
                provider.setSortType(sortType);
                provider.setPushMessage(showAdBean.isPushMessage());
                AdRunnable runnable = provider
                        .nativeExpress(activity,
                                viewContainer,
                                localAdParams,
                                listener);

                strategy.addADTask(runnable);
            }
        }

        strategy.execShow();
    }


    public static void showFullscreen(final Activity activity,
                                      AdParams adParams,
                                      final IFullScreenVideoAdListener listener) {

        if (!checkSafe(activity)) {
            return;
        }
        checkSafe(adParams);

        LocalAdBean localAdBean = findsShowAdBean(activity.getApplicationContext(), adParams.getCodeId());

        if (isAdInvalid(localAdBean)) {
            callOnFail(adParams.getCodeId(), 0, TYPE_LOCAL_MOBI, SDK_CODE_10005, SDK_MESSAGE_10005, listener);
            return;
        }

        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = localAdBean.getAdStrategy();
        if (strategy == null) {
            callOnFail(adParams.getCodeId(), sortType, TYPE_LOCAL_MOBI, SDK_CODE_10006, SDK_MESSAGE_10006, listener);
            return;
        }

        strategy.setAdFailListener(listener);

        for (ShowAdBean showAdBean : adBeans) {

            LocalAdParams localAdParams = LocalAdParams.create(sortType, adParams, showAdBean);

            IAdProvider provider = AdProviderManager.get().getProvider(showAdBean.getProviderType());
            if (provider != null) {
                provider.setMobiCodeId(localAdParams.getMobiCodeId());
                provider.setMd5(localAdParams.getMd5());
                provider.setSortType(sortType);
                provider.setPushMessage(showAdBean.isPushMessage());
                AdRunnable runnable = provider
                        .fullscreen(activity,
                                localAdParams,
                                listener);

                strategy.addADTask(runnable);
            }

        }

        strategy.execShow();

    }

    public static void showRewardView(final Activity activity,
                                      AdParams adParams,
                                      final IRewardAdListener listener) {

        if (!checkSafe(activity)) {
            return;
        }
        checkSafe(adParams);

        LocalAdBean localAdBean = findsShowAdBean(activity.getApplicationContext(), adParams.getCodeId());

        if (isAdInvalid(localAdBean)) {
            callOnFail(adParams.getCodeId(), 0, TYPE_LOCAL_MOBI, SDK_CODE_10005, SDK_MESSAGE_10005, listener);
            return;
        }

        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = localAdBean.getAdStrategy();
        if (strategy == null) {
            callOnFail(adParams.getCodeId(), sortType, TYPE_LOCAL_MOBI, SDK_CODE_10006, SDK_MESSAGE_10006, listener);
            return;
        }

        strategy.setAdFailListener(listener);

        for (ShowAdBean showAdBean : adBeans) {

            LocalAdParams localAdParams = LocalAdParams.create(sortType, adParams, showAdBean);

            IAdProvider provider = AdProviderManager.get().getProvider(showAdBean.getProviderType());
            if (provider != null) {
                provider.setMobiCodeId(localAdParams.getMobiCodeId());
                provider.setMd5(localAdParams.getMd5());
                provider.setSortType(sortType);
                provider.setPushMessage(showAdBean.isPushMessage());
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
    public static void showInteractionExpress(final Activity activity,
                                              AdParams adParams,
                                              final IInteractionAdListener listener) {

        if (!checkSafe(activity)) {
            return;
        }
        checkSafe(adParams);

        LocalAdBean localAdBean = findsShowAdBean(activity.getApplicationContext(), adParams.getCodeId());

        if (isAdInvalid(localAdBean)) {
            callOnFail(adParams.getCodeId(), 0, TYPE_LOCAL_MOBI, SDK_CODE_10005, SDK_MESSAGE_10005, listener);
            return;
        }

        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = localAdBean.getAdStrategy();
        if (strategy == null) {
            callOnFail(adParams.getCodeId(), sortType, TYPE_LOCAL_MOBI, SDK_CODE_10006, SDK_MESSAGE_10006, listener);
            return;
        }

        strategy.setAdFailListener(listener);

        for (ShowAdBean showAdBean : adBeans) {

            LocalAdParams localAdParams = LocalAdParams.create(sortType, adParams, showAdBean);
            IAdProvider provider = AdProviderManager.get().getProvider(showAdBean.getProviderType());
            if (provider != null) {
                provider.setMobiCodeId(localAdParams.getMobiCodeId());
                provider.setMd5(localAdParams.getMd5());
                provider.setSortType(sortType);
                provider.setPushMessage(showAdBean.isPushMessage());
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
