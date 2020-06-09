package com.mobi.core.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2019-11-09  14:00
 */
public class ConfigBean implements Serializable {
    private long storage_time = 0;
    private long timeout = 3600;//有效期
    private long ad_adk_req_timeout = 800;//延时执行时间
    private List<ConfigItemBean> list;
    private List<SdkInfoItem> sdk_info;
    private ConfigAdBean mConfigAdBean;

    public ConfigBean(long storage_time,
                      long timeout,
                      long ad_adk_req_timeout,
                      List<ConfigItemBean> list,
                      List<SdkInfoItem> sdk_info,
                      ConfigAdBean configAdBean) {
        this.storage_time = storage_time;
        this.timeout = timeout;
        this.ad_adk_req_timeout = ad_adk_req_timeout;
        this.list = list;
        this.sdk_info = sdk_info;
        mConfigAdBean = configAdBean;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getAd_adk_req_timeout() {
        return ad_adk_req_timeout;
    }

    public void setAd_adk_req_timeout(long ad_adk_req_timeout) {
        this.ad_adk_req_timeout = ad_adk_req_timeout;
    }

    public List<ConfigItemBean> getList() {
        return list;
    }

    public void setList(List<ConfigItemBean> list) {
        this.list = list;
    }

    public long getStorage_time() {
        return storage_time;
    }

    public void setStorage_time(long storage_time) {
        this.storage_time = storage_time;
    }

    public List<SdkInfoItem> getSdk_info() {
        return sdk_info;
    }

    public void setSdk_info(List<SdkInfoItem> sdk_info) {
        this.sdk_info = sdk_info;
    }

    public ConfigAdBean getConfigAdBean() {
        return mConfigAdBean;
    }

    public void setConfigAdBean(ConfigAdBean configAdBean) {
        mConfigAdBean = configAdBean;
    }

    @Override
    public String toString() {
        return "ConfigBean{" +
                "storage_time=" + storage_time +
                ", timeout=" + timeout +
                ", ad_adk_req_timeout=" + ad_adk_req_timeout +
                ", list=" + list +
                ", sdk_info=" + sdk_info +
                ", mConfigAdBean=" + mConfigAdBean +
                '}';
    }
}
