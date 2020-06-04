package com.mobi.gdt.wrapper;

import android.app.Activity;
import android.os.SystemClock;
import android.widget.Toast;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.listener.IRewardAdListener;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 11:29
 * @Dec 略
 */
public class RewardVideoAdWrapper extends BaseAdWrapper implements RewardVideoADListener {
    BaseAdProvider mAdProvider;
    Activity mActivity;
    String mCodeId;
    boolean mSupportDeepLink;
    IRewardAdListener mListener;

    private RewardVideoAD rewardVideoAD;

    public RewardVideoAdWrapper(BaseAdProvider adProvider,
                                Activity activity,
                                String codeId,
                                boolean supportDeepLink,
                                IRewardAdListener listener) {
        mAdProvider = adProvider;
        mActivity = activity;
        mCodeId = codeId;
        mSupportDeepLink = supportDeepLink;
        mListener = listener;
    }

    public void createRewardVideoAd() {
        rewardVideoAD = new RewardVideoAD(mActivity, getAppId(), mCodeId, this); // 有声播放
        rewardVideoAD.loadAD();
    }

    @Override
    public void onADLoad() {
//        if (listener != null) {
//            listener.onAdLoad(mProviderType);
//        }
        if (mAdProvider != null) {
            mAdProvider.callbackRewardLoad(mListener);
        }

        // 3.展示广告
        if (true) {//广告展示检查1：广告成功加载，此处也可以使用videoCached来实现视频预加载完成后再展示激励视频广告的逻辑
            if (rewardVideoAD != null && !rewardVideoAD.hasShown()) {//广告展示检查2：当前广告数据还没有展示过
                long delta = 1000;//建议给广告过期时间加个buffer，单位ms，这里demo采用1000ms的buffer
                //广告展示检查3：展示广告前判断广告数据未过期
                if (SystemClock.elapsedRealtime() < (rewardVideoAD.getExpireTimestamp() - delta)) {
                    rewardVideoAD.showAD();
                } else {
                    Toast.makeText(mActivity, "激励视频广告已过期，请再次请求广告后进行广告展示！", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(mActivity, "此条广告已经展示过，请再次请求广告后进行广告展示！", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mActivity, "成功加载广告后再进行广告展示！", Toast.LENGTH_LONG).show();
        }
        //情况一下实例
        rewardVideoAD = null;
    }

    @Override
    public void onVideoCached() {
//        if (listener != null) {
//            listener.onCached(mProviderType);
//        }

        if (mAdProvider != null) {
            mAdProvider.callbackRewardCached(mListener);
        }


    }

    @Override
    public void onADShow() {
//        if (listener != null) {
//            listener.onAdShow(mProviderType);
//        }

        if (mAdProvider != null) {
            mAdProvider.callbackRewardGdtShow(mListener);
        }
    }

    @Override
    public void onADExpose() {
//                if (listener != null) {
//                    listener.onAdShow(mProviderType);
//                }
//        if (mAdProvider != null) {
//            mAdProvider.callbackRewardShow(mListener);
//        }
        if (mAdProvider != null) {
            mAdProvider.callbackRewardExpose(mListener);
        }
    }

    @Override
    public void onReward() {
//        if (listener != null) {
//            listener.onRewardVerify(mProviderType, false, 0, "");
//        }
        if (mAdProvider != null) {
            mAdProvider.callbackRewardVerify(false, 0, "", mListener);
        }
    }

    @Override
    public void onADClick() {
//        if (listener != null) {
//            listener.onCached(mProviderType);
//        }
        if (mAdProvider != null) {
            mAdProvider.callbackRewardClick(mListener);
        }

    }

    @Override
    public void onVideoComplete() {
//        if (listener != null) {
//            listener.onVideoComplete(mProviderType);
//        }

        if (mAdProvider != null) {
            mAdProvider.callbackRewardVideoComplete(mListener);
        }
    }

    @Override
    public void onADClose() {
//        if (listener != null) {
//            listener.onAdClose(mProviderType);
//        }

        if (mAdProvider != null) {
            mAdProvider.callbackRewardClose(mListener);
        }
    }

    @Override
    public void onError(AdError adError) {
//        if (listener != null) {
//            listener.onAdFail(mProviderType, adError.getErrorCode(), adError.getErrorMsg());
//        }

        if (mAdProvider != null) {
            if (adError == null) {
                mAdProvider.callbackRewardFail(-100, "onNoAD 没有数据 adError == null", mListener);
            } else {
                mAdProvider.callbackRewardFail(adError.getErrorCode(), adError.getErrorMsg(), mListener);
            }
        }
    }
}
