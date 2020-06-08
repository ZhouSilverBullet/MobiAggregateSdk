package com.mobi.common;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.MainThread;
import android.view.ViewGroup;

import com.mobi.core.AdParams;
import com.mobi.core.AdProviderManager;
import com.mobi.core.CoreSession;
import com.mobi.core.LocalAdParams;
import com.mobi.core.bean.LocalAdBean;
import com.mobi.core.bean.ShowAdBean;
import com.mobi.core.listener.IAdFailListener;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.splash.BaseSplashSkipView;
import com.mobi.core.strategy.AdRunnable;
import com.mobi.core.strategy.AdStrategyFactory;
import com.mobi.core.strategy.IShowAdStrategy;
import com.mobi.core.strategy.StrategyError;
import com.mobi.csj.CsjSession;
import com.mobi.exception.MobiNullPointerException;
import com.mobi.gdt.GdtSession;

import java.util.ArrayList;
import java.util.List;

import static com.mobi.common.CheckUtils.checkSafe;
import static com.mobi.common.CheckUtils.isAdInvalid;

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
        if (context == null) {
            throw new MobiNullPointerException("context == null");
        }

        CommonSession.get().init(context, appId);
    }

    public static void setDebug(boolean isDebug) {
        mIsDebug = isDebug;

        //给本地的CommonSession进行设置值
        CommonSession.get().setIsAppDebug(isDebug);
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
            callOnFail("MobiType", -100, "mobi codeid 不正确 或者 codeId == null", listener);
            return;
        }


        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = AdStrategyFactory.create(sortType);
        if (strategy == null) {
            callOnFail("MobiType", -100, "mobi 的策略，本地还没有支持", listener);
            return;
        }

        strategy.setAdFailListener(listener);

        for (ShowAdBean showAdBean : adBeans) {
            String postId = showAdBean.getPostId();

            LocalAdParams localAdParams = LocalAdParams.create(postId, adParams);

            AdRunnable runnable = AdProviderManager.get().getProvider(showAdBean.getProviderType())
                    .splash(activity,
                            splashContainer,
                            skipView,
                            localAdParams,
                            listener);

            strategy.addADTask(runnable);
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
    public static void showExpress(final Activity activity,
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
            callOnFail("MobiType", -100, "mobi codeid 不正确 或者 codeId == null", listener);
            return;
        }

        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = AdStrategyFactory.create(sortType);
        if (strategy == null) {
            if (listener != null) {
                listener.onAdFail("MobiType", -100, "mobi 的策略，本地还没有支持");
            }
            return;
        }

        strategy.setAdFailListener(listener);

        for (ShowAdBean showAdBean : adBeans) {
            String postId = showAdBean.getPostId();

            LocalAdParams localAdParams = LocalAdParams.create(postId, adParams);

            AdRunnable runnable = AdProviderManager.get().getProvider(showAdBean.getProviderType())
                    .express(activity,
                            viewContainer,
                            localAdParams,
                            listener);

            strategy.addADTask(runnable);
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
            callOnFail("MobiType", -100, "mobi codeid 不正确 或者 codeId == null", listener);
            return;
        }

        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = AdStrategyFactory.create(sortType);
        if (strategy == null) {
            callOnFail("MobiType", -100, "mobi 的策略，本地还没有支持", listener);
            return;
        }

        strategy.setAdFailListener(listener);

        for (ShowAdBean showAdBean : adBeans) {
            String postId = showAdBean.getPostId();

            LocalAdParams localAdParams = LocalAdParams.create(postId, adParams);

            AdRunnable runnable = AdProviderManager.get().getProvider(showAdBean.getProviderType())
                    .fullscreen(activity,
                            localAdParams,
                            listener);

            strategy.addADTask(runnable);
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
            if (listener != null) {
                listener.onAdFail("MobiType", -100, "mobi codeid 不正确 或者 codeId == null");
            }
            return;
        }

        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = AdStrategyFactory.create(sortType);
        if (strategy == null) {
            callOnFail("MobiType", -100, "mobi 的策略，本地还没有支持", listener);
            return;
        }

        strategy.setAdFailListener(listener);

        for (ShowAdBean showAdBean : adBeans) {
            String postId = showAdBean.getPostId();

            LocalAdParams localAdParams = LocalAdParams.create(postId, adParams);

            AdRunnable runnable = AdProviderManager.get().getProvider(showAdBean.getProviderType())
                    .rewardVideo(activity,
                            localAdParams,
                            listener);

            strategy.addADTask(runnable);
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
            callOnFail("MobiType", -100, "mobi codeid 不正确 或者 codeId == null", listener);
            return;
        }

        List<ShowAdBean> adBeans = localAdBean.getAdBeans();
        int sortType = localAdBean.getSortType();

        IShowAdStrategy strategy = AdStrategyFactory.create(sortType);
        if (strategy == null) {
            callOnFail("MobiType", -100, "mobi 的策略，本地还没有支持", listener);
            return;
        }

        strategy.setAdFailListener(listener);

        for (ShowAdBean showAdBean : adBeans) {
            String postId = showAdBean.getPostId();

            LocalAdParams localAdParams = LocalAdParams.create(postId, adParams);

            AdRunnable runnable = AdProviderManager.get().getProvider(showAdBean.getProviderType())
                    .interactionExpress(activity,
                            localAdParams,
                            listener);

            strategy.addADTask(runnable);
        }

        strategy.execShow();
    }


    private static LocalAdBean findsShowAdBean(Context context, String codeId) {
        LocalAdBean localAdBean = CoreSession.get().findShowAdBean(codeId);
        if (localAdBean == null) {
            return null;
        }

        //初始化需要初始化的工作
        initIfNeed(context, localAdBean);

        return localAdBean;
    }

    private static void callOnFail(String type, int code, String message, IAdFailListener listener) {
        if (listener != null) {
            StrategyError strategyError = new StrategyError(type, code, message);
            ArrayList<StrategyError> strategyErrorList = new ArrayList<>();
            strategyErrorList.add(strategyError);
            listener.onAdFail(strategyErrorList);
        }
    }


    /**
     * 进行对应的初始化工作
     *
     * @param context
     * @param localAdBean
     */
    private static void initIfNeed(Context context, LocalAdBean localAdBean) {
        List<ShowAdBean> adBeans = localAdBean.getAdBeans();

        for (ShowAdBean adBean : adBeans) {
            String providerType = adBean.getProviderType();
            boolean appDebug = CommonSession.isAppDebug();
            String appId = adBean.getAppId();

            if (!CsjSession.get().isInit() &&
                    AdProviderManager.TYPE_CSJ.equals(providerType)) {
                String appName = adBean.getAppName();
                //初始化csj
                CsjSession.get().init(
                        context,
                        appId,
                        appName,
                        appDebug);

            } else if (!GdtSession.get().isInit() &&
                    AdProviderManager.TYPE_GDT.equals(providerType)) {
                //初始化GDT
                GdtSession.get().init(context, appId, appDebug);
            }
        }
    }
}
