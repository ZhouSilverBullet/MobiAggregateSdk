package com.mobi.core.network;

import android.support.v4.util.ArrayMap;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Email: zhousaito@163.com
 * Created by zhousaito 2020/6/8 17:49
 * Version: 1.0
 * Description:
 */
public class Request {
    public static final int GET = 0;
    public static final int POST = 1;

    private String url;
    /**
     * get / post
     * 默认是GET
     */
    private int method;

    /**
     * post请求的体
     */
    private String fromBody;

    private boolean isGzipCompress;

    private Map<String, String> headers;

    private Request(Builder builder) {
        setUrl(builder.url);
        setMethod(builder.method);
        setFromBody(builder.fromBody);

        isGzipCompress = builder.isGzipCompress;
        headers = builder.headers;
        if (headers == null) {
            headers = createMaps();
        }

        if (isGzipCompress) {
            headers.put("Accept-Encoding", "gzip");
        }

        putDefaultHeader();

    }

    private void putDefaultHeader() {
        headers.put("User-Agent", "MobiSdkHttp1.0");
        headers.put("Accept-Language", "zh-CN");
        headers.put("Connection", "Keep-Alive");
        headers.put("Charset", "UTF-8");
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getRequestMethod() {
        String requestMethod;
        if (method == 1) {
            requestMethod = "POST";
        } else {
            requestMethod = "GET";
        }
        return requestMethod;
    }

    public boolean isGzipCompress() {
        return isGzipCompress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getFromBody() {
        return fromBody;
    }

    public void setFromBody(String fromBody) {
        this.fromBody = fromBody;
    }

    public static final class Builder {
        private String url;
        private int method;
        private String fromBody;
        private boolean isGzipCompress;
        private Map<String, String> headers;

        public Builder() {
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setMethod(int method) {
            this.method = method;
            return this;
        }

        public Builder setFromBody(String fromBody) {
            this.fromBody = fromBody;
            return this;
        }

        public Builder setIsGzipCompress(boolean isGzipCompress) {
            this.isGzipCompress = isGzipCompress;
            return this;
        }

        public Builder addHeaders(String key, String value) {
            if (headers == null) {
                headers = createMaps();
            }
            headers.put(key, value);
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }

    private static Map<String, String> createMaps() {
        return new ArrayMap<>();
    }
}
