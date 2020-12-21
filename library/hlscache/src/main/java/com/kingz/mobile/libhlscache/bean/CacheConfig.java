package com.kingz.mobile.libhlscache.bean;

/**
 * 模块配置。
 * Created 2017/11/13.
 */
public class CacheConfig {
    private String cachePath;

    public CacheConfig(String cachePath) {
        this.cachePath = cachePath;
    }

    public String getCachePath() {
        return cachePath;
    }
}
