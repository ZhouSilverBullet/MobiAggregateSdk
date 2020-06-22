package com.mobi.core.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.mobi.core.MobiConstantValue;

import java.util.Locale;

/**
 * Created by Administrator on 2018/5/5.
 */

public class DeviceUtil {


    /**
     * 获取网络状况
     *
     * @param context
     * @return
     */
    public static boolean isNetAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isAvailable();
            }
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * zh_CN
     *
     * @return
     */
    public static String getLanguageAndCountry() {
        String country = Locale.getDefault().getCountry();
        String language = Locale.getDefault().getLanguage();
        return language + "_" + country;
    }

    /**
     * 获取设备蜂窝网络运营商
     *
     * @return ["中国电信CTCC":3]["中国联通CUCC:2]["中国移动CMCC":1]["other":0]["无sim卡":-1]["数据流量未打开":-2]
     */
    public static String getCellularOperator(Context context) {

        String operator = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            operator = tm.getNetworkOperator();
        } catch (Exception e) {
        }

        return operator;
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {

        if (context == null) {
            return "";
        }

        String deviceId = SpUtil.getString(MobiConstantValue.DEVICE_ID);
        if (!TextUtils.isEmpty(deviceId)) {
            return deviceId;
        }

        try {
            //获取当前设备的IMEI
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
            SpUtil.putString(MobiConstantValue.DEVICE_ID, deviceId);

        } catch (Exception e) {
            deviceId = "";
        }

        return deviceId == null ? "" : deviceId;
    }


    /**
     * 获取android Id
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        String androidId = Settings.System.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if (TextUtils.isEmpty(androidId)) {
            androidId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return androidId;
    }

    /**
     * 获取设备名称，如：huawei vivo xiaomi ...
     *
     * @return
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获取设备名称，如：8.0 9 10
     *
     * @return
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号  如：V1911A , Redmi K20 Pro
     *
     * @return
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    @SuppressLint("MissingPermission")
    public static String[] getLocation(Context context) {
        String lat = "";
        String lng = "";
        try {
            if (context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    lat = location.getLatitude() + "";
                    lng = location.getLongitude() + "";
                }
            }
        } catch (Exception e) {
        }
        return new String[]{lng, lat};
    }


}
