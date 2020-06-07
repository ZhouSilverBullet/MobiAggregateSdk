package com.mobi.core.strategy;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/7 13:51
 * @Dec 略
 */
public class StrategyError {

    /**
     * 出现错误的 sdk类型
     */
    private String providerType;
    /**
     * 出现错误的code
     */
    private int code;
    /**
     * 出现错误的message
     */
    private String message;

    public StrategyError(String providerType, int code, String message) {
        this.providerType = providerType;
        this.code = code;
        this.message = message;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
