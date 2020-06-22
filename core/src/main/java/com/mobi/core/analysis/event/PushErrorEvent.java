//package com.mobi.core.analysis.event;
//
//import android.text.TextUtils;
//
//import com.mobi.core.CoreSession;
//import com.mobi.core.utils.DateUtils;
//
///**
// * @author zhousaito
// * @version 1.0
// * @date 2020/6/18 20:39
// * @Dec 略
// */
//public class PushErrorEvent extends PushEvent {
//    // "event": 10,
//    //            "network":"tt",
//    //          	"day":"2020-06-18",
//    //          	"time":"2020-06-18 19:50",
//    //          	"sort_type": "1",
//    //          	"appid":"1112312xxx",
//    //          	"bundle":"本地安装应用的包名",
//    //            "posid":"2048001", //给用户的使用的 postId
//    //            "style_type":1, //插屏
//    //            "timestamp":1592465118652 //时间戳
//    //
//    //"type":10000,  //日志类型，显示  点击了， 报错了， 超时了
//    //            "code":200, //成功200 code  失败了用失败了的
//    //            "message":"tt广告成功显示了", //失败信息
//    //          	"debug" : "" //传自己的错误
//
//
//    private int type;
//    private int code;
//    private String message;
//    private String debug;
//
//
//    public PushErrorEvent(int event,
//                          int styleType,
//                          String postId,
//                          int sortType,
//                          String network,
//                          int type,
//                          int code,
//                          String message,
//                          String debug) {
//        super(event, styleType, postId, sortType, network);
//        this.type = type;
//        this.code = code;
//        this.message = message;
//        this.debug = debug;
//    }
//
//}
