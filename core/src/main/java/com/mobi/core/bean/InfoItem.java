package com.mobi.core.bean;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-11-25  17:01
 */
public class InfoItem implements Serializable {

    private String appid = "";
    private String appname = "";
    private String sdk = "";


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    @Override
    public String toString() {
        return "InfoItem{" +
                "appid='" + appid + '\'' +
                ", appname='" + appname + '\'' +
                ", sdk='" + sdk + '\'' +
                '}';
    }
}
