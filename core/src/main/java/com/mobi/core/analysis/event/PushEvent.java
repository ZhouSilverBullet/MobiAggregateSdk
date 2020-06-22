package com.mobi.core.analysis.event;

import android.text.TextUtils;

import com.mobi.core.CoreSession;
import com.mobi.core.utils.DateUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/18 20:39
 * @Dec 略
 */
public class PushEvent {
    // "event": 10,
    //            "network":"tt",
    //          	"day":"2020-06-18",
    //          	"time":"2020-06-18 19:50",
    //          	"sort_type": "1",
    //          	"appid":"1112312xxx",
    //          	"bundle":"本地安装应用的包名",
    //            "posid":"2048001", //给用户的使用的 postId
    //            "style_type":1, //插屏
    //            "timestamp":1592465118652 //时间戳

    private int event;
    private String postId;
    private int styleType;
    private int sortType;
    private String network;

    /**
     * 与错误相关的属性类
     */
    private int type;
    private int code;
    private String message;
    private String debug;

    private String day;
    private String time;
    private long timestamp;
    private String bundle;
    private String appId;

    public PushEvent(int event,
                     int styleType,
                     String postId,
                     int sortType,
                     String network) {
        this.event = event;
        this.styleType = styleType;
        this.postId = postId;
        this.sortType = sortType;
        this.network = network;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getDay() {
        if (TextUtils.isEmpty(day)) {
            day = DateUtils.getStringDateDay();
        }
        return day;
    }

    public String getTime() {
        if (TextUtils.isEmpty(time)) {
            time = DateUtils.getStringDateMin();
        }
        return time;
    }

    public long getTimestamp() {
        if (timestamp == 0L) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public String getAppId() {
        if (TextUtils.isEmpty(appId)) {
            if (CoreSession.get().getContext() != null) {
                appId = CoreSession.get().getAppId();
            }
        }
        return appId;
    }

    public String getBundle() {
        if (TextUtils.isEmpty(bundle)) {
            if (CoreSession.get().getContext() != null) {
                bundle = CoreSession.get().getContext().getPackageName();
            }
        }
        return bundle;
    }

    public int getSortType() {
        return sortType;
    }

    public int getEvent() {
        return event;
    }

    public int getStyleType() {
        return styleType;
    }

    public String getPostId() {
        return postId;
    }

    public String getNetwork() {
        return network;
    }
}
