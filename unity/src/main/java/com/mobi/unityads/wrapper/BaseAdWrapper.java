package com.mobi.unityads.wrapper;

import com.mobi.core.strategy.AdRunnable;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/4 10:56
 * @Dec 略
 */
public abstract class BaseAdWrapper extends AdRunnable {

    public long getPostId(String postId) {
        try {
            return Long.parseLong(postId);
        } catch (Exception e) {
            return 0L;
        }
    }

}
