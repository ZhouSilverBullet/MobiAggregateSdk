package com.mobi.core.network;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/7/23 15:19
 * @Dec 网络上报的分发器，防止OOM
 * java.lang.OutOfMemoryError: pthread_create (1040KB stack) failed: Out of memory
 * at java.lang.Thread.nativeCreate(Native Method)
 */
public class Dispatcher {
    //运行的队列
    private ArrayDeque<Runnable> mRunningDeque = new ArrayDeque<>();
    //等待的队列
    private ArrayDeque<Runnable> mReadyDeque = new ArrayDeque<>();
    //最大是64
    private int maxSize = 64;

    private final static ExecutorService DEFAULT_POOL = SdkExecutors.SDK_THREAD_POOL;

    public Dispatcher() {
    }

    public synchronized void execute(Runnable runnable) {
        if (mRunningDeque.size() < maxSize) {
            mRunningDeque.add(runnable);
            DEFAULT_POOL.execute(runnable);
        } else {
            mReadyDeque.add(runnable);
        }
    }

    public synchronized void finishRunnable(Runnable runnable) {
        //移除当前任务
        mRunningDeque.remove(runnable);
        if (mRunningDeque.isEmpty()) return;

        if (mRunningDeque.size() < maxSize) {
            for (Iterator<Runnable> i = mReadyDeque.iterator(); i.hasNext(); ) {
                Runnable next = i.next();
                if (mRunningDeque.size() < maxSize) {
                    i.remove();
                    mRunningDeque.add(next);
                    //最终执行代码
                    DEFAULT_POOL.execute(runnable);
                } else {
                    //停止添加
                    break;
                }
            }
        }
    }

}
