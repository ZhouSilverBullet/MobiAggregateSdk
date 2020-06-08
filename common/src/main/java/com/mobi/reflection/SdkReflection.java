package com.mobi.reflection;

import android.content.Context;

import com.mobi.core.IAdSession;

import java.lang.reflect.Method;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/8 15:07
 * @Dec 略
 */
public class SdkReflection {
    /**
     * com.mobi.csj.CsjSession
     *
     * @param clazzName
     * @return
     */
    public static Object findInitSession(Context context,
                                             String clazzName,
                                             String appId,
                                             String appName,
                                             boolean isDebug) {

        try {
//            final String className = "CsjSession";
            final Class<?> sessionClass = Class.forName(clazzName);
            System.out.println(sessionClass);
            final Method getMethod = sessionClass.getMethod("get");
            getMethod.setAccessible(true);
            //获取到session对象了
            final IAdSession sessionInstance = (IAdSession)getMethod.invoke(null);

            final Method initMethod = sessionClass.getMethod("init", Context.class, String.class, String.class, boolean.class);
            initMethod.invoke(sessionInstance, context, appId, appName, isDebug);

            final Method isInitMethod = sessionClass.getMethod("isInit");
            final boolean isInit = (boolean) isInitMethod.invoke(sessionInstance);
            System.out.println(isInit);
            return sessionInstance;
        } catch (ClassNotFoundException e) {
//            System.out.println(e);
            return null;
        } catch (Exception e) {

        }
        return false;
    }
}
