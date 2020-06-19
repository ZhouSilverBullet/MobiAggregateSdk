package com.mobi.core;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 17:39
 * @Dec 用于一些常量配置
 */
public class MobiConstantValue {
    /**
     * gdt 渲染失败 code
     */
    public static final int GDT_ERROR_RENDER_CODE = -10000;
    public static final String GDT_ERROR_RENDER_MESSAGE = "广告渲染失败";

    public static final String DEVICE_ID = "device_id";

    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 2;

    public static String PLATFORM = "android";
    public static String SDK_VERSION = BuildConfig.VERSION_NAME;

    public static String CHANNEL = "";//渠道

//    public static final String LOCAL_URL = "http://cdn.findwxapp.com/mediation.dev2.txt";
    public static final String LOCAL_URL = "http://www.zhousaito.top:8080/config";


    public static final String ALL_CONFIG = "all_config";
    public static final String ALL_TIMEOUT = "all_timeout";

    public static final String PROTO_CONFIG = "proto_config";
    public static final String PROTO_TIMEOUT = "proto_timeout";


    /**
     * 本地个类型，告知给外面
     */
    public static final String TYPE_LOCAL_MOBI= "MobiType";

    public static final int SDK_CODE_2001 = 2001;
    public static final String SDK_MESSAGE_2001 = "mobi codeid 不正确 或者 codeId == null";
    public static final int SDK_CODE_2002 = 2002;
    public static final String SDK_MESSAGE_2002 = "mobi 的策略，本地还没有支持";
    public static final int SDK_CODE_2003 = 2003;
    public static final String SDK_MESSAGE_2003 = "mobi 没有策略任务";
    public static final int SDK_CODE_2004 = 2004;
    public static final String SDK_MESSAGE_2004 = "mobi 后台获取的 postId 不正确 或者 postId == null";


}
