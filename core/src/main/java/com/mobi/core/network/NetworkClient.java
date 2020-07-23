package com.mobi.core.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mobi.core.BuildConfig;
import com.mobi.core.CoreSession;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.bean.ConfigAdBean;
import com.mobi.core.bean.ConfigBean;
import com.mobi.core.utils.ConfigBeanUtil;
import com.mobi.core.utils.DeviceUtil;
import com.mobi.core.utils.LogUtils;
import com.mobi.core.utils.SpUtil;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 14:10
 * @Dec 略
 */
public class NetworkClient {
    public static final String TAG = "NetworkControl";


    public void init(Context context, InitCallback callback) {
        CoreSession.get().getDispatcher().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    //读取存好的文件
                    //这个带有configURL的接口
                    String serviceConfig = SpUtil.getString(MobiConstantValue.ALL_CONFIG);
                    ConfigAdBean configAdBean = ConfigBeanUtil.getConfigAdBean(serviceConfig);
                    CoreSession.get().setConfigAdBean(configAdBean);

                    //获取这个serviceConfig把ConfigBean重新设置一下
                    ConfigBean configBean = ConfigBeanUtil.getConfigBean(serviceConfig);
                    if (configBean != null) {
                        CoreSession.get().setConfigBean(configBean);
                    }

                    if (configAdBean == null) {
                        //请求config
                        NetworkClient.this.requestConfig(callback);
                    } else {
                        if (NetworkClient.this.isTimeout(configAdBean)) {
                            //刷新本地的config
                            NetworkClient.this.requestConfig(callback);
                        } else {
                            //普通config
                            NetworkClient.this.requestProtoConfig(callback);
                        }
                    }
                } finally {
                    CoreSession.get().getDispatcher().finishRunnable(this);
                }
            }
        });
    }

    public void timeoutRequestConfig(InitCallback callback) {
        CoreSession.get().getDispatcher().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    requestConfig(callback);
                } finally {
                    CoreSession.get().getDispatcher().finishRunnable(this);
                }

            }
        });
    }

    public void timeoutRequestProtoConfig(InitCallback callback) {
        CoreSession.get().getDispatcher().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    requestProtoConfig(callback);
                } finally {
                    CoreSession.get().getDispatcher().finishRunnable(this);
                }
            }
        });
    }

    private void requestProtoConfig(InitCallback callback) {
        HttpClient httpClient = new HttpClient();
        String requestUrl = getRequestUrl(CoreSession.get().getProtoUrl());
        Request request = new Request.Builder()
                .setMethod(Request.GET)
                .setUrl(requestUrl)
                .build();
        Response response = httpClient.execute(request);

        if (response.getCode() == 200) {
            String resContent = response.body();
            //通过Sp存起来
            SpUtil.putCommitString(MobiConstantValue.PROTO_CONFIG, resContent);

            ConfigBean configBean = ConfigBeanUtil.getConfigBean(resContent);
            LogUtils.e(TAG, "configBean ： " + configBean);
            if (configBean != null) {
                SpUtil.putLongCommit(MobiConstantValue.PROTO_TIMEOUT,
                        System.currentTimeMillis() + configBean.getTimeout() * 1000);
            }

            CoreSession.get().runOnUiThread(() -> {
                if (callback != null) {
                    callback.onSuccess(configBean);
                }
            });
        } else {
            CoreSession.get().runOnUiThread(() -> {
                if (callback != null) {
                    callback.onFailure(response.getCode(), response.body());
                }
            });
        }
    }

    /**
     * 协议接口请求的超时的时间
     * 每次启动app的时候也刷一次
     *
     * @param configBean
     * @return
     */
    public boolean isProtoTimeout(ConfigBean configBean) {
        long timeout = SpUtil.getLong(MobiConstantValue.PROTO_TIMEOUT, System.currentTimeMillis() + 2000);
        //如果当前时间大于保存的时间，表示需要通过config去刷新接口
        if (System.currentTimeMillis() > timeout) {
            return true;
        }
        return false;
    }

    /**
     * 本地配置是否已经超时
     *
     * @param configAdBean
     * @return
     */
    public boolean isTimeout(ConfigAdBean configAdBean) {
        long timeout = SpUtil.getLong(MobiConstantValue.ALL_TIMEOUT, System.currentTimeMillis() + 2000);
        //如果当前时间大于保存的时间，表示需要通过config去刷新接口
        if (System.currentTimeMillis() > timeout) {
            return true;
        }
        return false;
    }

    private void requestConfig(InitCallback callback) {
        HttpClient httpClient = new HttpClient();

        String requestUrl = getRequestUrl(MobiConstantValue.LOCAL_URL);

        Request request = new Request.Builder()
                .setMethod(Request.GET)
                .setUrl(requestUrl)
                .build();

        RequestUtil.putEventHeader(request);

        Response response = httpClient.execute(request);
        if (response.getCode() == 200) {
            String resContent = response.body();
            //通过Sp存起来
            SpUtil.putCommitString(MobiConstantValue.ALL_CONFIG, resContent);
            SpUtil.putCommitString(MobiConstantValue.PROTO_CONFIG, resContent);

            ConfigBean configBean = ConfigBeanUtil.getConfigBean(resContent);
            LogUtils.e(TAG, "configBean2 ： " + configBean);
            if (configBean != null) {
                if (configBean.getConfigAdBean() != null) {
                    long timeout = configBean.getConfigAdBean().getTimeout();
                    SpUtil.putLongCommit(MobiConstantValue.ALL_TIMEOUT, System.currentTimeMillis() + timeout * 1000);
                }
                SpUtil.putLongCommit(MobiConstantValue.PROTO_TIMEOUT, System.currentTimeMillis() + configBean.getTimeout() * 1000);
            }

            CoreSession.get().runOnUiThread(() -> {

                if (callback != null) {
                    callback.onSuccess(configBean);
                }

            });
        } else {
            //出现异常了
//            if (response.getCode() == 0) {
//                Throwable e = response.getE();
//                if (e instanceof Throwable) {
//
//                }
//            }
            CoreSession.get().runOnUiThread(() -> {

                if (callback != null) {
                    callback.onFailure(response.getCode(), response.getMessage());
                }

            });
        }
    }

    private String getRequestUrl(String reqUrl) {
        if (TextUtils.isEmpty(reqUrl)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(reqUrl);
        sb.append("?sdkv=").append(BuildConfig.VERSION_NAME);
        sb.append("&imei=").append(DeviceUtil.getDeviceId(CoreSession.get().getContext()));
        sb.append("&os=").append(MobiConstantValue.PLATFORM);
        sb.append("&media_id=").append(CoreSession.get().getAppId());
        sb.append("&uuid=").append(DeviceUtil.getAndroidId(CoreSession.get().getContext()));
        return sb.toString();
    }


    public interface InitCallback {
        void onSuccess(ConfigBean configBean);

        void onFailure(int code, String message);
    }
}
