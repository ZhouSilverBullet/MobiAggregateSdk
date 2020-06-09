package com.mobi.core;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.mobi.core.bean.AdBean;
import com.mobi.core.bean.ConfigAdBean;
import com.mobi.core.bean.ConfigBean;
import com.mobi.core.bean.ConfigItemBean;
import com.mobi.core.bean.LocalAdBean;
import com.mobi.core.bean.ParameterBean;
import com.mobi.core.bean.ShowAdBean;
import com.mobi.core.network.NetworkClient;
import com.mobi.core.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 18:27
 * @Dec 略
 */
public class CoreSession {

    private static boolean isAppDebug;
    private final Handler mHandler;
    private Context mContext;
    private NetworkClient mNetworkClient;
    private volatile ConfigBean configBean;

    private String deviceNo;
    /**
     * 上报的Url
     */
    private String uploadUrl;

    private CoreSession() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static CoreSession get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CoreSession INSTANCE = new CoreSession();
    }

    public void init(Context context, boolean isDebug, NetworkClient.InitCallback initCallback) {
        mContext = context.getApplicationContext();
        isAppDebug = isDebug;

        mNetworkClient = new NetworkClient();
        mNetworkClient.init(context, new NetworkClient.InitCallback() {
            @Override
            public void onSuccess(ConfigBean configBean) {
                setConfigBean(configBean);

                if (initCallback != null) {
                    initCallback.onSuccess(configBean);
                }
            }

            @Override
            public void onFailure(int code, String message) {
                if (initCallback != null) {
                    initCallback.onFailure(code, message);
                }
            }
        });
    }

    public Context getContext() {
        return mContext;
    }

    public static boolean isAppDebug() {
        return isAppDebug;
    }

    public void runOnUiThread(Runnable runnable) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }

    public NetworkClient getNetworkClient() {
        return mNetworkClient;
    }

    public ConfigBean getConfigBean() {
        return configBean;
    }

    public void setConfigBean(ConfigBean configBean) {
        this.configBean = configBean;
    }

    public ConfigItemBean findConfigItemBean(String postId) {
        //传入的postId 进行判断
        if (TextUtils.isEmpty(postId)) {
            return null;
        }

        //configBean 进行判断
        ConfigBean configBean = CoreSession.get().getConfigBean();
        if (configBean == null) {
            return null;
        }

        //ConfigItemBean list 判断
        List<ConfigItemBean> list = configBean.getList();
        if (list == null || list.size() <= 0) {
            return null;
        }

        for (ConfigItemBean configItemBean : list) {
            if (configItemBean != null) {
                String posid = configItemBean.getPosid();
                if (postId.equals(posid)) {
                    return configItemBean;
                }
            }
        }

        return null;
    }

    public LocalAdBean findShowAdBean(String codeId) {
        ConfigItemBean configItemBean = CoreSession.get().findConfigItemBean(codeId);
        if (configItemBean == null) {
//            Log.e(TAG, "没有找到");
            return null;
        }

        //创建本地需要的AdBean
        LocalAdBean localAdBean = new LocalAdBean();
        localAdBean.setSortType(configItemBean.getSort_type());

        //ShowAdBean list
        List<ShowAdBean> list = new ArrayList<>();
        localAdBean.setAdBeans(list);


        List<AdBean> network = configItemBean.getNetwork();

        if (network != null) {
            for (AdBean adBean : network) {
                ParameterBean parameterBean = adBean.getParameterBean();
                String appid = parameterBean.getAppid();
                String appname = parameterBean.getAppname();
                String posId = parameterBean.getPosid();
                String sdk = adBean.getSdk();
                ShowAdBean showAdBean = new ShowAdBean();
                showAdBean.setPostId(posId);
                showAdBean.setAppId(appid);
                showAdBean.setAppName(appname);
                if ("tt".equals(sdk)) {
                    showAdBean.setProviderType(AdProviderManager.TYPE_CSJ);
                    list.add(showAdBean);
                } else if ("gdt".equals(sdk)) {
                    showAdBean.setProviderType(AdProviderManager.TYPE_GDT);
                    list.add(showAdBean);
                }
            }
        }

        return localAdBean;
    }

    public String getDeviceNo() {
        if (TextUtils.isEmpty(deviceNo)) {
            deviceNo = DeviceUtil.getDeviceNo(mContext);
        }
        return deviceNo;
    }

    public String getUploadUrl() {
        if (TextUtils.isEmpty(uploadUrl)) {
            if (configBean != null) {
                ConfigAdBean configAdBean = configBean.getConfigAdBean();
                if (configAdBean != null) {
                    uploadUrl = configAdBean.getReportUrl();
                }
            }
        }
        return uploadUrl;
    }
}
