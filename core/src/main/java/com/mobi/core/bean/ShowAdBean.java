package com.mobi.core.bean;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 16:44
 * @Dec 略
 */
public class ShowAdBean {
    private String appId;
    private String postId;
    private String providerType;
    private String appName;
    /**
     *  tt , gdt..
     *  用于md5加密使用的一项数据
     */
    private String sdk;
    private boolean isPushMessage;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public boolean isPushMessage() {
        return isPushMessage;
    }

    public void setPushMessage(boolean pushMessage) {
        isPushMessage = pushMessage;
    }
}
