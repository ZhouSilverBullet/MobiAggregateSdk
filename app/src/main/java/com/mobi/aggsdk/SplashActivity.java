package com.mobi.aggsdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.mobi.common.MobiPubSdk;
import com.mobi.core.AdParams;
import com.mobi.core.MobiAggregateSdk;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.splash.BaseSplashSkipView;
import com.mobi.core.splash.DefaultSplashSkipView;
import com.mobi.core.strategy.StrategyError;
import com.mobi.core.utils.LogUtils;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    public static final String TAG = "SplashActivity";
    ViewGroup clRoot;
    private boolean mIsSelfSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        clRoot = findViewById(R.id.clRoot);
        clRoot.post(() -> showSplash(clRoot));

        if (getIntent() != null) {
            mIsSelfSplash = getIntent().getBooleanExtra("isSelfSplash", false);
        }
    }

    boolean canSkip;

    private void showSplash(ViewGroup clRoot) {
        BaseSplashSkipView view = null;
        if (mIsSelfSplash) {
            view = new DefaultSplashSkipView();
        }

        AdParams adParams = new AdParams.Builder()
                .setCodeId("1024005")
                .setExpressViewAcceptedSize(1080, 1920)
                .setSupportDeepLink(true)
                .build();

        MobiPubSdk.showSplash(this, clRoot, view, adParams, new ISplashAdListener() {
            @Override
            public void onAdFail(List<StrategyError> strategyErrorList) {
                for (StrategyError strategyError : strategyErrorList) {
                    LogUtils.e(TAG, "onLoadFailed type : " + strategyError.getProviderType()
                            + " faildCode : " + strategyError.getCode() + ", faildMsg: " + strategyError.getMessage());
                }
                delayToHome(1000);
            }

            @Override
            public void onAdStartRequest(@NonNull String providerType) {
                Log.e(TAG, "onAdStartRequest providerType: " + providerType);
            }

            @Override
            public void onAdFail(String type, int code, String errorMsg) {
                Log.e(TAG, "providerType: " + type + "code: " + code + ", message: " + errorMsg);
            }

            @Override
            public void onAdClicked(String type) {
                Log.e(TAG, "onAdClicked " + "providerType: " + type);
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }

            @Override
            public void onAdExposure(String type) {
                Log.e(TAG, "onAdExposure " + "providerType: " + type);
            }

            @Override
            public void onAdDismissed(String type) {
                Log.e(TAG, "onAdDismissed " + "providerType: " + type);
//                if (!isClicked) {
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                }
//                isClicked = false;
                delayToHome(0);
            }

            @Override
            public void onAdLoaded(String providerType) {
                Log.e(TAG, "onAdLoaded " + "providerType: " + providerType);
            }
        });
    }

    private void delayToHome(int delayMillis) {
        clRoot.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, delayMillis);
    }

    @Override
    protected void onPause() {
        super.onPause();
        canSkip = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canSkip) {
            delayToHome(0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        finish();
    }
}