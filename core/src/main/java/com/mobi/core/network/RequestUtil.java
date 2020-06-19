package com.mobi.core.network;

import android.content.Context;

import com.mobi.core.BuildConfig;
import com.mobi.core.CoreSession;
import com.mobi.core.utils.DeviceUtil;
import com.mobi.core.utils.NetUtil;

import java.util.Map;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/18 21:17
 * @Dec 略
 */
public class RequestUtil {
    /**
     * 上报的头部数据
     */
    public static void putEventHeader(Request request) {
        Context context = CoreSession.get().getContext();
        if (context == null) {
            return;
        }

        Map<String, String> headers = request.getHeaders();
        headers.put("carrier", DeviceUtil.getCellularOperator(context));
        headers.put("uuid", DeviceUtil.getAndroidId(context));
        headers.put("imei", DeviceUtil.getDeviceId(context));
        headers.put("idfa", "");
        headers.put("oaid", "xxxxxxx");
        headers.put("mak", DeviceUtil.getBrand());
        headers.put("mod", DeviceUtil.getSystemModel());
        headers.put("osv", DeviceUtil.getSystemVersion());
        headers.put("os", "android");
        headers.put("lan", DeviceUtil.getLanguageAndCountry());
        headers.put("ver", BuildConfig.VERSION_NAME);
        String[] location = DeviceUtil.getLocation(context);
        headers.put("lon", location[0]);
        headers.put("lat", location[1]);
        headers.put("nt", NetUtil.getNetworkName(context));

    }

}
