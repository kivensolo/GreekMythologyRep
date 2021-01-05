package com.kingz.mobile.libhlscache.bean

/**
 * 缓存影片配置
 */
class CacheVideoConfig {
    var forwardCacheTime: Int // 单位秒，上取整
        private set

    constructor(cacheTime: Int) {
        forwardCacheTime = cacheTime
    }

    constructor() {
        forwardCacheTime = Int.MAX_VALUE
    }

}