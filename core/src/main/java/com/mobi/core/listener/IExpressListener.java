package com.mobi.core.listener;

import android.util.Log;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 16:08
 * @Dec 略
 */
public interface IExpressListener {
    void onAdClick(String type);//广告被点击

    void onLoadFailed(String type, int code, String errorMsg);//广告加载失败

    void onAdDismissed(String type);//广告被关闭

    void onAdRenderSuccess(String type);//广告渲染成功

    void onAdShow(String type);//广告显示


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


    ///////// CSJ start //////////////

    /**
     * 点击开始下载
     */
    default void onIdle(String type) {

    }

    /**
     * 下载中
     */
    default void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {

    }

    /**
     * 下载暂停
     *
     */
    default void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {

    }

    /**
     * 下载失败
     *
     */
    default void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {

    }

    /**
     * 下载完成
     */
    default void onDownloadFinished(long totalBytes, String fileName, String appName) {

    }

    /**
     * 安装完成
     */
    default void onInstalled(String fileName, String appName) {

    }
    ///////// CSJ end //////////////
}
