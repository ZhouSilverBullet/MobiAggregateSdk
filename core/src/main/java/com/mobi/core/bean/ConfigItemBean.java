package com.mobi.core.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2019-11-09  14:00
 */
public class ConfigItemBean implements Serializable {

    private String posid;//自己服务器广告位id
    private int sort_type;//排序类型 1按顺序 2按价格
    private List<AdBean> network = new ArrayList<>();
    private List<String> sort_parameter = new ArrayList<>();

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

    public List<AdBean> getNetwork() {
        return network;
    }

    public void setNetwork(List<AdBean> network) {
        this.network = network;
    }

    public List<String> getSort_parameter() {
        return sort_parameter;
    }

    public void setSort_parameter(List<String> sort_parameter) {
        this.sort_parameter = sort_parameter;
    }

    @Override
    public String toString() {
        return "ConfigItemBean{" +
                "posid='" + posid + '\'' +
                ", sort_type=" + sort_type +
                ", network=" + network +
                ", sort_parameter=" + sort_parameter +
                '}';
    }
}
