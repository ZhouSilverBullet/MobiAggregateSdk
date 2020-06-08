package com.mobi.core.bean;

import java.io.Serializable;

/**
 * 广告数据类 对应network中的item
 * author : liangning
 * date : 2019-11-09  14:02
 */
public class AdBean implements Serializable, Comparable<AdBean> {

    private String name;
    private String sdk;
    private int order;
    private ParameterBean parameterBean;

    public AdBean(String name, String sdk, int order, ParameterBean parameterBean) {
        this.name = name;
        this.sdk = sdk;
        this.order = order;
        this.parameterBean = parameterBean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ParameterBean getParameterBean() {
        return parameterBean;
    }

    public void setParameterBean(ParameterBean parameterBean) {
        this.parameterBean = parameterBean;
    }

    @Override
    public String toString() {
        return "AdBean{" +
                "name='" + name + '\'' +
                ", sdk='" + sdk + '\'' +
                ", order=" + order +
                ", parameterBean=" + parameterBean +
                '}';
    }

    @Override
    public int compareTo(AdBean o) {
        if (o == null) {
            return -1;
        }

        return order - o.order;
    }
}
