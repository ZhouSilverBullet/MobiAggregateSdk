package com.mobi.core.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.mobi.core.MobiConstantValue;

/**
 * Created by Administrator on 2018/5/5.
 */

public class DeviceUtil {

    @SuppressLint("MissingPermission")
    public static String getDeviceNo(Context context) {
        if (context == null) {
            return "";
        }

        String deviceNo = SpUtil.getString(MobiConstantValue.DEVICE_NO);
        if (!TextUtils.isEmpty(deviceNo)) {
            return deviceNo;
        }

        try {
            //获取当前设备的IMEI
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceNo = telephonyManager.getDeviceId();
            if (deviceNo == null) {
                deviceNo = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            SpUtil.putString(MobiConstantValue.DEVICE_NO, deviceNo);

        } catch (Exception e) {
            deviceNo = "";
        }


        return deviceNo == null ? "" : deviceNo;
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    /**
     * 获取网络状况
     *
     * @param context
     * @return
     */
    public static boolean isNetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable();
        }
        return false;
    }
}
