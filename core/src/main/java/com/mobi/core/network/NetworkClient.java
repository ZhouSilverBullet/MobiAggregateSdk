package com.mobi.core.network;

import android.content.Context;
import android.util.Log;

import com.mobi.core.CoreSession;
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
            String serviceConfig = SpUtil.getString("service_config");
            //设置上去
            CoreSession.get().setConfigBean(ConfigBeanUtil.getConfigBean(serviceConfig));

            //同步的网络请求
            //http://www.zhousaito.top:8080/config
            //http://cdn.findwxapp.com/mediation.dev2.txt
            HttpClient httpClient = new HttpClient();
            Request request = new Request.Builder()
                    .setMethod(Request.GET)
                    .setUrl("http://www.zhousaito.top:8080/config")
                    .build();
            Response response = httpClient.execute(request);

            if (response.getCode() == 200) {
                String resContent = response.body();
                //通过Sp存起来
                SpUtil.putCommitString("service_config", resContent);

                ConfigBean configBean = ConfigBeanUtil.getConfigBean(resContent);
                Log.e(TAG, "configBean ： " + configBean);

                CoreSession.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onSuccess(configBean);
                        }
                    }
                });
            } else {
                CoreSession.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFailure(response.getCode(), response.body());
                        }
                    }
                });
            }
        });
    }


    public interface InitCallback {
        void onSuccess(ConfigBean configBean);

        void onFailure(int code, String message);
    }
}
