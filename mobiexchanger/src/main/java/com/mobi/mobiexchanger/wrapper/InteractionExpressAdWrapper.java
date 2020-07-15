package com.mobi.mobiexchanger.wrapper;

import android.app.Activity;

import com.mobi.adsdk.MobiSdk;
import com.mobi.adsdk.ads.MobiAdSlot;
import com.mobi.adsdk.ads.interstital.MobiInterstitialAdListener;
import com.mobi.adsdk.ads.interstital.MobiInterstitialListener;
import com.mobi.adsdk.net.ads.interstital.InterstitalAd;
import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.InteractionAdView;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.utils.LogUtils;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 22:27
 * @Dec 略
 */
public class InteractionExpressAdWrapper extends BaseAdWrapper implements InteractionAdView, MobiInterstitialAdListener, MobiInterstitialListener {
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    private BaseAdProvider mAdProvider;
    private Activity mActivity;
    private IInteractionAdListener mListener;
    private List<InterstitalAd> mTTNativeExpressAds;

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


        MobiAdSlot slot = new MobiAdSlot.Builder()
                .setCodeId(postId)
                .build();

        MobiSdk.loadInterstitialAd(mActivity, slot, this);

    }

    @Override
    public void onError(String strCode, String message) {
        int code = 0;
        try {
            code = Integer.parseInt(strCode);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        localExecFail(mAdProvider, code, message);
    }

    @Override
    public void onNativeExpressAdLoad(List<InterstitalAd> list) {
        if (list == null || list.size() == 0) {
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_LOAD_EMPTY_ERROR, "type InterstitalAd  == null || type InterstitalAd list.size() == 0");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad();
        }

        if (isCancel()) {
            LogUtils.e(TAG, "mobi InteractionExpressAd isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "mobi InteractionExpressAd isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        mTTNativeExpressAds = list;
        for (InterstitalAd ttNativeExpressAd : mTTNativeExpressAds) {
            ttNativeExpressAd.setListener(this);
        }

        if (mAdProvider != null) {
            mAdProvider.callbackInteractionLoad(mListener, this, mAdParams.isAutoShowAd());
        }

    }

    @Override
    public void onAdDismiss() {
        if (mAdProvider != null) {
            mAdProvider.trackEventClose();
            mAdProvider.callbackInteractionClose(mListener);
        }
    }

    @Override
    public void onAdClicked() {
        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick();
            mAdProvider.callbackInteractionClick(mListener);
        }
    }

    @Override
    public void onAdShow() {
        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow();
            mAdProvider.callbackInteractionExposure(mListener);
        }
    }

    @Override
    public void onRenderFail() {
        localRenderFail(mAdProvider, 0, "渲染失败");
    }

    @Override
    public void onRenderSuccess() {
        if (mTTNativeExpressAds != null) {
            for (InterstitalAd ttNativeExpressAd : mTTNativeExpressAds) {
                ttNativeExpressAd.showInteractionExpressAd(mActivity);
            }
        }

        if (mAdProvider != null) {
            mAdProvider.trackRenderSuccess();
        }
    }

    @Override
    public void run() {
        if (mAdProvider != null) {
            mAdProvider.trackEventStart();
        }
        createInteractionAd();
    }

    @Override
    public int getStyleType() {
        return MobiConstantValue.STYLE.INTERACTION_EXPRESS;
    }

    @Override
    public void show() {
        if (mTTNativeExpressAds == null) {
            return;
        }

//        for (InterstitalAd ttFullScreenVideoAd : mTTNativeExpressAds) {
//            ttFullScreenVideoAd.render();
//        }

        if (mAdProvider != null) {
            mAdProvider.trackStartShow();
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        if (mTTNativeExpressAds != null) {
            mTTNativeExpressAds.clear();
        }
    }


}
