package com.mobi.gdt.wrapper;

import android.app.Activity;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Toast;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.LocalAdParams;
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
public class RewardVideoAdWrapper extends BaseAdWrapper implements RewardVideoADListener {
    private final LocalAdParams mAdParams;
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
    }

    private void createRewardVideoAd() {
        String postId = mAdParams.getPostId();
        if (TextUtils.isEmpty(postId)) {
            localExecFail(mAdProvider, -101,
                    "mobi 后台获取的 postId 不正确 或者 postId == null");
            return;
        }

        rewardVideoAD = new RewardVideoAD(mActivity, getAppId(), mAdParams.getPostId(), this); // 有声播放
        rewardVideoAD.loadAD();
    }

    @Override
    public void onADLoad() {

        //load成功前判断一下，是否已经把任务给取消了
        if (isCancel()) {
            LogUtils.e(TAG, "Gdt RewardVideoAdWrapper load isCancel");
            return;
        }

        if (mAdProvider != null) {
            mAdProvider.callbackRewardLoad(mListener);
        }

        // 3.展示广告
        if (true) {//广告展示检查1：广告成功加载，此处也可以使用videoCached来实现视频预加载完成后再展示激励视频广告的逻辑
            if (rewardVideoAD != null && !rewardVideoAD.hasShown()) {//广告展示检查2：当前广告数据还没有展示过
                long delta = 1000;//建议给广告过期时间加个buffer，单位ms，这里demo采用1000ms的buffer
                //广告展示检查3：展示广告前判断广告数据未过期
                if (SystemClock.elapsedRealtime() < (rewardVideoAD.getExpireTimestamp() - delta)) {
                    if (isCancel()) {
                        LogUtils.e(TAG, "Gdt RewardVideoAdWrapper onAdShow isCancel");
                        return;
                    }
                    setExecSuccess(true);
                    localExecSuccess(mAdProvider);

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

        if (mAdProvider != null) {
            mAdProvider.callbackRewardCached(mListener);
        }

    }

    @Override
    public void onADShow() {
        //show成功前判断一下，是否已经把任务给取消了
//        if (isCancel()) {
//            LogUtils.e(TAG, "Gdt RewardVideoAdWrapper onAdShow isCancel");
//            return;
//        }
//
//        setExecSuccess(true);
//        localExecSuccess(mAdProvider);

        if (mAdProvider != null) {
            mAdProvider.callbackRewardGdtShow(mListener);
        }
    }

    @Override
    public void onADExpose() {

        if (mAdProvider != null) {
            mAdProvider.callbackRewardExpose(mListener);
        }
    }

    @Override
    public void onReward() {

        if (mAdProvider != null) {
            mAdProvider.callbackRewardVerify(false, 0, "", mListener);
        }
    }

    @Override
    public void onADClick() {

        if (mAdProvider != null) {
            mAdProvider.callbackRewardClick(mListener);
        }

    }

    @Override
    public void onVideoComplete() {
        if (mAdProvider != null) {
            mAdProvider.callbackRewardVideoComplete(mListener);
        }
    }

    @Override
    public void onADClose() {

        if (mAdProvider != null) {
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
        createRewardVideoAd();
    }
}
