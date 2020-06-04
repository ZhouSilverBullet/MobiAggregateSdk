package com.mobi.core.utils;

import android.os.CountDownTimer;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/31 19:03
 * @Dec 给个时间进行倒计时处理  xx:xx:xx
 */
public class CountDownTimerWrap extends CountDownTimer {

    private volatile boolean isCancel;

    private Object tag;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public CountDownTimerWrap(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public CountDownTimerWrap(long millisInFuture) {
        super(millisInFuture, 1000);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int remainTime = (int) (millisUntilFinished / 1000L);
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = remainTime % 3600;
        if (remainTime > 3600) {
            h = remainTime / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = remainTime / 60;
            if (remainTime % 60 != 0) {
                s = remainTime % 60;
            }
        }
        if (countTimerListener != null && !isCancel()) {
            countTimerListener.onTime(String.format("%02d", h), String.format("%02d", d), String.format("%02d", s));
        }
    }

    @Override
    public void onFinish() {
        if (countTimerListener != null) {
            countTimerListener.onTimeFinish();
        }
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public boolean isCancel() {
        return isCancel;
    }

    private ICountTimerListener countTimerListener;

    public void setCountTimerListener(ICountTimerListener countTimerListener) {
        this.countTimerListener = countTimerListener;
    }

    public interface ICountTimerListener {
        void onTime(String hour, String minute, String second);

        void onTimeFinish();
    }
}
