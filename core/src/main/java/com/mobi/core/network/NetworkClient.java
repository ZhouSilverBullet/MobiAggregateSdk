package com.mobi.core.network;

import android.content.Context;
import android.util.Log;

import com.mobi.core.CoreSession;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.bean.ConfigAdBean;
import com.mobi.core.bean.ConfigBean;
import com.mobi.core.utils.ConfigBeanUtil;
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
        SdkExecutors.SDK_THREAD_POOL.execute(() -> {

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
                requestConfig(callback);
            } else {
                if (isTimeout(configAdBean)) {
                    //刷新本地的config
                    requestConfig(callback);
                } else {
                    //普通config
                    requestProtoConfig(callback);
                }
            }
        });
    }

    public void timeoutRequestConfig(InitCallback callback) {
        SdkExecutors.SDK_THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                requestConfig(callback);
            }
        });
    }

    public void timeoutRequestProtoConfig(InitCallback callback) {
        SdkExecutors.SDK_THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                requestProtoConfig(callback);
            }
        });
    }

    private void requestProtoConfig(InitCallback callback) {
        HttpClient httpClient = new HttpClient();
        Request request = new Request.Builder()
                .setMethod(Request.GET)
                .setUrl(CoreSession.get().getProtoUrl())
                .build();
        Response response = httpClient.execute(request);

        if (response.getCode() == 200) {
            String resContent = response.body();
            //通过Sp存起来
            SpUtil.putCommitString(MobiConstantValue.PROTO_CONFIG, resContent);

            ConfigBean configBean = ConfigBeanUtil.getConfigBean(resContent);
            Log.e(TAG, "configBean ： " + configBean);
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
        Request request = new Request.Builder()
                .setMethod(Request.GET)
                .setUrl(MobiConstantValue.LOCAL_URL)
                .build();
        Response response = httpClient.execute(request);
        if (response.getCode() == 200) {
            String resContent = response.body();
            //通过Sp存起来
            SpUtil.putCommitString(MobiConstantValue.ALL_CONFIG, resContent);
            SpUtil.putCommitString(MobiConstantValue.PROTO_CONFIG, resContent);

            ConfigBean configBean = ConfigBeanUtil.getConfigBean(resContent);
            Log.e(TAG, "configBean ： " + configBean);
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
            CoreSession.get().runOnUiThread(() -> {

                if (callback != null) {
                    callback.onFailure(response.getCode(), response.body());
                }

            });
        }
    }


    public interface InitCallback {
        void onSuccess(ConfigBean configBean);

        void onFailure(int code, String message);
    }
}
