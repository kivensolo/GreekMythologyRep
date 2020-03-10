package com.zeke.ktx.cache


import com.github.xulcache.CacheCenter
import com.github.xulcache.CacheDomain
import com.zeke.ktx.App

class AppCacheManager {

    private var memCache: CacheDomain? = null
    private var persistentCache: CacheDomain? = null

    init {
        memCache = CacheCenter
                .buildCacheDomain(CommonCacheId.ID_CACHE_MEMORY,
                        App.instance!!.applicationContext)
                .setDomainFlags(CacheCenter.CACHE_FLAG_MEMORY)
                .setMaxMemorySize(10 * 1024 * 1024L) //10M
                .build()

        persistentCache = CacheCenter
                .buildCacheDomain(CommonCacheId.ID_CAHCE_PERSISTENT,
                        App.instance!!.applicationContext)
                .setDomainFlags(CacheCenter.CACHE_FLAG_REVISION_LOCAL
                        or CacheCenter.CACHE_FLAG_PERSISTENT
                        or CacheCenter.CACHE_FLAG_PROPERTY)
                .build()
    }

    companion object {

        // 播放器纵横比
        const val ASPECT_RATIO = "aspect_ratio"
        internal var instance = AppCacheManager()

        fun persistentString(k: String, v: String) {
            instance.persistentCache!!.put(k, v)
        }

        fun loadPersistentString(k: String): String {
            return instance.persistentCache!!.getAsString(k)
        }

        fun cacheString(k: String, v: String) {
            instance.memCache!!.put(k, v)
        }

        fun getCachedString(k: String): String {
            return instance.memCache!!.getAsString(k)
        }

        fun <T> cacheObject(k: String, v: T) {
            instance.memCache!!.put(k, v)
        }

        fun <T> getCachedObject(k: String): T {
            return instance.memCache!!.getAsObject(k) as T
        }

        fun removePersistentCache(k: String) {
            instance.persistentCache!!.remove(k)
        }
    }
}
