package com.mobi.core.common;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import com.mobi.core.AdParams;
import com.mobi.core.AdProviderManager;
import com.mobi.core.CoreSession;
import com.mobi.core.IAdSession;
import com.mobi.core.FakeAdSession;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.analysis.event.PushEventTrack;
import com.mobi.core.bean.LocalAdBean;
import com.mobi.core.bean.ShowAdBean;
import com.mobi.core.listener.IAdFailListener;
import com.mobi.core.reflection.SdkReflection;
import com.mobi.core.strategy.StrategyError;
import com.mobi.core.utils.L;
import com.mobi.core.utils.LogUtils;
import com.mobi.core.exception.MobiNullPointerException;
import com.mobi.core.utils.MD5Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 20:47
 * @Dec 一个初始化和检测等进行
 */
class SdkUtils {
    public static final String TAG = "CheckUtils";

    static boolean checkSafe(Activity activity) {
        if (activity == null) {
            throw new MobiNullPointerException("activity == null");
        }

        if (activity.isFinishing()) {
            LogUtils.e(TAG, " activity is finishing");
            return false;
        }


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN && activity.isDestroyed()) {
            LogUtils.e(TAG, " activity is finishing");
            return false;
        }

        return true;
    }

    static boolean checkSafe(Context context) {
        if (context == null) {
            throw new MobiNullPointerException("context == null");
        }

        return true;
    }

    static boolean checkSafe(View view) {
        if (view == null) {
            throw new MobiNullPointerException("view == null");
        }

        return true;
    }

    static boolean checkSafe(AdParams adParams) {
        if (adParams == null) {
            throw new MobiNullPointerException("AdParams == null");
        }

        if (TextUtils.isEmpty(adParams.getCodeId())) {
            throw new MobiNullPointerException("codeId == null 或者 为空 \"\"");
        }
        return true;
    }

    static boolean checkStrSafe(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        return true;
    }

    /**
     * 判断adBean是否非法
     *
     * @param showAdBean
     * @return
     */
    static boolean isAdInvalid(LocalAdBean showAdBean) {
        return showAdBean == null || showAdBean.getAdBeans().size() == 0;
    }

    static void init(Context context, String appId) {
        if (context == null) {
            throw new MobiNullPointerException("context == null");
        }

        CoreSession.get().init(context, appId, true);
    }

    static void setDebug(boolean isAppDebug) {
        CoreSession.setIsAppDebug(isAppDebug);
    }


    static LocalAdBean findsShowAdBean(Context context, String codeId) {
        LocalAdBean localAdBean = CoreSession.get().findShowAdBean(codeId);
        if (localAdBean == null) {
            return null;
        }

        //初始化需要初始化的工作
        initIfNeed(context, localAdBean);

        return localAdBean;
    }

    static void callOnFail(String codeId, int sortType, int styleType, String type, int code, String message, IAdFailListener listener) {

        if (listener != null) {
            StrategyError strategyError = new StrategyError(type, code, message);
            ArrayList<StrategyError> strategyErrorList = new ArrayList<>();
            strategyErrorList.add(strategyError);
            listener.onAdFail(strategyErrorList);
        }

        PushEventTrack.trackAD(MobiConstantValue.EVENT.ERROR,
                styleType,
                codeId,
                sortType,
                type,
                "",
                code,
                0,
                "",
                message);
    }


    /**
     * 进行对应的初始化工作
     *
     * @param context
     * @param localAdBean
     */
    static void initIfNeed(Context context, LocalAdBean localAdBean) {
        List<ShowAdBean> adBeans = localAdBean.getAdBeans();

        for (ShowAdBean adBean : adBeans) {
            String providerType = adBean.getProviderType();
            boolean appDebug = CoreSession.isAppDebug();
            String appId = adBean.getAppId();
            String appName = adBean.getAppName();

//            if (!CsjSession.get().isInit() &&
//                    AdProviderManager.TYPE_CSJ.equals(providerType)) {
//                //初始化csj
//                CsjSession.get().init(
//                        context,
//                        appId,
//                        appName,
//                        appDebug);
//
//            } else if (!GdtSession.get().isInit() &&
//                    AdProviderManager.TYPE_GDT.equals(providerType)) {
//                //初始化GDT
//                GdtSession.get().init(context, appId, appName, appDebug);
//            }
            initSession(context, providerType, appId, appName, appDebug);
        }
    }

    /**
     * 计算用户一次请求的hash值，用于服务端分析这次请求的一条线
     *
     * @param mobiCodeId
     * @param sortType
     * @return
     */
    static String getMd5Value(String mobiCodeId, int sortType) {
        return MD5Helper.encode(mobiCodeId + sortType + "mobi" + System.currentTimeMillis());
    }


    static void initSession(Context context, String providerType, String appId, String appName, boolean appDebug) {
        IAdSession adSession = AdProviderManager.get().getAdSession(providerType);
        if (adSession == null) {
            String clazzPath = "";
            if (AdProviderManager.TYPE_CSJ.equals(providerType)) {
                clazzPath = AdProviderManager.TYPE_CSJ_PATH;
            } else if (AdProviderManager.TYPE_GDT.equals(providerType)) {
                clazzPath = AdProviderManager.TYPE_GDT_PATH;
            } else if (AdProviderManager.TYPE_KS.equals(providerType)) {
                clazzPath = AdProviderManager.TYPE_KS_PATH;
            } else if (AdProviderManager.TYPE_MOBI_SDK.equals(providerType)) {
                clazzPath = AdProviderManager.TYPE_MOBIS_SDK_PATH;
            } else if (AdProviderManager.TYPE_ADMOB.equals(providerType)) {
                clazzPath = AdProviderManager.TYPE_ADMOB_PATH;
            }
            if (!TextUtils.isEmpty(clazzPath)) {
                Object o = SdkReflection.findInitSession(context, clazzPath,
                        appId, appName, appDebug);
                //ClassNotFoundException
                if (o == null) {
                    AdProviderManager.get().putAdSession(providerType, FakeAdSession.get());
                } else if (o instanceof IAdSession) {
                    AdProviderManager.get().putAdSession(providerType, (IAdSession) o);
                } else {
                    LogUtils.e(TAG, "Platform : " + providerType + " init fail！！！");
                    L.e("Platform : " + providerType + " init fail！！！");
                }
            }
        } else {
            if (adSession instanceof FakeAdSession) {
                if (AdProviderManager.TYPE_CSJ.equals(providerType)) {
                    LogUtils.e(TAG, "没有导入或者不支持 或者 穿山甲 初始化出错");
                    L.e(TAG, "没有导入或者不支持 或者 穿山甲 初始化出错");

                } else if (AdProviderManager.TYPE_GDT.equals(providerType)) {
                    LogUtils.e(TAG, "没有导入或者不支持 或者 广点通 初始化出错");
                    L.e(TAG, "没有导入或者不支持 或者 广点通 初始化出错");
                } else {
                    //todo 更多
                }
            }
        }
    }
}
