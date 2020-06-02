package com.mobi.aggsdk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.core.listener.ISplashAdListener;
import com.mobi.core.utils.LogUtils;
import com.mobi.csj.CsjProvider;
import com.mobi.gdt.GdtProvider;

public class SplashActivity extends AppCompatActivity {
    public static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ViewGroup clRoot = findViewById(R.id.clRoot);
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

        new GdtProvider().splash(this, Const.GDT_SPLASH_ID, clRoot, new ISplashAdListener() {
            @Override
            public void onAdFail(String type, String s) {

            }

            @Override
            public void onAdClicked(String type) {

            }

            @Override
            public void onAdExposure(String type) {

            }

            @Override
            public void onAdDismissed(String type) {

            }

            @Override
            public void onAdLoaded(String providerType) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}