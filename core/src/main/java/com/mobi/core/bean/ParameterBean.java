package com.mobi.core.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-11-09  14:03
 */
public class ParameterBean implements Serializable {
    @SerializedName("appid")
    private String appid;
    @SerializedName("appname")
    private String appname;
    @SerializedName("posid")
    private String posid;

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

    public String getPosid() {
        return posid;
    }

    public void setPosid(String posid) {
        this.posid = posid;
    }

    @Override
    public String toString() {
        return "ParameterBean{" +
                "appid='" + appid + '\'' +
                ", appname='" + appname + '\'' +
                ", posid='" + posid + '\'' +
                '}';
    }
}
