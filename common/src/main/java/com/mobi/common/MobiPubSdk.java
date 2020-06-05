package com.mobi.common;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.mobi.core.AdProviderManager;
import com.mobi.core.CoreSession;
import com.mobi.core.bean.ShowAdBean;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.splash.BaseSplashSkipView;
import com.mobi.csj.CsjSession;
import com.mobi.exception.MobiNullPointerException;
import com.mobi.gdt.GdtSession;

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
                                  String codeId,
                                  final ViewGroup splashContainer,
                                  BaseSplashSkipView skipView,
                                  final ISplashAdListener listener) {

        ShowAdBean showAdBean = findsShowAdBean(activity.getApplicationContext(), codeId);
        if (showAdBean == null) {
            if (listener != null) {
                listener.onAdFail("MobiType", -100, "mobi codeid 不正确 或者 codeId == null");
            }
            return;
        }

        AdProviderManager.get().getProvider(showAdBean.getProviderType()).splash(activity, showAdBean.getPostId(),
                600,
                800,
                true,
                skipView,
                splashContainer,
                listener);
    }

    /**
     * 信息流
     *
     * @param activity
     * @param codeId
     * @param supportDeepLink
     * @param listener
     */
    public static void showExpress(final Activity activity,
                                   final ViewGroup viewContainer,
                                   String codeId,
                                   boolean supportDeepLink,
                                   int aDViewWidth,
                                   int aDViewHeight,
                                   int loadCount,
                                   final IExpressListener listener) {


//        FragmentManager fragmentManager =
//                activity.getFragmentManager();
//        fragmentManager.beginTransaction()
//                .add(new AdFragment(), "mobiad")
//                .commitAllowingStateLoss();
        ShowAdBean showAdBean = findsShowAdBean(activity.getApplicationContext(), codeId);
        if (showAdBean == null) {
            return;
        }

        AdProviderManager.get().getProvider(showAdBean.getProviderType())
                .express(activity,
                        showAdBean.getPostId(),
                        supportDeepLink,
                        viewContainer,
                        aDViewWidth,
                        aDViewHeight,
                        loadCount,
                        listener);
    }


    public static void showFullscreen(final Activity activity, String codeId, int orientation, final IFullScreenVideoAdListener listener) {

        ShowAdBean showAdBean = findsShowAdBean(activity.getApplicationContext(), codeId);
        if (showAdBean == null) {
            return;
        }

        AdProviderManager.get().getProvider(showAdBean.getProviderType())
                .fullscreen(activity, showAdBean.getPostId(), orientation, true, listener);
    }

    public static void showRewardView(final Activity activity,
                                      final String codeId,
                                      boolean supportDeepLink,
                                      final IRewardAdListener listener) {

        ShowAdBean showAdBean = findsShowAdBean(activity.getApplicationContext(), codeId);
        if (showAdBean == null) {
            return;
        }

        AdProviderManager.get().getProvider(showAdBean.getProviderType())
                .rewardVideo(activity, showAdBean.getPostId(), supportDeepLink, listener);
    }

    /**
     * 插屏
     *
     * @param activity
     * @param codeId
     * @param supportDeepLink
     * @param listener
     */
    public static void showInteractionExpress(final Activity activity,
                                              final ViewGroup viewContainer,
                                              final String codeId,
                                              boolean supportDeepLink,
                                              float expressViewWidth,
                                              float expressViewHeight,
                                              final IInteractionAdListener listener) {

        ShowAdBean showAdBean = findsShowAdBean(activity.getApplicationContext(), codeId);
        if (showAdBean == null) {
            if (listener != null) {
                listener.onAdFail("MobiAd", -100, "codeId == null 或者 不正确");
            }
            return;
        }

        AdProviderManager.get()
                .getProvider(showAdBean.getProviderType())
                .interactionExpress(activity,
                        showAdBean.getPostId(),
                        supportDeepLink,
                        viewContainer,
                        expressViewWidth,
                        expressViewHeight,
                        listener);
    }


    private static ShowAdBean findsShowAdBean(Context context, String codeId) {
        ShowAdBean showAdBean = CoreSession.get().findShowAdBean(codeId);
        if (showAdBean == null) {
            return null;
        }

        //初始化需要初始化的工作
        initIfNeed(context, showAdBean);

        return showAdBean;
    }


    /**
     * 进行对应的初始化工作
     *
     * @param context
     * @param showAdBean
     */
    private static void initIfNeed(Context context, ShowAdBean showAdBean) {
        String providerType = showAdBean.getProviderType();
        boolean appDebug = CommonSession.isAppDebug();
        String appId = showAdBean.getAppId();

        if (AdProviderManager.TYPE_CSJ.equals(providerType)) {
            String appName = showAdBean.getAppName();
            //初始化csj
            CsjSession.get().init(
                    context,
                    appId,
                    appName,
                    appDebug);

        } else if (AdProviderManager.TYPE_GDT.equals(providerType)) {
            //初始化GDT
            GdtSession.get().init(context, appId, appDebug);
        }
    }
}
