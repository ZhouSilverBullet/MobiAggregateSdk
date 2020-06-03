package com.mobi.gdt.wrapper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.listener.IExpressListener;
import com.mobi.gdt.GdtSession;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 17:00
 * @Dec 略
 */
public class NativeExpressAdWrapper implements NativeExpressAD.NativeExpressADListener {
    private final BaseAdProvider mAdProvider;
    Activity mContext;
    String mCodeId;
    boolean mSupportDeepLink;
    ViewGroup mViewContainer;
    int mADViewWidth;
    int mADViewHeight;
    int mLoadCount;
    IExpressListener mListener;
    private NativeExpressAD mNativeExpressAD;

    public NativeExpressAdWrapper(Activity context,
                                  BaseAdProvider adProvider,
                                  String codeId,
                                  boolean supportDeepLink,
                                  ViewGroup viewContainer,
                                  int ADViewWidth,
                                  int ADViewHeight,
                                  int loadCount,
                                  IExpressListener listener) {
        mContext = context;
        mAdProvider = adProvider;
        mCodeId = codeId;
        mSupportDeepLink = supportDeepLink;
        mViewContainer = viewContainer;
        mADViewWidth = ADViewWidth;
        mADViewHeight = ADViewHeight;
        mLoadCount = loadCount;
        mListener = listener;
    }

    /**
     * 创建对应的对象
     */
    public void createNativeExpressAD() {
        mNativeExpressAD = new NativeExpressAD(mContext,
                getADSize(true, mADViewWidth, mADViewHeight),
                getAppId(),
                mCodeId,
                this);

        mNativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS)//设置什么网络情况下可以自动播放视频
                .setAutoPlayMuted(true)//设置自动播放视频时是否静音
                .build());
//        nativeExpressAD.setMaxVideoDuration(15);//设置视频最大时长
        mNativeExpressAD.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO);
        //记载数量
        mNativeExpressAD.loadAD(getLoadCount(mLoadCount));
    }

    private int getLoadCount(int loadCount) {
        int count = 1;

        if (loadCount > 0) {
            count = loadCount;
        }
        return count;
    }

    private ADSize getADSize(boolean mHeightAuto, int ADViewWidth, int ADViewHeight) {
        if (mHeightAuto || ADViewHeight <= 0) {
            return new ADSize((int) ADViewWidth, ADSize.AUTO_HEIGHT);
        }
        return new ADSize((int) ADViewWidth, (int) ADViewHeight);
    }

    private String getAppId() {
        return GdtSession.get().getAppId();
    }


    @Override
    public void onADLoaded(List<NativeExpressADView> list) {
        //加载广告成功
        if (list == null || list.size() == 0) {
            return;
        }

        NativeExpressADView nativeExpressADView = list.get(0);
        if (nativeExpressADView != null) {
            if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                //如果是视频广告 可添加视频播放监听
                nativeExpressADView.setMediaListener(null);
            }
            nativeExpressADView.render();
//                            recordRenderSuccess(mProviderType);
            if (mViewContainer.getChildCount() > 0) {
                mViewContainer.removeAllViews();
            }

            if (mViewContainer.getVisibility() != View.VISIBLE) {
                mViewContainer.setVisibility(View.VISIBLE);
            }

            mViewContainer.addView(nativeExpressADView);
        }
    }

    @Override
    public void onRenderFail(NativeExpressADView nativeExpressADView) {
        //广告渲染失败
        if (mAdProvider != null) {
            mAdProvider.callbackExpressLoadFailed(MobiConstantValue.GDT_ERROR_RENDER_CODE,
                    MobiConstantValue.GDT_ERROR_RENDER_MESSAGE, mListener);
        }
    }

    @Override
    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
        //广告渲染成功
//                        if (firstCome) {
//                            renderGDTAD();
//                            firstCome = false;
//                        }
        if (mAdProvider != null) {
            mAdProvider.callbackExpressRenderSuccess(mListener);
        }

    }

    @Override
    public void onADExposure(NativeExpressADView nativeExpressADView) {
        //广告曝光
        if (mAdProvider != null) {
            mAdProvider.callbackExpressShow(mListener);
        }
    }

    @Override
    public void onADClicked(NativeExpressADView nativeExpressADView) {
        //广告被点击
        if (mAdProvider != null) {
            mAdProvider.callbackExpressClick(mListener);
        }
//                        AdStatistical.trackAD(mContext, mProviderType, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
    }

    @Override
    public void onADClosed(NativeExpressADView nativeExpressADView) {
        //广告关闭
//                        if (mBearingView != null && mBearingView.getChildCount() > 0) {
//                            mBearingView.removeAllViews();
//                            mBearingView.setVisibility(View.GONE);
//                        }

        if (mAdProvider != null) {
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
        }
    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
        if (mAdProvider != null) {
            mAdProvider.callbackExpressCloseOverlay(mListener);
        }
    }

    @Override
    public void onNoAD(AdError adError) {
//                        AdStatistical.trackAD(mContext, mProviderType, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
        //加载失败
        if (mAdProvider != null) {
            mAdProvider.callbackExpressLoadFailed(adError.getErrorCode(), adError.getErrorMsg(), mListener);
        }
    }
}
