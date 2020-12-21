package com.kingz.mobile.libhlscache.bean;

/**
 * 影片配置。
 * Created 2017/11/8.
 */
public class CacheVideoConfig {
    private int forwardCacheTime;  // 单位秒，上取整

    public CacheVideoConfig(int forwardCacheTime) {
        this.forwardCacheTime = forwardCacheTime;
    }

    public CacheVideoConfig() {
        forwardCacheTime = Integer.MAX_VALUE;
    }

    public int getForwardCacheTime() {
        return forwardCacheTime;
    }
}
