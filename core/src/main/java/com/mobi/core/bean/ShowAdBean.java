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
}
