package com.mobi.core.network;

import android.content.Context;
import android.util.Log;

import com.mobi.core.CoreSession;
import com.mobi.core.bean.ConfigBean;
import com.mobi.core.utils.ConfigBeanUtil;

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

            HttpClient httpClient = new HttpClient("http://www.zhousaito.top:8080/config", "GET", 10000, "UTF-8");
            httpClient.call();

            int responseCode = httpClient.getResponseCode();
            if (responseCode == 200) {
                String resContent = httpClient.getResContent();
                ConfigBean configBean = ConfigBeanUtil.getConfigBean(resContent);
//                ConfigBean configBean = new Gson().fromJson(resContent, ConfigBean.class);
                Log.e(TAG, "configBean ： " + configBean);

//                List<ConfigItemBean> list = configBean.getList();

                CoreSession.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onSuccess(configBean);
                        }
                    }
                });
            } else {
                if (callback != null) {
                    callback.onFailure(responseCode, httpClient.getErrInfo());
                }
            }
        });
    }


    public interface InitCallback {
        void onSuccess(ConfigBean configBean);

        void onFailure(int code, String message);
    }
}
