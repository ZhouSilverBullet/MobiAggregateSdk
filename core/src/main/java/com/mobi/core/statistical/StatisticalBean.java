package com.mobi.core.statistical;

import java.io.Serializable;

/**
 * 统计用到的数据基类
 * author : liangning
 * date : 2019-11-17  18:54
 */
public class StatisticalBean extends StatisticalBaseBean implements Serializable {

    private String network;//广告渠道 tt/gdt
    private String posid;//自由广告位id
    //pv和click取反值
    private int pv;// 1展示 0未展示
    private int click;// 1 点击 0未点击

    public StatisticalBean(String network, String posid, int pv, int click) {
        this.network = network;
        this.posid = posid;
        this.pv = pv;
        this.click = click;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getPosid() {
        return posid;
    }

    public void setPosid(String posid) {
        this.posid = posid;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }
}
