package com.mobi.core;

import com.mobi.core.strategy.impl.ServiceOrderShowAdStrategy;
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
            "tt", "gdt", "ks", "mobisdk", "admob"};
    public static final String TYPE_CSJ = SKD_PLATFORM[0];
    public static final String TYPE_CSJ_PATH = "com.mobi.csj.CsjSession";
    public static final String TYPE_GDT = SKD_PLATFORM[1];
    public static final String TYPE_GDT_PATH = "com.mobi.gdt.GdtSession";
    public static final String TYPE_KS = SKD_PLATFORM[2];
    public static final String TYPE_KS_PATH = "com.mobi.ks.KsSession";
    public static final String TYPE_MOBI_SDK = SKD_PLATFORM[3];
    public static final String TYPE_MOBIS_SDK_PATH = "com.mobi.mobiexchanger.MobiAdSession";
    public static final String TYPE_ADMOB = SKD_PLATFORM[4];
    public static final String TYPE_ADMOB_PATH = "com.mobi.admob.AdMobSession";

    private Map<String, ILazyCreateProvider> mAdProviderMap;
    /**
     * 用于存放 所有session的单例实例，这样来判断
     * 是否存在对应的广告引入
     */
    private Map<String, IAdSession> mAdSessionMap;

    /**
     * 执行广告下标，给 {@link ServiceOrderShowAdStrategy}
     * 使用 key ： postId
     * value : sort_parameter[] 的下标
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

    public void putCreateProvider(String key, ILazyCreateProvider provider) {
        if (mAdProviderMap != null) {
            // 不包含，或者已经为null了
            //用于防止重复创建
            if (!mAdProviderMap.containsKey(key)
                    || mAdProviderMap.get(key) == null) {
                mAdProviderMap.put(key, provider);
            }
        }
    }

    public void removeCreateProvider(String key) {
        if (mAdProviderMap != null) {
            mAdProviderMap.remove(key);
        }
    }

    public IAdProvider getProvider(String key) {
        ILazyCreateProvider createProvider = mAdProviderMap.get(key);
        if (createProvider != null) {
            return createProvider.create();
        }
        return null;
    }

    @Deprecated
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

    /**
     * 由于不知道外界怎么实现的，所以只能让外界进行初始化操作
     * 然后new 对象。
     * 关键前面用唯一性来解决的时候，发现公用了一个Provider
     * 会导致公用的Provider codeId和md5 会被覆盖这个问题
     * 所以用这个接口来进行重复创建
     */
    public interface ILazyCreateProvider {
        IAdProvider create();
    }

}
