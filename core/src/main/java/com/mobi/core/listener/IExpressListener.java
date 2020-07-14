package com.mobi.core.listener;

import com.mobi.core.feature.ExpressAdView;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 16:08
 * @Dec 略
 */
public interface IExpressListener extends ITTAppDownloadListener, IAdFailListener {

    void onAdLoad(String providerType, ExpressAdView view);

    void onAdExposure(String type);//广告显示

    void onAdClick(String type);//广告被点击

    void onAdClose(String type);//广告被关闭

    void onAdRenderSuccess(String type);//广告渲染成功


    ////////// GDT start /////////////

    /**
     * 因为广告点击等原因离开当前 app 时调用
     */
    default void onADLeftApplication(String type) {

    }

    /**
     * 广告展开遮盖时调用
     */
    default void onADOpenOverlay(String type) {

    }

    /**
     * 广告关闭遮盖时调用
     */
    default void onADCloseOverlay(String type) {

    }

    ///////// GDT end //////////////

}
