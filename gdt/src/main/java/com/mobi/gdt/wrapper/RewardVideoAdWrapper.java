package com.mobi.gdt.wrapper;

import android.app.Activity;
import android.os.SystemClock;
import android.widget.Toast;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.feature.IAdView;
import com.mobi.core.feature.RewardAdView;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.utils.LogUtils;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 11:29
 * @Dec 略
 */
public class RewardVideoAdWrapper extends BaseAdWrapper implements RewardAdView, RewardVideoADListener {
    private final LocalAdParams mAdParams;
    private final String mMobiCodeId;
    BaseAdProvider mAdProvider;
    Activity mActivity;
    IRewardAdListener mListener;

    private RewardVideoAD rewardVideoAD;

    public RewardVideoAdWrapper(BaseAdProvider adProvider,
                                Activity activity,
                                LocalAdParams adParams,
                                IRewardAdListener listener) {
        mAdProvider = adProvider;
        mActivity = activity;
        mAdParams = adParams;
        mListener = listener;
        mMobiCodeId = mAdParams.getMobiCodeId();
    }

    private void createRewardVideoAd() {
        String postId = mAdParams.getPostId();
        if (checkPostIdEmpty(mAdProvider, postId)) {
            return;
        }

        rewardVideoAD = new RewardVideoAD(mActivity, mAdParams.getPostId(), this); // 有声播放
        rewardVideoAD.loadAD();
    }

    @Override
    public void onADLoad() {

        if (mAdProvider != null) {
            mAdProvider.trackEventLoad();
        }

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "Gdt RewardVideoAdWrapper load isCancel");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_CANCEL, "isCancel");
            return;
        }

        if (isTimeOut()) {
            LogUtils.e(TAG, "Gdt RewardVideoAdWrapper load isTimeOut");
            localExecFail(mAdProvider, MobiConstantValue.ERROR.TYPE_TIMEOUT, "isTimeOut");
            return;
        }


        if (rewardVideoAD != null && !rewardVideoAD.hasShown()) {//广告展示检查2：当前广告数据还没有展示过
            long delta = 1000;//建议给广告过期时间加个buffer，单位ms，这里demo采用1000ms的buffer
            //广告展示检查3：展示广告前判断广告数据未过期
            if (SystemClock.elapsedRealtime() < (rewardVideoAD.getExpireTimestamp() - delta)) {

                setExecSuccess(true);
                localExecSuccess(mAdProvider);

//                IExpressAdView expressAdView = null;
//                if (mAdParams.isAutoShowAd()) {
//                    rewardVideoAD.showAD();
//                } else {
//                    expressAdView = new GdtRewardAdView(rewardVideoAD);
//                }

                if (mAdProvider != null) {
                    mAdProvider.callbackRewardLoad(mListener, this, mAdParams.isAutoShowAd());
                }

            } else {
                Toast.makeText(mActivity, "激励视频广告已过期，请再次请求广告后进行广告展示！", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mActivity, "激励视频广告过期，请再次请求广告后进行广告展示！", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onVideoCached() {

        if (mAdProvider != null) {
            mAdProvider.callbackRewardCached(mListener);
            mAdProvider.trackCache();
        }

    }

    @Override
    public void onADShow() {
        //show成功前判断一下，是否已经把任务给取消了
        if (mAdProvider != null) {
            mAdProvider.callbackRewardGdtShow(mListener);
            mAdProvider.trackGdtShow();
        }
    }

    @Override
    public void onADExpose() {

        if (mAdProvider != null) {
            mAdProvider.trackShow();
            mAdProvider.trackEventShow();
            mAdProvider.callbackRewardExpose(mListener);
        }
    }

    @Override
    public void onReward() {
        if (mAdProvider != null) {
            mAdProvider.callbackRewardVerify(false, 0, "", mListener);
            mAdProvider.trackRewardVerify();
        }
    }

    @Override
    public void onADClick() {

        if (mAdProvider != null) {
            mAdProvider.trackClick();
            mAdProvider.trackEventClick();
            mAdProvider.callbackRewardClick(mListener);
        }

    }

    @Override
    public void onVideoComplete() {
        if (mAdProvider != null) {
            mAdProvider.callbackRewardVideoComplete(mListener);
            mAdProvider.trackComplete();
        }
    }

    @Override
    public void onADClose() {

        if (mAdProvider != null) {
            mAdProvider.trackEventClose();
            mAdProvider.callbackRewardClose(mListener);
        }
    }

    @Override
    public void onError(AdError adError) {
        if (mAdProvider != null) {
            if (adError == null) {
                localExecFail(mAdProvider, -100, "onNoAD 没有数据 adError == null");
            } else {
                localExecFail(mAdProvider, adError.getErrorCode(), adError.getErrorMsg());
            }
        }
    }

    @Override
    public void run() {
        if (mAdProvider != null) {
            mAdProvider.trackEventStart();
        }
        createRewardVideoAd();
    }

    @Override
    public int getStyleType() {
        return MobiConstantValue.STYLE.REWARD;
    }

    @Override
    public void show() {
        if (rewardVideoAD != null) {
            rewardVideoAD.showAD();
        }

        if (mAdProvider != null) {
            mAdProvider.trackStartShow();
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        if (rewardVideoAD != null) {
            rewardVideoAD = null;
        }
    }
}
