package com.mobi.core.utils;

import android.text.TextUtils;
import android.util.Log;

import com.mobi.core.BuildConfig;

/**
 * 作者: zhousaito
 * 日期: 2019/5/18
 */
public class LogUtils {
    static String log_mobi = "MobiPub";

    //for error log
    public static void error(String msg) {
        if (Log.isLoggable(log_mobi, Log.ERROR)) {
            msg = handleMsg(msg);
            Log.e(log_mobi, msg);
        }
    }

    //for warming log
    public static void warn(String msg) {
        if (Log.isLoggable(log_mobi, Log.WARN)) {
            msg = handleMsg(msg);
            Log.w(log_mobi, msg);
        }
    }

    //for info log
    public static void info(String msg) {
        if (Log.isLoggable(log_mobi, Log.INFO)) {
            msg = handleMsg(msg);
            Log.i(log_mobi, msg);
        }
    }

    //for debug log
    public static void debug(String msg) {
        if (Log.isLoggable(log_mobi, Log.DEBUG)) {
            msg = handleMsg(msg);
            Log.d(log_mobi, msg);
        }
    }

    //for verbose log
    public static void verbose(String msg) {
        if (Log.isLoggable(log_mobi, Log.VERBOSE)) {
            msg = handleMsg(msg);
            Log.v(log_mobi, msg);
        }
    }

    //for error log
    public static void e(String msg) {
        if (Log.isLoggable(log_mobi, Log.ERROR)) {
            msg = handleMsg(msg);
            Log.e(log_mobi, msg);
        }
    }

    //for warming log
    public static void w(String msg) {
        if (Log.isLoggable(log_mobi, Log.WARN)) {
            msg = handleMsg(msg);
            Log.w(log_mobi, msg);
        }
    }

    //for info log
    public static void i(String msg) {
        if (Log.isLoggable(log_mobi, Log.INFO)) {
            msg = handleMsg(msg);
            Log.i(log_mobi, msg);
        }
    }

    //for debug log
    public static void d(String msg) {
        if (Log.isLoggable(log_mobi, Log.DEBUG)) {
            msg = handleMsg(msg);
            Log.d(log_mobi, msg);
        }
    }

    //for verbose log
    public static void v(String msg) {
        if (Log.isLoggable(log_mobi, Log.VERBOSE)) {
            msg = handleMsg(msg);
            Log.v(log_mobi, msg);
        }
    }


    //for warming log
    public static void w(String tag, String msg) {
        if (Log.isLoggable(log_mobi, Log.WARN)) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = log_mobi;
            }
            msg = handleMsg(msg);
            Log.w(log_mobi, tag + "--" + msg);
        }
    }

    //for info log
    public static void i(String tag, String msg) {
        if (Log.isLoggable(log_mobi, Log.INFO)) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = log_mobi;
            }
            msg = handleMsg(msg);
            Log.i(log_mobi, tag + "--" + msg);
        }
    }

    //for debug log
    public static void d(String tag, String msg) {
        if (Log.isLoggable(log_mobi, Log.DEBUG)) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = log_mobi;
            }
            msg = handleMsg(msg);
            Log.d(log_mobi, tag + "--" +  msg);
        }
    }

    //for verbose log
    public static void v(String tag, String msg) {
        if (Log.isLoggable(log_mobi, Log.VERBOSE)) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = log_mobi;
            }
            msg = handleMsg(msg);
            Log.v(log_mobi, tag + "--" +  msg);
        }
    }

    //for verbose log
    public static void e(String tag, String msg) {
        if (Log.isLoggable(log_mobi, Log.ERROR)) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = log_mobi;
            }
            msg = handleMsg(msg);
            Log.e(log_mobi, tag + "--" + msg);
        }
    }

    private static String handleMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "";
        }
        return msg;
    }


    /**
     * 点击Log跳转到指定源码位置
     */
    public static void showLog(String tag, String msg) {
        if (Log.isLoggable(log_mobi, Log.INFO) && !TextUtils.isEmpty(msg)) {
            if (TextUtils.isEmpty(tag)) {
                tag = log_mobi;
            }
            StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
            int currentIndex = -1;
            for (int i = 0; i < stackTraceElement.length; i++) {
                if (stackTraceElement[i].getMethodName().compareTo("showLog") == 0) {
                    currentIndex = i + 1;
                    break;
                }
            }
            if (currentIndex >= 0) {
                String fullClassName = stackTraceElement[currentIndex].getClassName();
                String className = fullClassName.substring(fullClassName
                        .lastIndexOf(".") + 1);
                String methodName = stackTraceElement[currentIndex].getMethodName();
                String lineNumber = String
                        .valueOf(stackTraceElement[currentIndex].getLineNumber());

                Log.i(log_mobi, tag + "--" + msg + "\n  ---->at " + className + "." + methodName + "("
                        + className + ".java:" + lineNumber + ")");
            } else {
                Log.i(log_mobi, tag + "--" +  msg);
            }
        }
    }

    private static boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
