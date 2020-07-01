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

    public static final String LOCAL_URL = "http://dev.findwxapp.com/flow-mediation/v1/ad";
//    public static final String LOCAL_URL = "http://www.zhousaito.top:8080/config";


    public static final String ALL_CONFIG = "all_config";
    public static final String ALL_TIMEOUT = "all_timeout";

    public static final String PROTO_CONFIG = "proto_config";
    public static final String PROTO_TIMEOUT = "proto_timeout";


    /**
     * 本地个类型，告知给外面
     */
    public static final String TYPE_LOCAL_MOBI= "MobiType";

    public static final int SDK_CODE_10005 = 10005;
    public static final String SDK_MESSAGE_10005 = "mobi codeid 不正确 或者 codeId == null";
    public static final int SDK_CODE_10006 = 10006;
    public static final String SDK_MESSAGE_10006 = "mobi 的策略，本地还没有支持";
    public static final int SDK_CODE_10007 = 10007;
    public static final String SDK_MESSAGE_10007 = "mobi 没有策略任务";
    public static final int SDK_CODE_10008 = 10008;
    public static final String SDK_MESSAGE_10008 = "mobi 后台获取的 postId 不正确 或者 postId == null";


    /**
     * 所有事件的集合
     */
    public interface EVENT {
        /**
         * 开始请求
         */
        public static final int START = 10;

        /**
         * 填充 or load
         */
        public static final int LOAD = 20;

        /**
         * 展示PV
         */
        public static final int SHOW = 30;

        /**
         * 点击
         */
        public static final int CLICK = 40;



        /**
         * 开始展示
         */
        public static final int START_SHOW = 80;

        /**
         * 关闭
         */
        public static final int CLOSE = 81;

        /**
         * 缓存
         */
        public static final int CACHE = 82;

        /**
         * 完成
         */
        public static final int COMPLETE = 83;

        /**
         * 跳过
         */
        public static final int SKIP = 84;

        /**
         * 渲染成功
         */
        public static final int RENDER_SUCCESS = 85;

        /**
         * 奖励
         */
        public static final int REWARD_VERIFY = 86;

        /**
         * 广告展开遮盖时调用
         */
        public static final int GDT_OPEN_OVERLAY = 87;

        /**
         * 广告关闭遮盖时调用
         */
        public static final int GDT_CLOSE_OVERLAY = 88;

        /**
         * 广点通广告显示
         */
        public static final int GDT_SHOW = 89;

        /**
         * 广点通广告显示
         */
        public static final int GDT_LEFT_APPLICATION = 90;

        /**
         * 错误
         */
        public static final int ERROR = 100;
    }

    /**
     * 上传的广告类型
     */
    public interface STYLE {

        /**
         * 启动页广告
         */
        public static final int SPLASH = 1;


        /**
         * 插入广告
         */
        public static final int INTERACTION_EXPRESS = 2;

        /**
         * 本地信息流
         */
        public static final int NATIVE_EXPRESS = 3;


        /**
         * 激励视频
         */
        public static final int REWARD = 4;

        /**
         * 全屏视频
         */
        public static final int FULL_SCREEN = 5;
    }


    public interface ERROR {
        /**
         * 取消
         */
        public static final int TYPE_CANCEL = 10000;

        /**
         * 超时
         */
        public static final int TYPE_TIMEOUT = 10001;

        /**
         * 普通出错
         */
        public static final int TYPE_ERROR = 10002;

        /**
         * 渲染出错
         */
        public static final int TYPE_RENDER_ERROR = 10003;


        /**
         * 获取的广告为空
         */
        public static final int TYPE_LOAD_EMPTY_ERROR = 10004;

        /**
         * postId 获取的时候为空错误
         */
        public static final int TYPE_POSTID_EMPTY_ERROR = SDK_CODE_10008;



    }
}
