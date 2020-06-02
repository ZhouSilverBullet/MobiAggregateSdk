package com.mobi.aggsdk;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.mobi.csj.CsjSession;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

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
}