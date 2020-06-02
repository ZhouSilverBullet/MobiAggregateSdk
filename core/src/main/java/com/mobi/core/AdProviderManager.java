package com.mobi.core;

import com.mobi.core.utils.LogUtils;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/2 20:45
 * @Dec 略
 */
public class AdProviderManager {
    public static final String TAG = "AdProviderManager";
    public static final String TYPE_CSJ = "CsjProvider";
    public static final String TYPE_GDT = "GdtProvider";

    private Map<String, IAdProvider> mAdProviderMap;

    private AdProviderManager() {
        mAdProviderMap = new ConcurrentHashMap<>();
    }

    public static AdProviderManager get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final AdProviderManager INSTANCE = new AdProviderManager();
    }

    public void putProvider(String key, IAdProvider provider) {
        if (mAdProviderMap != null) {
            mAdProviderMap.put(key, provider);
        }
    }

    public void removeProvider(String key) {
        if (mAdProviderMap != null) {
            mAdProviderMap.remove(key);
        }
    }

    public IAdProvider getProvider(String key) {
        return mAdProviderMap.get(key);
    }

    String getProviderKey() {
        Random random = new Random();
        int i = random.nextInt(100);
        LogUtils.i(TAG, " getProviderKey " + i);
        return i % 2 == 0 ? TYPE_CSJ : TYPE_GDT;
    }
}
