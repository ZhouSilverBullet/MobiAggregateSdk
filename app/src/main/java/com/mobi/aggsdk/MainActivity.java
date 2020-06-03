package com.mobi.aggsdk;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.mobi.aggsdk.config.TTAdManagerHolder;
import com.mobi.aggsdk.utils.TToast;
import com.mobi.core.MobiAggregateSdk;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.utils.LogUtils;
import com.mobi.csj.CsjSession;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private Context mContext;
    private ViewGroup flContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        flContainer = findViewById(R.id.flContainer);

//        AdSlot adSlot = new AdSlot.Builder()
//                .setCodeId("1028117") //广告位id
//                .setSupportDeepLink(true)
//                .setAdCount(1) //请求广告数量为1到3条
//                .setExpressViewAcceptedSize(350, 0) //必填：期望个性化模板广告view的size,单位dp
//                .setImageAcceptedSize(640, 320) //这个参数设置即可，不影响个性化模板广告的size
//                .build();

        CsjSession.get().getAdManager().requestPermissionIfNecessary(this);

    }

    public void btnMessageIo(View view) {
        startActivity(new Intent(this, NativeExpressActivity.class));
    }

    public void btnFullScreen(View view) {
        MobiAggregateSdk.showFullscreen(this, 1, new IFullScreenVideoAdListener() {
            @Override
            public void onAdShow(String type) {
                LogUtils.e(TAG, "onAdShow ");
            }

            @Override
            public void onAdFail(String type, String errorMsg) {
                LogUtils.e(TAG, "onAdFail " + errorMsg);
            }

            @Override
            public void onAdLoad(String type) {
                LogUtils.e(TAG, "onAdLoad ");
            }

            @Override
            public void onCached(String type) {
                LogUtils.e(TAG, "onCached ");
            }

            @Override
            public void onAdClose(String providerType) {
                LogUtils.e(TAG, "onAdClose ");
            }

            @Override
            public void onVideoComplete(String providerType) {
                LogUtils.e(TAG, "onVideoComplete ");
            }

            @Override
            public void onSkippedVideo(String providerType) {
                LogUtils.e(TAG, "onSkippedVideo ");
            }

            @Override
            public void onAdVideoBarClick(String providerType) {
                LogUtils.e(TAG, "onAdVideoBarClick ");
            }
        });
    }

    public void btnReward(View view) {
        MobiAggregateSdk.showRewardView(this, "2090845242931421", true, new IRewardAdListener() {
            @Override
            public void onAdFail(String type, String errorMsg) {
                LogUtils.e(TAG, "onAdFail type : " + type + ", " + errorMsg);
            }

            @Override
            public void onAdLoad(String type) {
                LogUtils.e(TAG, "onAdLoad type : " + type);

            }

            @Override
            public void onAdShow(String type) {
                LogUtils.e(TAG, "onAdShow type : " + type);
            }

            @Override
            public void onAdClick(String type) {
                LogUtils.e(TAG, "onAdLoad type : " + type);
            }

            @Override
            public void onAdClose(String providerType) {
                LogUtils.e(TAG, "onAdClose type : " + providerType);
            }

            @Override
            public void onVideoComplete(String providerType) {
                LogUtils.e(TAG, "onVideoComplete type : " + providerType);
            }

            @Override
            public void onSkippedVideo(String providerType) {
                LogUtils.e(TAG, "onSkippedVideo type : " + providerType);
            }

            @Override
            public void onRewardVerify(String providerType, boolean rewardVerify, int rewardAmount, String rewardName) {
                LogUtils.e(TAG, "onRewardVerify type : " + providerType);
            }

            @Override
            public void onCached(String type) {
                LogUtils.e(TAG, "onCached type : " + type);
            }
        });
    }

    public void btnInteraction(View view) {
        MobiAggregateSdk.showInteractionExpress(this, flContainer, "8020259898964453", true, new IInteractionAdListener() {
            @Override
            public void onAdFail(String type, String errorMsg) {
                LogUtils.e(TAG, "onAdFail type : " + type + ", " + errorMsg);
            }

            @Override
            public void onADReceive(String type) {
                LogUtils.e(TAG, "onADReceive type : " + type);

            }

            @Override
            public void onADOpened(String type) {
                LogUtils.e(TAG, "onADOpened type : " + type);

            }

            @Override
            public void onADExposure(String type) {
                LogUtils.e(TAG, "onADExposure type : " + type);

            }

            @Override
            public void onAdLoad(String type) {
                LogUtils.e(TAG, "onAdLoad type : " + type);

            }

            @Override
            public void onAdShow(String type) {
                LogUtils.e(TAG, "onAdShow type : " + type);
            }

            @Override
            public void onAdClick(String type) {
                LogUtils.e(TAG, "onAdLoad type : " + type);
            }

            @Override
            public void onAdClose(String providerType) {
                LogUtils.e(TAG, "onAdClose type : " + providerType);
            }

            @Override
            public void onVideoComplete(String providerType) {
                LogUtils.e(TAG, "onVideoComplete type : " + providerType);
            }

            @Override
            public void onSkippedVideo(String providerType) {
                LogUtils.e(TAG, "onSkippedVideo type : " + providerType);
            }

            @Override
            public void onRewardVerify(String providerType, boolean rewardVerify, int rewardAmount, String rewardName) {
                LogUtils.e(TAG, "onRewardVerify type : " + providerType);
            }

            @Override
            public void onCached(String type) {
                LogUtils.e(TAG, "onCached type : " + type);
            }
        });
    }

    public void btnExpress(View view) {
        MobiAggregateSdk.showExpress(this, flContainer, "901121125", true, new IExpressListener() {
            @Override
            public void onAdClick(String type) {
                LogUtils.e(TAG, "onAdClick type : " + type);
            }

            @Override
            public void onLoadFailed(String type, int faildCode, String faildMsg) {
                LogUtils.e(TAG, "onLoadFailed type : " + type + " faildCode : " + faildCode + ", faildMsg: " + faildMsg);
            }

            @Override
            public void onAdDismissed(String type) {
                LogUtils.e(TAG, "onAdDismissed type : " + type);

            }

            @Override
            public void onAdRenderSuccess(String type) {
                LogUtils.e(TAG, "onAdRenderSuccess type : " + type);
            }

            @Override
            public void onAdShow(String type) {
                LogUtils.e(TAG, "onAdShow type : " + type);
            }
        });
    }
}