package com.mobi.core;

import android.content.Context;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/8 15:16
 * @Dec 给予广告session进行使用，然后通过反射
 * 来创建对应的session，进行初始化判断
 */
public interface IAdSession {
    /**
     * 初始化
     * @param context
     * @param appId
     * @param appName
     * @param isDebug
     */
    void init(Context context, String appId, String appName, boolean isDebug);

    /**
     * 是否初始化成功
     * @return
     */
    boolean isInit();


    /**
     * 获取上下文
     * @return
     */
    Context getContext();

}
