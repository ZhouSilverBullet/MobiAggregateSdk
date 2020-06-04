package com.mobi.csj.splash;

import android.view.View;
import android.view.ViewGroup;

import com.mobi.core.splash.BaseSplashSkipView;
import com.mobi.core.utils.CountDownTimerWrap;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 18:03
 * @Dec 略
 */
public class CsjSplashSkipViewControl implements CountDownTimerWrap.ICountTimerListener {
    private BaseSplashSkipView mBaseSplashSkipView;
    private int mMillisInFuture;
    /**
     * 用于跳转使用
     */
    private SkipCallback mSkipCallback;
    private CountDownTimerWrap mCountDownTimerWrap;

    public CsjSplashSkipViewControl(BaseSplashSkipView baseSplashSkipView) {
        mBaseSplashSkipView = baseSplashSkipView;
        if (mBaseSplashSkipView != null) {
            mMillisInFuture = mBaseSplashSkipView.getCountTime() * 1000;
        }
    }

    public void setSkipCallback(SkipCallback skipCallback) {
        mSkipCallback = skipCallback;
    }

    public void handleSplashSkipView(ViewGroup splashContainer) {
        View skipView = mBaseSplashSkipView.createSkipView(splashContainer.getContext(), splashContainer);
        ViewGroup.LayoutParams layoutParams = mBaseSplashSkipView.getLayoutParams();
        splashContainer.addView(skipView, layoutParams);
        //设置点击跳转
        if (mBaseSplashSkipView.isCanClickSkip()) {
            skipView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //自己调用跳过方法
                    if (mSkipCallback != null) {
                        mSkipCallback.onSkip();
                    }

                    if (mCountDownTimerWrap != null) {
                        mCountDownTimerWrap.cancel();
                    }
                }
            });
        }

        mCountDownTimerWrap = new CountDownTimerWrap(mMillisInFuture);
        mCountDownTimerWrap.setCountTimerListener(this);
        mCountDownTimerWrap.start();
    }

    @Override
    public void onTime(String hour, String minute, String second) {
        if (mBaseSplashSkipView != null) {
            try {
                mBaseSplashSkipView.onTime(Integer.parseInt(second));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTimeFinish() {
        if (mSkipCallback != null) {
            mSkipCallback.onSkip();
        }
    }

    /**
     * 跳过的时候使用
     * 要么倒计时完成，要么点击了跳过
     */
    public interface SkipCallback {
        void onSkip();
    }

}
