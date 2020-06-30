package com.mobi.core.analysis;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/30 16:35
 * @Dec 提供AdProvider上传时必须需要的参数
 */
public class AdPushParams {
    //聚合sdk的codeId
    private String mobiCodeId;
    //整条先的md5
    private String md5;
    //策略类型
    private int sortType;
    //类型 1 启动页 2 插屏
    private int styleType;
    /**
     * 是否推送除
     * 10 请求 20 填充  30 展示  40点击
     * 是否进行推送
     */
    private boolean isPushOtherEvent;

    public AdPushParams(String mobiCodeId,
                        String md5,
                        int sortType,
                        int styleType,
                        boolean isPushOtherEvent) {
        this.mobiCodeId = mobiCodeId;
        this.md5 = md5;
        this.sortType = sortType;
        this.styleType = styleType;
        this.isPushOtherEvent = isPushOtherEvent;
    }

    public static AdPushParams create(String mobiCodeId,
                                      String md5,
                                      int sortType,
                                      int styleType,
                                      boolean isPushOtherEvent) {
        return new AdPushParams(mobiCodeId,
                md5,
                sortType,
                styleType,
                isPushOtherEvent);
    }

    public String getMobiCodeId() {
        return mobiCodeId;
    }

    public void setMobiCodeId(String mobiCodeId) {
        this.mobiCodeId = mobiCodeId;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public int getStyleType() {
        return styleType;
    }

    public void setStyleType(int styleType) {
        this.styleType = styleType;
    }

    public boolean isPushOtherEvent() {
        return isPushOtherEvent;
    }

    public void setPushOtherEvent(boolean pushOtherEvent) {
        isPushOtherEvent = pushOtherEvent;
    }
}
