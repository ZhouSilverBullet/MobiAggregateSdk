package com.mobi.core.analysis;

import com.mobi.core.CoreSession;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.utils.DateUtils;

import java.io.Serializable;

/**
 * Email: zhousaito@163.com
 * Created by zhousaito 2020/6/8 20:51
 * Version: 1.0
 * Description:
 */
public class BaseAnalysisBean implements Serializable {

    private String day = DateUtils.getStringDateDay();
    private String time = DateUtils.getStringDateMin();
    private String deviceid = CoreSession.get().getDeviceNo();
    private String platform = MobiConstantValue.PLATFORM;
    private String sdkv = MobiConstantValue.SDK_VERSION;
    private String channel_no = MobiConstantValue.CHANNEL;

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
