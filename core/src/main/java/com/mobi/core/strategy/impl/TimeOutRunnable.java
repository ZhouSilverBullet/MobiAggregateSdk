package com.mobi.core.strategy.impl;

import com.mobi.core.strategy.AdRunnable;

import java.lang.ref.WeakReference;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/10 15:59
 * @Dec 略
 */
public class TimeOutRunnable implements Runnable {
    private final TimeOutCallback mOutCallback;
    private final int mType;

    public TimeOutRunnable(int type,  TimeOutCallback outCallback) {
        mType = type;
        mOutCallback = outCallback;
    }

    @Override
    public void run() {
        if (mOutCallback != null) {
            mOutCallback.onTimeOut(mType);
        }
    }

    public interface TimeOutCallback {
        void onTimeOut(int type);
    }
}
