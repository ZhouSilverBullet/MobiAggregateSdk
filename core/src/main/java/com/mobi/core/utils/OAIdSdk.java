package com.mobi.core.utils;

import android.content.Context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/23 17:32
 * @Dec 略
 */
public class OAIdSdk {
    public static void init(Context context, final ResultCallback callback) {
        try {
            Class<?> jLibraryClazz = Class.forName("com.bun.miitmdid.core.JLibrary");
            Method initEntryMethod = jLibraryClazz.getMethod("InitEntry", Context.class);
            initEntryMethod.invoke(null, context);


            Class<?> listenerClazz = Class.forName("com.bun.supplier.IIdentifierListener");
            final Object object = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{listenerClazz}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    //方法被回调了
                    if ("OnSupport".equals(method.getName())) {
                        boolean isSupport = false;
                        String oaid = "";
                        String vaid = "";
                        String aaid = "";
                        try {
                            if (args[0] instanceof Boolean) {
                                isSupport = (boolean) args[0];
                            }
                            Method getOAID = args[1].getClass().getMethod("getOAID");
                            Method getAAID = args[1].getClass().getMethod("getAAID");
                            Method getVAID = args[1].getClass().getMethod("getVAID");
                            oaid = (String) getOAID.invoke(args[1]);
                            vaid = (String) getVAID.invoke(args[1]);
                            aaid = (String) getAAID.invoke(args[1]);
                        } finally {
                            if (callback != null) {
                                callback.onResult(isSupport, oaid, vaid, aaid);
                            }
                        }

                    }
                    return null;
                }
            });


            Class<?> sdkHelperClazz = Class.forName("com.bun.miitmdid.core.MdidSdkHelper");
            Method initSdkMethod = sdkHelperClazz.getMethod("InitSdk", Context.class, boolean.class, listenerClazz);
            initSdkMethod.invoke(null, context, true, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ResultCallback {
        void onResult(boolean isSupport, String oaid, String vaid, String aaid);
    }
}
