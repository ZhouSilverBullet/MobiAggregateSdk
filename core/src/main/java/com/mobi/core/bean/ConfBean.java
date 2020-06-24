package com.mobi.core.bean;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/24 15:28
 * @Dec 略
 */
public class ConfBean {
    //rp_err
    private int rpErr;

    public ConfBean(int rpErr) {
        this.rpErr = rpErr;
    }

    public int getRpErr() {
        return rpErr;
    }

    public void setRpErr(int rpErr) {
        this.rpErr = rpErr;
    }
}
