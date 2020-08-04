package com.mobi.mobiexchanger.wrapper;

import com.mobi.core.strategy.AdRunnable;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 10:56
 * @Dec 略
 */
public abstract class BaseAdWrapper extends AdRunnable {

    protected int getLoadCount(int loadCount) {
        int count = 1;

        if (loadCount > 0) {
            count = loadCount;
        }
        return count;
    }

}
