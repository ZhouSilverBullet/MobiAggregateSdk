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
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.utils.LogUtils;
import com.mobi.csj.impl.CsjExpressAdViewImpl;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 18:03
 * @Dec csj wrapper
 */
public class NativeExpressAdWrapper extends BaseAdWrapper implements TTAdNative.NativeExpressAdListener, TTNativeExpressAd.AdInteractionListener, TTAppDownloadListener {
    public static final String TAG = "CsjNativeExpressAd";
    private final BaseAdProvider mAdProvider;
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    Activity mContext;
    ViewGroup mViewContainer;
    IExpressListener mListener;

    public NativeExpressAdWrapper(BaseAdProvider adProvider,
                                  Activity context,
                                  ViewGroup viewContainer,
                                  LocalAdParams adParams,
                                  IExpressListener listener) {
        mContext = context;
        mAdProvider = adProvider;
        mAdParams = adParams;
        mViewContainer = viewContainer;
        mListener = listener;

        mMobiCodeId = mAdParams.getMobiCodeId();

        if (mAdProvider != null) {
            mProviderType = mAdProvider.getProviderType();
        }
    }

    private void createNativeExpressAD() {
        String postId = mAdParams.getPostId();
        if (checkPostIdEmpty(mAdProvider, postId)) {
            if (mAdProvider != null) {
                mAdProvider.trackEventError(getStyleType(), MobiConstantValue.ERROR.TYPE_ERROR,
                        0, "", "postId 获取失败或者为空");
            }
            return;
        }


        TTAdNative mTTAdNative = createAdNative(mContext.getApplicationContext());

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mAdParams.getPostId())
                .setSupportDeepLink(mAdParams.isSupportDeepLink())
                .setAdCount(getLoadCount(mAdParams.getAdCount()))
                .setExpressViewAcceptedSize(mAdParams.getExpressViewWidth(), mAdParams.getExpressViewHeight())
                .setImageAcceptedSize(mAdParams.getImageWidth(), mAdParams.getExpressViewHeight())
                .build();

        mTTAdNative.loadNativeExpressAd(adSlot, this);
    }


    @Override
    public void onError(int code, String message) {

        if (mAdProvider != null) {
            mAdProvider.trackEventError(getStyleType(), MobiConstantValue.ERROR.TYPE_ERROR, code, message);
        }

        if (mViewContainer != null) {
            mViewContainer.removeAllViews();
        }

        localExecFail(mAdProvider, code, message);

    }

    @Override
    public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {

        if (list == null || list.size() == 0) {
            localExecFail(mAdProvider, -100, "type TTNativeExpressAd  == null || type TTNativeExpressAd list.size() == 0 ");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad(getStyleType());
        }

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            if (mAdProvider != null) {
                mAdProvider.trackEventError(getStyleType(), MobiConstantValue.ERROR.TYPE_CANCEL, 0, "");
            }
            LogUtils.e(TAG, "CsjNativeExpressAd load isCancel");
            return;
        }

        if (isTimeOut()) {
            if (mAdProvider != null) {
                mAdProvider.trackEventError(getStyleType(), MobiConstantValue.ERROR.TYPE_TIMEOUT, 0, "");
            }
            LogUtils.e(TAG, "CsjNativeExpressAd load isTimeOut");
            localExecFail(mAdProvider, -104, " 访问超时 ");
            return;
        }


        //任务执行成功
        setExecSuccess(true);
        localExecSuccess(mAdProvider);

        CsjExpressAdViewImpl expressAdView = null;
        if (mAdParams.isAutoShowAd()) {
            for (TTNativeExpressAd ttNativeExpressAd : list) {
                ttNativeExpressAd.setExpressInteractionListener(this);
                //这里default
                if (ttNativeExpressAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    setAppDownloadListener(mListener);
                    ttNativeExpressAd.setDownloadListener(this);
                }
                ttNativeExpressAd.render();
            }
        } else {
            LogUtils.e(TAG, "CsjNativeExpressAd load not show");
            for (TTNativeExpressAd ttNativeExpressAd : list) {
                ttNativeExpressAd.setExpressInteractionListener(this);
                //这里default
                if (ttNativeExpressAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    setAppDownloadListener(mListener);
                    ttNativeExpressAd.setDownloadListener(this);
                }
            }
            expressAdView = new CsjExpressAdViewImpl(list);
        }

        if (mAdProvider != null) {
            mAdProvider.callbackExpressLoad(mListener, expressAdView, mAdParams.isAutoShowAd());
        }
    }

    @Override
    public void onAdDismiss() {
        if (mAdProvider != null) {
            mAdProvider.trackEventClose(getStyleType());
            mAdProvider.callbackExpressDismissed(mListener);
        }
    }

    @Override
    public void onAdClicked(View view, int i) {
        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick(getStyleType());
            mAdProvider.callbackExpressClick(mListener);
        }
    }

    @Override
    public void onAdShow(View view, int i) {
        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow(getStyleType());
            mAdProvider.callbackExpressShow(mListener);
        }
    }

    @Override
    public void onRenderFail(View view, String message, int code) {
        if (mAdProvider != null) {
            mAdProvider.trackEventError(getStyleType(), MobiConstantValue.ERROR.TYPE_RENDER_ERROR, code, message);
        }
//        localExecFail(mAdProvider, code, message);
        localRenderFail(mAdProvider, code, message);
    }

    @Override
    public void onRenderSuccess(View view, float v, float v1) {

        if (mViewContainer.getVisibility() != View.VISIBLE) {
            mViewContainer.setVisibility(View.VISIBLE);
        }
        //防止广告重叠显示
        if (mViewContainer.getChildCount() > 0) {
            mViewContainer.removeAllViews();
        }
        if (mViewContainer != null) {
            mViewContainer.addView(view);
        }

        if (mAdProvider != null) {
            mAdProvider.callbackExpressRenderSuccess(mListener);
        }
    }


    /**
     * 创建任务执行
     */
    @Override
    public void run() {
        if (mAdProvider != null) {
            mAdProvider.trackEventStart(getStyleType());
        }
        createNativeExpressAD();
    }

    @Override
    public int getStyleType() {
        return MobiConstantValue.STYLE.NATIVE_EXPRESS;
    }
}
