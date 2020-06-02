package com.mobi.core.utils;

import android.content.Context;

import com.mobi.core.CoreSession;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 18:20
 * @Dec 略
 */
public class ScreenUtils {
    public static int getAppWidth() {
        return CoreSession.get().getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getAppHeight() {
        return CoreSession.get().getContext().getResources().getDisplayMetrics().heightPixels;
    }
}
