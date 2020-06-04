package com.mobi.csj.wrapper;

import android.content.Context;

import com.bytedance.sdk.openadsdk.TTAdNative;
import com.mobi.csj.CsjSession;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 10:56
 * @Dec 略
 */
public class BaseAdWrapper {

    protected int getLoadCount(int loadCount) {
        int count = 1;

        if (loadCount > 0) {
            count = loadCount;
        }
        return count;
    }

    protected TTAdNative createAdNative(Context context) {
        return CsjSession.get().getAdManager().createAdNative(context);
    }
}
