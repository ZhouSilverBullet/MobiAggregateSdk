package com.mobi.core.bean;

import java.util.List;
import java.util.Set;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 19:50
 * @Dec 略
 */
public class LocalAdBean {
    private int sortType;
    private List<ShowAdBean> mAdBeans;

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public List<ShowAdBean> getAdBeans() {
        return mAdBeans;
    }

    public void setAdBeans(List<ShowAdBean> adBeans) {
        mAdBeans = adBeans;
    }

}
