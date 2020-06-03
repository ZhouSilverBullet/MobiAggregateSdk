package com.mobi.core.listener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 16:08
 * @Dec 略
 */
public interface IExpressListener {
    void onAdClick(String type);//广告被点击

    void onLoadFailed(String type, int faildCode, String faildMsg);//广告加载失败

    void onAdDismissed(String type);//广告被关闭

    void onAdRenderSuccess(String type);//广告渲染成功

    void onAdShow(String type);//广告显示
}
