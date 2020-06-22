package com.mobi.gdt.wrapper;

import android.app.Activity;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.listener.IFullScreenVideoAdListener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 18:44
 * @Dec gdt没有这个实现
 */
public class GdtFullScreenVideoAdWrapper extends BaseAdWrapper {

    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    private String mProviderType;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    IFullScreenVideoAdListener mListener;

    public GdtFullScreenVideoAdWrapper(BaseAdProvider adProvider,
                                    Activity activity,
                                    LocalAdParams adParams,
                                    IFullScreenVideoAdListener listener) {
        mAdProvider = adProvider;
        mActivity = activity;
        mAdParams = adParams;
        mListener = listener;
        mMobiCodeId = mAdParams.getMobiCodeId();
        if (mAdProvider != null) {
            mProviderType = mAdProvider.getProviderType();
        }
    }

    private void createFullScreenVideoAd() {
//        String postId = mAdParams.getPostId();
//        if (TextUtils.isEmpty(postId)) {
//            localExecFail(mAdProvider, -101,
//                    "mobi 后台获取的 postId 不正确 或者 postId == null");
//            return;
//        }
        if (isCancel()) {
            return;
        }

        localExecFail(mAdProvider, -102, "gdt 没有这个实现");
        //todo 没有这个实现
    }

    @Override
    public void run() {
        createFullScreenVideoAd();
    }

    @Override
    public int getStyleType() {
        return MobiConstantValue.STYLE.FULL_SCREEN;
    }
}
