package com.mobi.common;

import android.content.Context;

import com.mobi.core.CoreSession;
import com.mobi.csj.CsjSession;
import com.mobi.exception.MobiNullPointerException;
import com.mobi.gdt.GdtSession;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 11:30
 * @Dec 略
 */
public class MobiPubSdk {

    public static boolean mIsDebug = true;

    /**
     *
     * @param context
     * @param appId
     */
    public static void init(Context context, String appId) {
        if (context == null) {
            throw new MobiNullPointerException("context == null");
        }

        CommonSession.get().init(context);



//        CsjSession.get().init(context,
//                Const.CSJ_APPID,
//                Const.CSJ_APPNAME, false);
//
//        GdtSession.get().init(this, Const.GDT_APPID,
//                BuildConfig.DEBUG);
//
//        CoreSession.get().init(this, BuildConfig.DEBUG);
    }

    public static void setDebug(boolean isDebug) {
        mIsDebug = isDebug;
    }
}
