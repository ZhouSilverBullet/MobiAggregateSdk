package com.mobi.csj.wrapper;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.utils.LogUtils;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 22:27
 * @Dec 略
 */
public class InteractionExpressAdWrapper extends BaseAdWrapper implements TTAdNative.NativeExpressAdListener, TTNativeExpressAd.AdInteractionListener {
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    private BaseAdProvider mAdProvider;
    private Activity mActivity;
    private IInteractionAdListener mListener;

    private TTNativeExpressAd mTTAd;

    public InteractionExpressAdWrapper(BaseAdProvider baseAdProvider,
                                       Activity activity,
                                       LocalAdParams adParams,
                                       IInteractionAdListener listener) {

        mAdProvider = baseAdProvider;
        mActivity = activity;
        mAdParams = adParams;
        mListener = listener;

        mMobiCodeId = mAdParams.getMobiCodeId();

        if (baseAdProvider != null) {
            mProviderType = baseAdProvider.getProviderType();
        }
    }

    private void createInteractionAd() {
        String postId = mAdParams.getPostId();
        if (checkPostIdEmpty(mAdProvider, postId)) {
            return;
        }


        TTAdNative adNative = createAdNative(mActivity.getApplicationContext());
        //设置广告参数
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mAdParams.getPostId()) //广告位id
                .setSupportDeepLink(mAdParams.isSupportDeepLink())
                .setAdCount(getLoadCount(mAdParams.getAdCount())) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(mAdParams.getExpressViewWidth(), mAdParams.getExpressViewHeight()) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(mAdParams.getImageWidth(), mAdParams.getImageHeight())//这个参数设置即可，不影响个性化模板广告的size
                .build();

        //加载广告
        adNative.loadInteractionExpressAd(adSlot, this);

    }

    @Override
    public void onError(int code, String message) {
        localExecFail(mAdProvider, code, message);
    }

    @Override
    public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
        if (list == null || list.size() == 0) {
            localExecFail(mAdProvider, -100, "type TTNativeExpressAd  == null || type TTNativeExpressAd list.size() == 0 ");
            return;
        }

        if (isCancel()) {
            LogUtils.e(TAG, "Csj InteractionExpressAd isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "Csj InteractionExpressAd isTimeOut");
            localExecFail(mAdProvider, -104, " 访问超时 ");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.callbackInteractionLoad(mListener);
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        mTTAd = list.get(0);
        mTTAd.setExpressInteractionListener(this);
        if (mTTAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            //本地接口扔到Base里面去回调
            setAppDownloadListener(mListener);

            mTTAd.setDownloadListener(this);
        }
        mTTAd.render();//调用render开始渲染广告
    }

    @Override
    public void onAdDismiss() {
        if (mAdProvider != null) {
            mAdProvider.callbackInteractionClose(mListener);
        }
    }

    @Override
    public void onAdClicked(View view, int i) {
        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.callbackInteractionClick(mListener);
        }
    }

    @Override
    public void onAdShow(View view, int i) {
        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.callbackInteractionExposure(mListener);
        }
    }

    @Override
    public void onRenderFail(View view, String s, int i) {
//        localExecFail(mAdProvider, i, s);
        localRenderFail(mAdProvider, i, s);
    }

    @Override
    public void onRenderSuccess(View view, float v, float v1) {
        if (mTTAd != null) {
            mTTAd.showInteractionExpressAd(mActivity);
        }
    }

    @Override
    public void run() {
        createInteractionAd();
    }
}
