package com.mobi.gdt;

import android.app.Activity;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mobi.core.BaseAdProvider;
import com.mobi.core.listener.IExpressListener;
import com.mobi.core.listener.IFullScreenVideoAdListener;
import com.mobi.core.listener.IInteractionAdListener;
import com.mobi.core.listener.IRewardAdListener;
import com.mobi.core.listener.ISplashAdListener;
import com.mobi.gdt.wrapper.NativeExpressAdWrapper;
import com.mobi.gdt.wrapper.UnifiedInterstitialADWrapper;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/1 18:12
 * @Dec 略
 */
public class GdtProvider extends BaseAdProvider {
    public static final String TAG = "GdtProvider";

    public GdtProvider(String providerType) {
        super(providerType);
    }

    public void splash(final Activity activity,
                       final String codeId,
                       final ViewGroup splashContainer,
                       final ISplashAdListener listener) {


        callbackSplashStartRequest(listener);

        SplashAD splashAD = new SplashAD(activity, GdtSession.get().getAppId(), codeId, new SplashADListener() {
            @Override
            public void onADDismissed() {

                callbackSplashDismissed(listener);
            }

            @Override
            public void onNoAD(AdError adError) {
                if (listener != null) {
                    if (adError != null) {
                        callbackSplashFail("code: " + adError.getErrorCode() + ", errorMsg: " + adError.getErrorMsg(), listener);
                    } else {
                        callbackSplashFail("广告加载失败", listener);
                    }
                }
            }

            @Override
            public void onADPresent() {

            }

            @Override
            public void onADClicked() {

                callbackSplashClicked(listener);
            }

            @Override
            public void onADTick(long l) {

            }

            @Override
            public void onADExposure() {
                callbackSplashExposure(listener);
            }

            @Override
            public void onADLoaded(long l) {

                callbackSplashLoaded(listener);
            }
        });

        splashAD.fetchAndShowIn(splashContainer);

    }

    @Override
    public void fullscreen(Activity activity, String codeId, int orientation, boolean supportDeepLink, IFullScreenVideoAdListener listener) {

    }

    private RewardVideoAD rewardVideoAD;

    @Override
    public void rewardVideo(Activity activity, String codeId, boolean supportDeepLink, IRewardAdListener listener) {
        rewardVideoAD = new RewardVideoAD(activity, GdtSession.get().getAppId(), codeId, new RewardVideoADListener() {
            @Override
            public void onADLoad() {
                if (listener != null) {
                    listener.onAdLoad(mProviderType);
                }

                // 3.展示广告
                if (true) {//广告展示检查1：广告成功加载，此处也可以使用videoCached来实现视频预加载完成后再展示激励视频广告的逻辑
                    if (rewardVideoAD != null && !rewardVideoAD.hasShown()) {//广告展示检查2：当前广告数据还没有展示过
                        long delta = 1000;//建议给广告过期时间加个buffer，单位ms，这里demo采用1000ms的buffer
                        //广告展示检查3：展示广告前判断广告数据未过期
                        if (SystemClock.elapsedRealtime() < (rewardVideoAD.getExpireTimestamp() - delta)) {
                            rewardVideoAD.showAD();
                        } else {
                            Toast.makeText(activity, "激励视频广告已过期，请再次请求广告后进行广告展示！", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(activity, "此条广告已经展示过，请再次请求广告后进行广告展示！", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(activity, "成功加载广告后再进行广告展示！", Toast.LENGTH_LONG).show();
                }
                //情况一下实例
                rewardVideoAD = null;
            }

            @Override
            public void onVideoCached() {
                if (listener != null) {
                    listener.onCached(mProviderType);
                }


            }

            @Override
            public void onADShow() {
                if (listener != null) {
                    listener.onAdShow(mProviderType);
                }
            }

            @Override
            public void onADExpose() {
//                if (listener != null) {
//                    listener.onAdShow(mProviderType);
//                }
            }

            @Override
            public void onReward() {
                if (listener != null) {
                    listener.onRewardVerify(mProviderType, false, 0, "");
                }
            }

            @Override
            public void onADClick() {
                if (listener != null) {
                    listener.onCached(mProviderType);
                }
            }

            @Override
            public void onVideoComplete() {
                if (listener != null) {
                    listener.onVideoComplete(mProviderType);
                }
            }

            @Override
            public void onADClose() {
                if (listener != null) {
                    listener.onAdClose(mProviderType);
                }
            }

            @Override
            public void onError(AdError adError) {
                if (listener != null) {
                    listener.onAdFail(mProviderType, adError.getErrorMsg());
                }
            }
        }); // 有声播放
        rewardVideoAD.loadAD();
    }

    UnifiedInterstitialAD iad;

    @Override
    public void interactionExpress(Activity activity,
                                   String codeId,
                                   boolean supportDeepLink,
                                   ViewGroup viewContainer,
                                   float expressViewWidth,
                                   float expressViewHeight,
                                   IInteractionAdListener listener) {


        UnifiedInterstitialADWrapper unifiedInterstitialADWrapper =
                new UnifiedInterstitialADWrapper(this, activity,
                        GdtSession.get().getAppId(), codeId, listener);

        unifiedInterstitialADWrapper.createInterstitialAD();

//        iad = new UnifiedInterstitialAD(activity, GdtSession.get().getAppId(), codeId, new UnifiedInterstitialADListener() {
//            @Override
//            public void onADReceive() {
//                if (listener != null) {
//                    listener.onADReceive(mProviderType);
//                }
//                iad.showAsPopupWindow();
//            }
//
//            @Override
//            public void onVideoCached() {
//                if (listener != null) {
//                    listener.onCached(mProviderType);
//                }
//                iad.showAsPopupWindow();
//            }
//
//            @Override
//            public void onNoAD(AdError adError) {
//                if (listener != null) {
//                    listener.onAdFail(mProviderType, "code: " + adError.getErrorCode() + ", errorMsg: " + adError.getErrorMsg());
//                }
//            }
//
//            @Override
//            public void onADOpened() {
//                if (listener != null) {
//                    listener.onADOpened(mProviderType);
//                }
//            }
//
//            @Override
//            public void onADExposure() {
//                if (listener != null) {
//                    listener.onADExposure(mProviderType);
//                }
//            }
//
//            @Override
//            public void onADClicked() {
//                if (listener != null) {
//                    listener.onAdClick(mProviderType);
//                }
//            }
//
//            @Override
//            public void onADLeftApplication() {
////                if (listener != null) {
////                    listener.onAdClick(mProviderType);
////                }
//            }
//
//            @Override
//            public void onADClosed() {
//                if (listener != null) {
//                    listener.onAdClose(mProviderType);
//                }
//            }
//        });
//        iad.loadAD();
    }

    @Override
    public void express(Activity mContext,
                        String codeId,
                        boolean supportDeepLink,
                        ViewGroup viewContainer,
                        int aDViewWidth,
                        int aDViewHeight,
                        int loadCount,
                        IExpressListener mListener) {

        NativeExpressAdWrapper nativeExpressAdWrap = new NativeExpressAdWrapper(
                mContext,
                this,
                codeId,
                supportDeepLink,
                viewContainer,
                aDViewWidth,
                aDViewHeight,
                loadCount,
                mListener);

        nativeExpressAdWrap.createNativeExpressAD();

    }


    private String getAppId() {
        return GdtSession.get().getAppId();
    }
}
