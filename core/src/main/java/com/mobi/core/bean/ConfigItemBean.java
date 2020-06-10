package com.mobi.core.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author : liangning
 * date : 2019-11-09  14:00
 */
public class ConfigItemBean implements Serializable {

    private String posid;//自己服务器广告位id
    private int sort_type;//排序类型 1按顺序 2按价格
    private List<AdBean> network;
    private List<String> sortParameters;

    public ConfigItemBean(String posid,
                          int sort_type,
                          List<AdBean> network,
                          List<String> sortParameters) {
        this.posid = posid;
        this.sort_type = sort_type;
        this.network = network;
        this.sortParameters = sortParameters;
    }

    /**
     * NetWorkSort 是否已经进行了排序
     */
    private boolean isNetworkSort;

    public String getPosid() {
        return posid;
    }

    public void setPosid(String posid) {
        this.posid = posid;
    }

    public int getSort_type() {
        return sort_type;
    }

    public void setSort_type(int sort_type) {
        this.sort_type = sort_type;
    }

    /**
     * 排序一次
     * @return
     */
    public List<AdBean> getNetwork() {
        if (!isNetworkSort) {
            Collections.sort(network);
        }
        return network;
    }

    public void setNetwork(List<AdBean> network) {
        this.network = network;
    }

    public List<String> getSortParameters() {
        return sortParameters;
    }

    public void setSortParameters(List<String> sortParameters) {
        this.sortParameters = sortParameters;
    }

    @Override
    public String toString() {
        return "ConfigItemBean{" +
                "posid='" + posid + '\'' +
                ", sort_type=" + sort_type +
                ", network=" + network +
                ", sort_parameter=" + sortParameters +
                '}';
    }
}
