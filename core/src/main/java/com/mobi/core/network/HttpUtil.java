package com.mobi.core.network;

import android.text.TextUtils;

import com.mobi.core.utils.LogUtils;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Locale;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/15 15:36
 * @Dec 略
 */
public class HttpUtil {
    public static final String TAG = "HttpUtil";

    public static String getHttpExceptionMessage(Throwable t) {
        return getHttpExceptionMessage(t, "");
    }

    public static String getHttpExceptionMessage(Throwable exception, String errorMsg) {
        String defaultMsg = "未知错误";
        if (exception != null) {
            LogUtils.e(TAG, "Request Exception:" + exception.getMessage());
            if (exception instanceof UnknownHostException) {
                defaultMsg = "您的网络可能有问题,请确认连接上有效网络后重试";
            } else if (exception instanceof ConnectTimeoutException) {
                defaultMsg = "连接超时,您的网络可能有问题,请确认连接上有效网络后重试";
            } else if (exception instanceof SocketTimeoutException) {
                defaultMsg = "请求超时,您的网络可能有问题,请确认连接上有效网络后重试";
            } else if(exception instanceof JSONException) {
                defaultMsg = "json数据格式解析失败";
            } else {
                defaultMsg = "未知的网络错误, 请重试";
            }
        } else {
            if (!TextUtils.isEmpty(errorMsg)) {
                LogUtils.e(TAG, "Request Exception errorMsg: " + errorMsg);
                String lowerMsg = errorMsg.toLowerCase(Locale.ENGLISH);
                if (lowerMsg.contains("java")
                        || lowerMsg.contains("exception")
                        || lowerMsg.contains(".net")
                        || lowerMsg.contains("java")) {
                    defaultMsg = "未知错误, 请重试";
                } else {
                    defaultMsg = "未知错误, 请重试";
                }
            }
        }
        return defaultMsg;
    }
}
