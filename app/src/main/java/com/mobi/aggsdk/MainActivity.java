package com.mobi.aggsdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.core.ConstantValue;
import com.mobi.core.analysis.event.PushEvent;
import com.mobi.core.common.MobiPubSdk;
import com.mobi.core.AdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.analysis.AnalysisBean;
import com.mobi.core.db.use.DataManager;
import com.mobi.core.feature.IExpressAdView;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.network.SdkExecutors;
import com.mobi.core.strategy.StrategyError;
import com.mobi.core.utils.LogUtils;

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

    }

    public void btnSplash(View view) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("isSelfSplash", false);
        startActivity(intent);
    }

    public void btnFullScreen(View view) {
        AdParams adParams = new AdParams.Builder()
                .setCodeId("1024002")
                .setOrientation(ConstantValue.VERTICAL)
                .build();

        MobiPubSdk.showFullscreen(this, adParams, new IFullScreenVideoAdListener() {
            @Override
            public void onAdFail(List<StrategyError> strategyErrorList) {
                for (StrategyError strategyError : strategyErrorList) {
                    LogUtils.e(TAG, "onLoadFailed type : " + strategyError.getProviderType()
                            + " faildCode : " + strategyError.getCode() + ", faildMsg: " + strategyError.getMessage());
                }
            }

            @Override
            public void onAdExposure(String type) {
                LogUtils.e(TAG, "onAdShow ");
            }

            @Override
            public void onAdLoad(String type, IExpressAdView view, boolean isAutoShow) {
                LogUtils.e(TAG, "onAdLoad ");
                if (view != null) {
                    view.render();
                }
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
            public void onAdClick(String providerType) {
                LogUtils.e(TAG, "onAdVideoBarClick ");
            }
        });
    }

    public void btnReward(View view) {
        AdParams adParams = new AdParams.Builder()
                .setAutoShowAd(false)
                .setCodeId("1024003")
                .build();

        MobiPubSdk.showRewardView(this, adParams, new IRewardAdListener() {
            @Override
            public void onAdFail(List<StrategyError> strategyErrorList) {
                for (StrategyError strategyError : strategyErrorList) {
                    LogUtils.e(TAG, "onLoadFailed type : " + strategyError.getProviderType()
                            + " faildCode : " + strategyError.getCode() + ", faildMsg: " + strategyError.getMessage());
                }
            }

            @Override
            public void onAdLoad(String type, IExpressAdView view, boolean isAutoShow) {
                LogUtils.e(TAG, "onAdLoad type : " + type);
                renderAd(view, isAutoShow, 0);
            }

            @Override
            public void onAdExpose(String type) {
                LogUtils.e(TAG, "onAdExpose type : " + type);
            }

            @Override
            public void onAdShow(String type) {
                LogUtils.e(TAG, "onAdGdtShow type : " + type);
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
        AdParams adParams = new AdParams.Builder()
                .setCodeId("1024004")
                .setAutoShowAd(true)
                .build();
        MobiPubSdk.showInteractionExpress(this, adParams, new IInteractionAdListener() {
            @Override
            public void onAdFail(List<StrategyError> strategyErrorList) {
                for (StrategyError strategyError : strategyErrorList) {
                    LogUtils.e(TAG, "onLoadFailed type : " + strategyError.getProviderType()
                            + " faildCode : " + strategyError.getCode() + ", faildMsg: " + strategyError.getMessage());
                }
            }


            @Override
            public void onGdtOpened(String type) {
                LogUtils.e(TAG, "onADOpened type : " + type);

            }

            @Override
            public void onAdExposure(String type) {
                LogUtils.e(TAG, "onADExposure type : " + type);

            }

            @Override
            public void onAdLoad(String type, IExpressAdView view, boolean isAutoShow) {
                LogUtils.e(TAG, "onAdLoad type : " + type);
                renderAd(view, isAutoShow, 0);
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
            public void onGdtCached(String type) {
                LogUtils.e(TAG, "onCached type : " + type);
            }
        });
    }

    public void btnExpress(View view) {
        AdParams adParams = new AdParams.Builder()
                .setCodeId("1024001")
                .setAdCount(1)
                .setImageAcceptedSize(640, 320)
                .setExpressViewAcceptedSize(350, 0)
                .build();

        //native
        MobiPubSdk.showNativeExpress(this, flContainer, adParams, new IExpressListener() {
            @Override
            public void onAdClick(String type) {
                LogUtils.e(TAG, "onAdClick type : " + type);
            }

            @Override
            public void onAdLoad(String type, IExpressAdView view, boolean isAutoShow) {
                LogUtils.e(TAG, "onAdLoad type : " + type);
                renderAd(view, isAutoShow, 0);
            }

            @Override
            public void onAdFail(List<StrategyError> strategyErrorList) {
                for (StrategyError strategyError : strategyErrorList) {
                    LogUtils.e(TAG, "onLoadFailed type : " + strategyError.getProviderType()
                            + " faildCode : " + strategyError.getCode() + ", faildMsg: " + strategyError.getMessage());
                }
            }

            @Override
            public void onAdClose(String type) {
                LogUtils.e(TAG, "onAdDismissed type : " + type);

            }

            @Override
            public void onAdRenderSuccess(String type) {
                LogUtils.e(TAG, "onAdRenderSuccess type : " + type);
            }

            @Override
            public void onAdExposure(String type) {
                LogUtils.e(TAG, "onAdShow type : " + type);
            }
        });
    }

    private void renderAd(IExpressAdView view, boolean isAutoShow, long delayTime) {
        if (view != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.render();
                }
            }, delayTime);
        }
    }

    public void btnQuery(View view) {
        new Thread(() -> {
            List<PushEvent> eventList = DataManager.getAllPushEvent(this);
            Log.e(TAG, " size = " + eventList.size());
            for (PushEvent pushEvent : eventList) {
                Log.e(TAG, pushEvent.toString());
            }
        }).start();
    }
}