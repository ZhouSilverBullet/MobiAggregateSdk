package com.mobi.core.statistical;

import com.mobi.core.utils.DateUtils;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-11-18  13:49
 */
public class StatisticalBaseBean implements Serializable {

    private String day = DateUtils.getStringDateDay();
    private String time = DateUtils.getStringDateMin();
    private String deviceid = "caae4c37421a60f4251b06304143b3ea";
    private String platform = "1";
    private String sdkv = "1.0.0";
    private String channel_no = "xiaomi";

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSdkv() {
        return sdkv;
    }

    public void setSdkv(String sdkv) {
        this.sdkv = sdkv;
    }

    public String getChannel_no() {
        return channel_no;
    }

    public void setChannel_no(String channel_no) {
        this.channel_no = channel_no;
    }
}
