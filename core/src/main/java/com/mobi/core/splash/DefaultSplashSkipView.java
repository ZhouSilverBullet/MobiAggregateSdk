package com.mobi.core.splash;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mobi.core.R;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 16:54
 * @Dec 略
 */
public class DefaultSplashSkipView extends BaseSplashSkipView {

    TextView mTextView;

    @Override
    public View createSkipView(Context context, ViewGroup container) {
        //code: 4009, message: 开屏广告的自定义跳过按钮尺寸小于3x3dp
        //这里出现了，
        if (mTextView == null) {
            mTextView = (TextView) LayoutInflater.from(context)
                    .inflate(R.layout.view_default_splash_skip, container, false);
        }
        return mTextView;
    }

    @Override
    public void onTime(int second) {
        mTextView.setText(second + " | 可跳过");
    }

    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.END | Gravity.TOP;
        lp.topMargin = 50;
        lp.rightMargin = 30;
        return lp;
    }
}
