package com.mobi.aggsdk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.core.MobiAggregateSdk;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.utils.LogUtils;
import com.mobi.csj.CsjProvider;
import com.mobi.gdt.GdtProvider;

public class SplashActivity extends AppCompatActivity {
    public static final String TAG = "SplashActivity";
    ViewGroup clRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        clRoot = findViewById(R.id.clRoot);
//        CsjProvider csjProvider = new CsjProvider();
//        csjProvider.splash(this, Const.CSJ_SPLASH_ID, clRoot, new ISplashAdListener() {
//            @Override
//            public void onAdFail(String type, String s) {
//                LogUtils.e(TAG, "onAdFail : " + s);
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//
//            }
//
//            @Override
//            public void onAdClicked(String type) {
//
//            }
//
//            @Override
//            public void onAdExposure(String type) {
//
//            }
//
//            @Override
//            public void onAdDismissed(String type) {
//                LogUtils.e(TAG, "onAdDismissed");
//
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//            }
//        });

        clRoot.post(() -> showSplash(clRoot));

    }

    boolean canSkip;

    private void showSplash(ViewGroup clRoot) {
        MobiAggregateSdk.showSplash(this, clRoot, new ISplashAdListener() {
            @Override
            public void onAdStartRequest(@NonNull String providerType) {
                Log.e(TAG, "onAdStartRequest ");
            }

            @Override
            public void onAdFail(String type, int code, String errorMsg) {
                Log.e(TAG, "code: " + code + ", message: " + errorMsg);
                delayToHome(1000);
            }

            @Override
            public void onAdClicked(String type) {
                Log.e(TAG, "onAdClicked ");
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }

            @Override
            public void onAdExposure(String type) {
                Log.e(TAG, "onAdExposure ");
            }

            @Override
            public void onAdDismissed(String type) {
                Log.e(TAG, "onAdDismissed ");
//                if (!isClicked) {
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                }
//                isClicked = false;
                delayToHome(0);
            }

            @Override
            public void onAdLoaded(String providerType) {
                Log.e(TAG, "onAdLoaded ");
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
            delayToHome(1000);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        finish();
    }
}