package com.mobi.common;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.mobi.core.AdParams;
import com.mobi.core.bean.LocalAdBean;
import com.mobi.core.utils.LogUtils;
import com.mobi.exception.MobiIllegalStateException;
import com.mobi.exception.MobiNullPointerException;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 20:47
 * @Dec 略
 */
class CheckUtils {
    public static final String TAG = "CheckUtils";

    static boolean checkSafe(Activity activity) {
        if (activity == null) {
            throw new MobiNullPointerException("activity == null");
        }

        if (activity.isFinishing()) {
            LogUtils.e(TAG, " activity is finishing" );
            return false;
        }


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN && activity.isDestroyed()) {
            LogUtils.e(TAG, " activity is finishing" );
            return false;
        }

        return true;
    }

    static boolean checkSafe(View view) {
        if (view == null) {
            throw new MobiNullPointerException("view == null");
        }

        return true;
    }

    static boolean checkSafe(AdParams adParams) {
        if (adParams == null) {
            throw new MobiNullPointerException("AdParams == null");
        }

        if (TextUtils.isEmpty(adParams.getCodeId())) {
            throw new MobiNullPointerException("codeId == null 或者 为空 \"\"");
        }
        return true;
    }

    static boolean checkStrSafe(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        return true;
    }

    /**
     * 判断adBean是否非法
     *
     * @param showAdBean
     * @return
     */
    static boolean isAdInvalid(LocalAdBean showAdBean) {
        return showAdBean == null || showAdBean.getAdBeans().size() == 0;
    }
}
