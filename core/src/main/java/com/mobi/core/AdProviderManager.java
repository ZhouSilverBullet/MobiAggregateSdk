package com.mobi.core;

import com.mobi.core.utils.LogUtils;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/2 20:45
 * @Dec todo 改名， AdManager
 */
public class AdProviderManager {
    public static final String TAG = "AdProviderManager";

    public static final String[] SKD_PLATFORM = new String[]{
            "tt", "gdt"};
    public static final String TYPE_CSJ = SKD_PLATFORM[0];
    public static final String TYPE_CSJ_PATH = "com.mobi.csj.CsjSession";
    public static final String TYPE_GDT = SKD_PLATFORM[1];
    public static final String TYPE_GDT_PATH = "com.mobi.gdt.GdtSession";

    private Map<String, IAdProvider> mAdProviderMap;
    /**
     * 用于存放 所有session的单例实例，这样来判断
     * 是否存在对应的广告引入
     */
    private Map<String, IAdSession> mAdSessionMap;

    /**
     * 执行广告下标，给 {@link com.mobi.core.strategy.impl.OrderShowAdStrategy}
     * 使用
     */
    private Map<String, Integer> mAdExecIndex;

    private AdProviderManager() {
        mAdProviderMap = new ConcurrentHashMap<>();
        mAdSessionMap = new ConcurrentHashMap<>();
        mAdExecIndex = new ConcurrentHashMap<>();
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
//        return TYPE_CSJ;
    }

    public void putAdSession(String key, IAdSession session) {
        mAdSessionMap.put(key, session);
    }

    public IAdSession getAdSession(String key) {
        return mAdSessionMap.get(key);
    }

    public void putAdExecIndex(String key, int index) {
        mAdExecIndex.put(key, index);
    }

    public int getAdExecIndex(String key) {
        Integer integer = mAdExecIndex.get(key);
        return integer == null ? 0 : integer;
    }

}
