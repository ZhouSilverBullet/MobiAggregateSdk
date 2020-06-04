package com.mobi.csj.wrapper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.listener.IInteractionAdListener;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 22:27
 * @Dec 略
 */
public class InteractionExpressAdWrapper extends BaseAdWrapper implements TTAdNative.NativeExpressAdListener, TTNativeExpressAd.AdInteractionListener, TTAppDownloadListener {
    private final boolean mSupportDeepLink;
    private final int mLoadCount;
    private final float mExpressViewWidth;
    private final float mExpressViewHeight;
    private final ViewGroup mViewContainer;
    private String mProviderType;
    private BaseAdProvider mAdProvider;
    private Activity mActivity;
    private String mCodeId;
    private IInteractionAdListener mListener;

    private TTNativeExpressAd mTTAd;

    public InteractionExpressAdWrapper(BaseAdProvider baseAdProvider,
                                       Activity activity,
                                       String codeId,
                                       boolean supportDeepLink,
                                       ViewGroup viewContainer,
                                       int loadCount,
                                       float expressViewWidth,
                                       float expressViewHeight,
                                       IInteractionAdListener listener) {

        mAdProvider = baseAdProvider;
        mActivity = activity;
        mCodeId = codeId;

        mSupportDeepLink = supportDeepLink;
        mViewContainer = viewContainer;
        mLoadCount = loadCount;
        mExpressViewWidth = expressViewWidth;
        mExpressViewHeight = expressViewHeight;

        mListener = listener;

        if (baseAdProvider != null) {
            mProviderType = baseAdProvider.getProviderType();
        }
    }

    public void createInteractionAd() {

        TTAdNative adNative = createAdNative(mActivity.getApplicationContext());
        //设置广告参数
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mCodeId) //广告位id
                .setSupportDeepLink(mSupportDeepLink)
                .setAdCount(getLoadCount(mLoadCount)) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(mExpressViewWidth, mExpressViewHeight) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响个性化模板广告的size
                .build();

        //加载广告
        adNative.loadInteractionExpressAd(adSlot, this);

    }

    @Override
    public void onError(int code, String message) {
        if (mViewContainer != null) {
            mViewContainer.removeAllViews();
        }
    }

    @Override
    public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
        if (list == null || list.size() == 0) {
            if (mAdProvider != null) {
                mAdProvider.callbackInteractionFail(-100, "type TTNativeExpressAd  == null || type TTNativeExpressAd list.size() == 0 ", mListener);
            }
            return;
        }

        mTTAd = list.get(0);
        mTTAd.setExpressInteractionListener(this);
        if (mTTAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
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
            mAdProvider.callbackInteractionClick(mListener);
        }
    }

    @Override
    public void onAdShow(View view, int i) {
        if (mAdProvider != null) {
            mAdProvider.callbackInteractionShow(mListener);
        }
    }

    @Override
    public void onRenderFail(View view, String s, int i) {
        if (mAdProvider != null) {
            mAdProvider.callbackInteractionFail(i, s, mListener);
        }
    }

    @Override
    public void onRenderSuccess(View view, float v, float v1) {
        mTTAd.showInteractionExpressAd(mActivity);
    }

    @Override
    public void onIdle() {
        if (mListener != null) {
            mListener.onIdle(mProviderType);
        }
    }

    @Override
    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
        if (mListener != null) {
            mListener.onDownloadActive(totalBytes, currBytes, fileName, appName);
        }
    }

    @Override
    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
        if (mListener != null) {
            mListener.onDownloadPaused(totalBytes, currBytes, fileName, appName);
        }
    }

    @Override
    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
        if (mListener != null) {
            mListener.onDownloadFailed(totalBytes, currBytes, fileName, appName);
        }
    }

    @Override
    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
        if (mListener != null) {
            mListener.onDownloadFinished(totalBytes, fileName, appName);
        }
    }

    @Override
    public void onInstalled(String fileName, String appName) {
        if (mListener != null) {
            mListener.onInstalled(fileName, appName);
        }
    }
}
