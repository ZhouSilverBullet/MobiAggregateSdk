package com.mobi.core.listener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 16:21
 * @Dec  穿山甲下载的回调 这个只有穿山甲的情况下才有回调
 */
public interface ITTAppDownloadListener {
    ///////// CSJ start //////////////

    /**
     * 点击开始下载
     */
    default void onIdle() {

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
