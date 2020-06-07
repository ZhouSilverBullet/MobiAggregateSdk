package com.mobi.gdt.wrapper;

import android.app.Activity;
import android.text.TextUtils;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.utils.LogUtils;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.util.AdError;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 21:18
 * @Dec 略
 */
public class UnifiedInterstitialADWrapper extends BaseAdWrapper implements UnifiedInterstitialADListener {
    private final LocalAdParams mAdParams;
    private String mProviderType;
    private BaseAdProvider mBaseAdProvider;
    private Activity mActivity;
    private IInteractionAdListener mListener;

    private UnifiedInterstitialAD iad;

    public UnifiedInterstitialADWrapper(BaseAdProvider baseAdProvider,
                                        Activity activity,
                                        LocalAdParams adParams,
                                        IInteractionAdListener listener) {
        mBaseAdProvider = baseAdProvider;
        mActivity = activity;
        mAdParams = adParams;
        mListener = listener;
        if (mBaseAdProvider != null) {
            mProviderType = mBaseAdProvider.getProviderType();
        }
    }

    private void createInterstitialAD() {
        String postId = mAdParams.getPostId();
        if (TextUtils.isEmpty(postId)) {
            localExecFail(mBaseAdProvider, -101,
                    "mobi 后台获取的 postId 不正确 或者 postId == null");

//            if (mAdProvider != null) {
//                mAdProvider.callbackExpressLoadFailed();
//            }
            return;
        }

        iad = new UnifiedInterstitialAD(mActivity, getAppId(), mAdParams.getPostId(), this);
        iad.loadAD();
    }

    @Override
    public void onADReceive() {

        if (isCancel()) {
            LogUtils.e(TAG, "Gdt UnifiedInterstitialAD isCancel");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mBaseAdProvider);

        if (iad != null) {
            iad.showAsPopupWindow();
        }

        if (mBaseAdProvider != null) {
            mBaseAdProvider.callbackInteractionReceive(mListener);
        }
    }

    @Override
    public void onVideoCached() {

        if (iad != null) {
            iad.showAsPopupWindow();
        }

        if (mBaseAdProvider != null) {
            mBaseAdProvider.callbackInteractionCached(mListener);
        }
    }

    @Override
    public void onNoAD(AdError adError) {
        if (mBaseAdProvider != null) {
            if (adError == null) {
                localExecFail(mBaseAdProvider, -100, "onNoAD 没有数据 adError == null");
//                mBaseAdProvider.callbackInteractionFail(-100, "onNoAD 没有数据 adError == null", mListener);
            } else {
                localExecFail(mBaseAdProvider, adError.getErrorCode(), adError.getErrorMsg());
//                mBaseAdProvider.callbackInteractionFail(adError.getErrorCode(), adError.getErrorMsg(), mListener);
            }
        }
    }

    @Override
    public void onADOpened() {

        if (mBaseAdProvider != null) {
            mBaseAdProvider.callbackInteractionOpened(mListener);
        }

    }

    @Override
    public void onADExposure() {
        if (mBaseAdProvider != null) {
            mBaseAdProvider.callbackInteractionExposure(mListener);
        }
    }

    @Override
    public void onADClicked() {
        if (mBaseAdProvider != null) {
            mBaseAdProvider.callbackInteractionClick(mListener);
        }
    }

    @Override
    public void onADLeftApplication() {
        if (mListener != null) {
            mListener.onADLeftApplication(mProviderType);
        }
    }

    @Override
    public void onADClosed() {
        if (mBaseAdProvider != null) {
            mBaseAdProvider.callbackInteractionClose(mListener);
        }
    }

    @Override
    public void run() {
        createInterstitialAD();
    }
}
