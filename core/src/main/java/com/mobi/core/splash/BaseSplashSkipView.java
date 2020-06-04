package com.mobi.core.splash;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 16:47
 * @Dec 略
 */
public abstract class BaseSplashSkipView {

    private int mCountTime = 5;

    /**
     * 是否点击跳转
     */
    private boolean isCanClickSkip = true;

    public void setCountTime(int second) {
        this.mCountTime = second;
    }

    public int getCountTime() {
        return mCountTime;
    }

    public boolean isCanClickSkip() {
        return isCanClickSkip;
    }

    public void setCanClickSkip(boolean canClickSkip) {
        isCanClickSkip = canClickSkip;
    }

    /**
     * 创建跳过按钮的布局
     *
     * @param context
     * @return
     */
    public abstract View createSkipView(Context context, ViewGroup container);

    /**
     * 处理倒计时的展示，单位：秒
     *
     * @param second
     */
    public abstract void onTime(int second);

    /**
     * 获取布局参数，控制跳过按钮的位置
     *
     * @return
     */
    public abstract ViewGroup.LayoutParams getLayoutParams();

}
