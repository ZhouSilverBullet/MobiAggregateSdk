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
import com.mobi.core.strategy.AdStrategyFactory;
import com.mobi.core.strategy.IShowAdStrategy;
import com.mobi.core.strategy.impl.ServiceOrderShowAdStrategy;
import com.mobi.core.utils.DeviceUtil;
import com.mobi.core.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 18:27
 * @Dec 略
 */
public class CoreSession implements NetworkClient.InitCallback {
    public static final String TAG = "CoreSession";

    private static boolean isAppDebug = true;
    private final Handler mHandler;
    private Context mContext;
    private NetworkClient mNetworkClient;
    private volatile ConfigBean configBean;
    private volatile ConfigAdBean configAdBean;

    private String deviceNo;

    /**
     * 上报的Url
     */
    private String reportUrl;

    /**
     * 协议的url
     */
    private String protoUrl;
    /**
     * 超时时间
     */
    private long timeOut;
    /**
     * mobi sdk 对应的 appId
     */
    private String mAppId;

    private CoreSession() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static CoreSession get() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void onSuccess(ConfigBean configBean) {
        setConfigBean(configBean);
        if (configBean != null) {
            ConfigAdBean configAdBean = configBean.getConfigAdBean();

            //这个如果是拉去协议的时候请求的，configAdBean是null所以不进行覆盖
            if (configAdBean != null) {
                setConfigAdBean(configAdBean);
            }
        }
    }

    @Override
    public void onFailure(int code, String message) {
        LogUtils.e(TAG, " 获取配置失败 ： " + message);
    }

    private static class SingletonHolder {
        private static final CoreSession INSTANCE = new CoreSession();
    }

    public void init(Context context) {
        LogUtils.e(TAG, " init1 ");
        if (mContext != null || context == null) {
            //已经初始化过了 || 传的context是null
            return;
        }
        mContext = context.getApplicationContext();
    }

    public void init(Context context, String appId, boolean isDebug) {
        LogUtils.e(TAG, " init2 ");
        mContext = context.getApplicationContext();
        mAppId = appId;
        isAppDebug = isDebug;

        mNetworkClient = new NetworkClient();
        mNetworkClient.init(context, this);
    }

    public Context getContext() {
        return mContext;
    }

    public static boolean isAppDebug() {
        return isAppDebug;
    }

    public static void setIsAppDebug(boolean isAppDebug) {
        CoreSession.isAppDebug = isAppDebug;
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
        if (getNetworkClient() != null) {
            //每次都进行判断协议是否已经超时了
            //超时了就去更新下载
            if (getNetworkClient().isProtoTimeout(configBean)) {
                getNetworkClient().timeoutRequestProtoConfig(this);
            }
        }
        return configBean;
    }

    public void setConfigBean(ConfigBean configBean) {
        this.configBean = configBean;
    }

    public void setConfigAdBean(ConfigAdBean configAdBean) {
        this.configAdBean = configAdBean;
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
        IShowAdStrategy strategy = AdStrategyFactory.create(configItemBean.getSort_type());
        localAdBean.setAdStrategy(strategy);

        String serviceOrderShowType = null;
        //根据服务的策略这里需要注入SortParameters
        if (configItemBean.getSort_type() == AdStrategyFactory.SORT_TYPE_SERVICE_ORDER) {
            if (strategy instanceof ServiceOrderShowAdStrategy) {
                int adExecIndex = AdProviderManager.get().getAdExecIndex(codeId);
                List<String> sortParameters = configItemBean.getSortParameters();
                if (sortParameters != null && sortParameters.size() > 0) {
                    int size = sortParameters.size();
                    serviceOrderShowType = sortParameters.get(adExecIndex % size);
                    int saveIndex = adExecIndex % size + 1;
                    AdProviderManager.get().putAdExecIndex(codeId, saveIndex);
                }
            }
        }

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

            //serviceOrderShowType 使用
            if (!TextUtils.isEmpty(serviceOrderShowType)) {
                ShowAdBean tempShowAdBean = null;
                for (int i = 0; i < list.size(); i++) {
                    ShowAdBean showAdBean = list.get(i);
                    String providerType = showAdBean.getProviderType();
                    if (serviceOrderShowType.equals(providerType)) {
                        tempShowAdBean = list.remove(i);
                        break;
                    }
                }
                if (tempShowAdBean != null) {
                    //添加的前面位置
                    list.add(0, tempShowAdBean);
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

    public String getReportUrl() {
        if (TextUtils.isEmpty(reportUrl)) {
            if (configAdBean != null) {
                reportUrl = configAdBean.getReportUrl();
            }
        }
        return reportUrl;
    }

    public String getProtoUrl() {
        if (TextUtils.isEmpty(protoUrl)) {
            if (configAdBean != null) {
                protoUrl = configAdBean.getProtoUrl();
            }
        }
        return protoUrl;
    }

    public long getTimeOut() {
        if (timeOut == 0L) {
            if (configBean != null) {
                timeOut = configBean.getAd_adk_req_timeout();
                if (timeOut == 0L) {
                    //默认返回5s钟
                    return 5000L;
                }
            }
        }
        return timeOut;
    }

    public String getAppId() {
        return mAppId;
    }
}
