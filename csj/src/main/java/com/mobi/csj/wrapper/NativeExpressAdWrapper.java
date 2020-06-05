package com.mobi.csj.wrapper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.mobi.core.AdParams;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.listener.IExpressListener;
import com.mobi.csj.CsjSession;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 18:03
 * @Dec csj wrapper
 */
public class NativeExpressAdWrapper extends BaseAdWrapper implements TTAdNative.NativeExpressAdListener, TTNativeExpressAd.AdInteractionListener, TTAppDownloadListener {
    private final BaseAdProvider mAdProvider;
    private final AdParams mParams;
    private String mProviderType;
    Activity mContext;
    String mCodeId;
    boolean mSupportDeepLink;
    ViewGroup mViewContainer;
    int mADViewWidth;
    int mADViewHeight;
    int mLoadCount;
    IExpressListener mListener;

    private TTNativeExpressAd mTTNativeExpressAd;

    public NativeExpressAdWrapper(BaseAdProvider adProvider,
                                  Activity context,
                                  ViewGroup viewContainer,
                                  AdParams params,
                                  IExpressListener listener) {
        mContext = context;
        mAdProvider = adProvider;
        mParams = params;
        mCodeId = params.getCodeId();
        mSupportDeepLink = params.isSupportDeepLink();
        mViewContainer = viewContainer;
        mADViewWidth = params.getExpressViewWidth();
        mADViewHeight = params.getExpressViewHeight();
        mLoadCount = params.getAdCount();
        mListener = listener;

        if (mAdProvider != null) {
            mProviderType = mAdProvider.getProviderType();
        }
    }

    public void createNativeExpressAD() {
        TTAdNative mTTAdNative = createAdNative(mContext.getApplicationContext());

//        if (mHeightAuto) {
//            ADViewHeight = 0;
//        }

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mParams.getPostId())
                .setSupportDeepLink(mParams.isSupportDeepLink())
                .setAdCount(getLoadCount(mParams.getAdCount()))
                .setExpressViewAcceptedSize(mParams.getExpressViewWidth(), mParams.getExpressViewHeight())
                .setImageAcceptedSize(mParams.getImageWidth(), mParams.getExpressViewHeight())
                .build();

        mTTAdNative.loadNativeExpressAd(adSlot, this);
    }


    @Override
    public void onError(int i, String s) {
//        AdStatistical.trackAD(mContext, mProviderType, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
//        mBearingView.removeAllViews();
//        if (mListener != null) {
//            mListener.onLoadFailed(mProviderType, i, s);
//        }

        if (mViewContainer != null) {
            mViewContainer.removeAllViews();
        }

        if (mAdProvider != null) {
            mAdProvider.callbackExpressLoadFailed(i, s, mListener);
        }
    }

    @Override
    public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
        if (list == null || list.size() == 0) {
            if (mAdProvider != null) {
                mAdProvider.callbackExpressLoadFailed(-100, "type TTNativeExpressAd  == null || type TTNativeExpressAd list.size() == 0 ", mListener);
            }
            return;
        }
        mTTNativeExpressAd = list.get(0);
        mTTNativeExpressAd.setExpressInteractionListener(this);
        //这里default
        if (mTTNativeExpressAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            setAppDownloadListener(mListener);
            mTTNativeExpressAd.setDownloadListener(this);
        }
        mTTNativeExpressAd.render();

//        recordRenderSuccess(mProviderType);
//        renderTTAD();

    }

    @Override
    public void onAdDismiss() {
        if (mAdProvider != null) {
            mAdProvider.callbackExpressDismissed(mListener);
        }
    }

    @Override
    public void onAdClicked(View view, int i) {
        if (mAdProvider != null) {
            mAdProvider.callbackExpressClick(mListener);
        }
    }

    @Override
    public void onAdShow(View view, int i) {
        if (mAdProvider != null) {
            mAdProvider.callbackExpressShow(mListener);
        }
    }

    @Override
    public void onRenderFail(View view, String s, int i) {
        if (mAdProvider != null) {
            mAdProvider.callbackExpressLoadFailed(i, s, mListener);
        }
    }

    @Override
    public void onRenderSuccess(View view, float v, float v1) {
//        Log.e("MainActivity", "" + (view == mTTNativeExpressAd.getExpressAdView()));
//        mViewContainer.addView(mTTNativeExpressAd.getExpressAdView());
        if (mViewContainer != null) {
            mViewContainer.addView(view);
        }

//        if (mTTNativeExpressAd != null) {
//            mTTNativeExpressAd.render();
//        }

        if (mAdProvider != null) {
            mAdProvider.callbackExpressRenderSuccess(mListener);
        }
    }
}
