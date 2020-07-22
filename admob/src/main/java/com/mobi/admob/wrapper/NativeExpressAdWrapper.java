package com.mobi.admob.wrapper;

import android.content.Context;
import android.view.ViewGroup;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.ExpressAdView;
import com.mobi.core.listener.IExpressListener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 18:03
 * @Dec csj wrapper
 */
public class NativeExpressAdWrapper extends BaseAdWrapper implements ExpressAdView {
    public static final String TAG = "CsjNativeExpressAd";
    private final BaseAdProvider mAdProvider;
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    Context mContext;
    ViewGroup mViewContainer;
    IExpressListener mListener;

    public NativeExpressAdWrapper(BaseAdProvider adProvider,
                                  Context context,
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
            return;
        }

        localExecFail(mAdProvider, -102, "AdMob 没有这个实现");

    }


    /**
     * 创建任务执行
     */
    @Override
    public void run() {
        if (mAdProvider != null) {
            mAdProvider.trackEventStart();
        }
        createNativeExpressAD();
    }

    @Override
    public int getStyleType() {
        return MobiConstantValue.STYLE.NATIVE_EXPRESS;
    }


    @Override
    public void show() {

    }
}
