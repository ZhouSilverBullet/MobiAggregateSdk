package com.mobi.core.utils;

import android.text.TextUtils;
import android.util.Log;

import com.mobi.core.CoreSession;

/**
 * 作者: zhousaito
 * 日期: 2019/5/18
 */
public class L {
    static String log_mobi = "MobiPub";

    //for error log
    public static void error(String msg) {
        if (isDebug()) {
            msg = handleMsg(msg);
            Log.e(log_mobi, msg);
        }
    }

    //for warming log
    public static void warn(String msg) {
        if (isDebug()) {
            msg = handleMsg(msg);
            Log.w(log_mobi, msg);
        }
    }

    //for info log
    public static void info(String msg) {
        if (isDebug()) {
            msg = handleMsg(msg);
            Log.i(log_mobi, msg);
        }
    }

    //for debug log
    public static void debug(String msg) {
        if (isDebug()) {
            msg = handleMsg(msg);
            Log.d(log_mobi, msg);
        }
    }

    //for verbose log
    public static void verbose(String msg) {
        if (isDebug()) {
            msg = handleMsg(msg);
            Log.v(log_mobi, msg);
        }
    }

    //for error log
    public static void e(String msg) {
        if (isDebug()) {
            msg = handleMsg(msg);
            Log.e(log_mobi, msg);
        }
    }

    //for warming log
    public static void w(String msg) {
        if (isDebug()) {
            msg = handleMsg(msg);
            Log.w(log_mobi, msg);
        }
    }

    //for info log
    public static void i(String msg) {
        if (isDebug()) {
            msg = handleMsg(msg);
            Log.i(log_mobi, msg);
        }
    }

    //for debug log
    public static void d(String msg) {
        if (isDebug()) {
            msg = handleMsg(msg);
            Log.d(log_mobi, msg);
        }
    }

    //for verbose log
    public static void v(String msg) {
        if (isDebug()) {
            msg = handleMsg(msg);
            Log.v(log_mobi, msg);
        }
    }


    //for warming log
    public static void w(String tag, String msg) {
        if (isDebug()) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = log_mobi;
            }
            msg = handleMsg(msg);
            Log.w(tag, msg);
        }
    }

    //for info log
    public static void i(String tag, String msg) {
        if (isDebug()) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = log_mobi;
            }
            msg = handleMsg(msg);
            Log.i(tag, msg);
        }
    }

    //for debug log
    public static void d(String tag, String msg) {
        if (isDebug()) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = log_mobi;
            }
            msg = handleMsg(msg);
            Log.d(tag, msg);
        }
    }

    //for verbose log
    public static void v(String tag, String msg) {
        if (isDebug()) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = log_mobi;
            }
            msg = handleMsg(msg);
            Log.v(tag, msg);
        }
    }

    //for verbose log
    public static void e(String tag, String msg) {
        if (isDebug()) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = log_mobi;
            }
            msg = handleMsg(msg);
            Log.e(tag, msg);
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
        if (isDebug() && !TextUtils.isEmpty(msg)) {
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

                Log.i(tag, msg + "\n  ---->at " + className + "." + methodName + "("
                        + className + ".java:" + lineNumber + ")");
            } else {
                Log.i(tag, msg);
            }
        }
    }

    private static boolean isDebug() {
        return CoreSession.isAppDebug();
    }
}
