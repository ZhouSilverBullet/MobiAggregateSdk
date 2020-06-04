package com.mobi.gdt.wrapper;

import com.mobi.gdt.GdtSession;
import com.qq.e.ads.nativ.ADSize;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 11:29
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

    protected ADSize getADSize(boolean mHeightAuto, int ADViewWidth, int ADViewHeight) {
        if (mHeightAuto || ADViewHeight <= 0) {
            return new ADSize((int) ADViewWidth, ADSize.AUTO_HEIGHT);
        }
        return new ADSize((int) ADViewWidth, (int) ADViewHeight);
    }

    protected String getAppId() {
        return GdtSession.get().getAppId();
    }
}
