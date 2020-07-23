package com.mobi.core.network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 14:11
 * @Dec 略
 */
public class SdkExecutors {
    public final static ExecutorService SDK_THREAD_POOL;

    /**
     * 用于数据库操作
     */
    public final static ExecutorService DB_OPERATION_POOL;

    static {
        SDK_THREAD_POOL = Executors.newCachedThreadPool();
        DB_OPERATION_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }

}
