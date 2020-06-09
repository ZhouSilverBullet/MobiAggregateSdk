package com.mobi.core.bean;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/8 21:55
 * @Dec 略
 */
public class ConfigAdBean {

    /**
     * timeout : 3600
     * ad_adk_req_timeout : 3000
     * report_url : http://dev.findwxapp.com/rcv/v1/server-projects/upload/ad-log
     * developer_url : http://dev.findwxapp.com/rcv/v1/server-projects/upload/ad-log
     * proto_url : https://cdn.findwxapp.com/mediation.dev2.txt
     */

    private long timeout;
    private long adAdkReqTimeout;
    private String reportUrl;
    private String developerUrl;
    private String protoUrl;

    public ConfigAdBean(long timeout,
                        long adAdkReqTimeout,
                        String reportUrl,
                        String developerUrl,
                        String protoUrl) {
        this.timeout = timeout;
        this.adAdkReqTimeout = adAdkReqTimeout;
        this.reportUrl = reportUrl;
        this.developerUrl = developerUrl;
        this.protoUrl = protoUrl;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getAdAdkReqTimeout() {
        return adAdkReqTimeout;
    }

    public void setAdAdkReqTimeout(long adAdkReqTimeout) {
        this.adAdkReqTimeout = adAdkReqTimeout;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getDeveloperUrl() {
        return developerUrl;
    }

    public void setDeveloperUrl(String developerUrl) {
        this.developerUrl = developerUrl;
    }

    public String getProtoUrl() {
        return protoUrl;
    }

    public void setProtoUrl(String protoUrl) {
        this.protoUrl = protoUrl;
    }
}
