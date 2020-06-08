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
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.utils.LogUtils;

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
    private final LocalAdParams mParams;
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
                                  LocalAdParams params,
                                  IExpressListener listener) {
        mContext = context;
        mAdProvider = adProvider;
        mParams = params;
        mCodeId = params.getPostId();
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

    private void createNativeExpressAD() {
        String postId = mParams.getPostId();
        if (TextUtils.isEmpty(postId)) {
            localExecFail(mAdProvider, -101,
                    "mobi 后台获取的 postId 不正确 或者 postId == null");

            return;
        }


        TTAdNative mTTAdNative = createAdNative(mContext.getApplicationContext());

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

        if (mViewContainer != null) {
            mViewContainer.removeAllViews();
        }

        localExecFail(mAdProvider, i, s + " postId: " + mParams.getPostId());

    }

    @Override
    public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
        if (list == null || list.size() == 0) {
            localExecFail(mAdProvider, -100, "type TTNativeExpressAd  == null || type TTNativeExpressAd list.size() == 0 ");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.callbackExpressLoad(mListener);
        }

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "CsjNativeExpressAd load isCancel");
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

//        localExecFail(mAdProvider, i, s);
        localRenderFail(mAdProvider, i, s);
    }

    @Override
    public void onRenderSuccess(View view, float v, float v1) {

        //渲染前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "CsjNativeExpressAd onRenderSuccess isCancel");
            return;
        }

        setExecSuccess(true);
        localExecSuccess(mAdProvider);

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
        createNativeExpressAD();
    }
}
