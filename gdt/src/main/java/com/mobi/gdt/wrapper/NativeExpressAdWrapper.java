package com.mobi.gdt.wrapper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.IExpressAdView;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.utils.LogUtils;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 17:00
 * @Dec 略
 */
public class NativeExpressAdWrapper extends BaseAdWrapper implements IExpressAdView, NativeExpressAD.NativeExpressADListener {
    public static final String TAG = "GdtNativeExpressAd";

    private final BaseAdProvider mAdProvider;
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    Activity mContext;
    ViewGroup mViewContainer;
    IExpressListener mListener;
    private NativeExpressAD mNativeExpressAD;
    private List<NativeExpressADView> mNativeExpressADViews;

    public NativeExpressAdWrapper(BaseAdProvider adProvider,
                                  Activity context,
                                  ViewGroup viewContainer,
                                  LocalAdParams params,
                                  IExpressListener listener) {
        mContext = context;
        mAdProvider = adProvider;
        mAdParams = params;
        mViewContainer = viewContainer;
        mListener = listener;

        mMobiCodeId = mAdParams.getMobiCodeId();
    }

    /**
     * 创建对应的对象
     */
    private void createNativeExpressAD() {

        String postId = mAdParams.getPostId();
        if (checkPostIdEmpty(mAdProvider, postId)) {
            return;
        }

        mNativeExpressAD = new NativeExpressAD(mContext,
                getADSize(true, mAdParams.getExpressViewWidth(), mAdParams.getExpressViewHeight()),
                mAdParams.getPostId(),
                this);

        mNativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS)//设置什么网络情况下可以自动播放视频
                .setAutoPlayMuted(true)//设置自动播放视频时是否静音
                .build());
//        nativeExpressAD.setMaxVideoDuration(15);//设置视频最大时长
        mNativeExpressAD.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO);
        //记载数量
        mNativeExpressAD.loadAD(getLoadCount(mAdParams.getAdCount()));
    }


    @Override
    public void onADLoaded(List<NativeExpressADView> list) {
        //加载广告成功

        if (list == null || list.size() == 0) {
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_LOAD_EMPTY_ERROR, "没有对应的广告");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad(getStyleType());
        }

        //load前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "GdtNativeExpressAd load isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "GdtNativeExpressAd load isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }


        setExecSuccess(true);
        localExecSuccess(mAdProvider);

//        IExpressAdView expressAdView = null;



//        if (mAdParams.isAutoShowAd()) {
//            for (NativeExpressADView nativeExpressADView : list) {
//                if (nativeExpressADView != null) {
//                    if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
//                        //如果是视频广告 可添加视频播放监听
//                        nativeExpressADView.setMediaListener(null);
//                    }
//                    nativeExpressADView.render();
//
//
//                    LogUtils.e(TAG, "GdtNativeExpressAd onRenderSuccess");
//
//                    // 需要保证 View 被绘制的时候是可见的，否则将无法产生曝光和收益。
//                    // 有的时候还不显示广告，闪的一下就过去了的bug
//                    mViewContainer.addView(nativeExpressADView);
//                }
//            }
//        } else {
//            LogUtils.e(TAG, "GdtNativeExpressAd load not show");
//            for (NativeExpressADView nativeExpressADView : list) {
//                mViewContainer.addView(nativeExpressADView);
//            }
//
//            expressAdView = new GdtExpressAdViewImpl(list);
//        }
        mNativeExpressADViews = list;

        if (mViewContainer.getChildCount() > 0) {
            mViewContainer.removeAllViews();
        }

        for (NativeExpressADView nativeExpressADView : list) {
            mViewContainer.addView(nativeExpressADView);
        }

        if (mAdProvider != null) {
            mAdProvider.callbackExpressLoad(mListener, this, mAdParams.isAutoShowAd());
        }
    }

    @Override
    public void onRenderFail(NativeExpressADView nativeExpressADView) {
        if (mViewContainer != null) {
            mViewContainer.removeAllViews();
        }
        //广告渲染失败
        localRenderFail(mAdProvider, MobiConstantValue.ERROR.TYPE_RENDER_ERROR,
                MobiConstantValue.GDT_ERROR_RENDER_MESSAGE);
    }

    @Override
    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

        //广告渲染成功
        if (mViewContainer.getVisibility() != View.VISIBLE) {
            mViewContainer.setVisibility(View.VISIBLE);
        }
        //防止广告重叠显示
        if (mViewContainer.getChildCount() > 0) {
            mViewContainer.removeAllViews();
        }

        mViewContainer.addView(nativeExpressADView);

        if (mAdProvider != null) {
            mAdProvider.callbackExpressRenderSuccess(mListener);
            mAdProvider.trackRenderSuccess(getStyleType());
        }

    }

    @Override
    public void onADExposure(NativeExpressADView nativeExpressADView) {
        //广告曝光
        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow(getStyleType());
            mAdProvider.callbackExpressShow(mListener);
        }
    }

    @Override
    public void onADClicked(NativeExpressADView nativeExpressADView) {
        //广告被点击
        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick(getStyleType());
            mAdProvider.callbackExpressClick(mListener);
        }
    }

    @Override
    public void onADClosed(NativeExpressADView nativeExpressADView) {

        if (mAdProvider != null) {
            mAdProvider.trackEventClose(getStyleType());
            mAdProvider.callbackExpressDismissed(mListener);
        }
    }

    @Override
    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
        if (mAdProvider != null) {
            mAdProvider.callbackExpressLeftApplication(mListener);
        }
    }

    @Override
    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {
        if (mAdProvider != null) {
            mAdProvider.callbackExpressOpenOverlay(mListener);
            mAdProvider.trackGdtOpenOverlay(getStyleType());
        }
    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
        if (mAdProvider != null) {
            mAdProvider.callbackExpressCloseOverlay(mListener);
            mAdProvider.trackGdtCloseOverlay(getStyleType());
        }
    }

    @Override
    public void onNoAD(AdError adError) {
        //加载失败
        localExecFail(mAdProvider, adError.getErrorCode(), adError.getErrorMsg());
    }

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

    @Override
    public void render() {
        if (mNativeExpressADViews == null) {
            return;
        }

        for (View view : mNativeExpressADViews) {
            if (view instanceof NativeExpressADView) {
                ((NativeExpressADView) view).render();
            }
        }

        if (mAdProvider != null) {
            mAdProvider.trackStartShow(getStyleType());
        }
    }

    @Override
    public void onDestroy() {
        mContext = null;
        if (mNativeExpressADViews != null) {
            mNativeExpressADViews.clear();
        }
        if (mNativeExpressAD != null) {
            mNativeExpressAD = null;
        }
    }
}
