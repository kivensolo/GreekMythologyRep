package com.zeke.ktx.cache

object CommonCacheId {

    private val ID_BASE = 0x2000

    // APP级别缓存ID
    val ID_API = ID_BASE + 1
    val ID_CACHE_MEMORY = ID_BASE + 2
    val ID_CAHCE_PERSISTENT = ID_BASE + 3
    val ID_CAHCE_PERSISTENT_GLOBAL = ID_BASE + 4

    // APP测试缓存域id
    val ID_CAHCE_TEST_DEMO = ID_BASE + 5

    // 具体业务缓存ID eg:
    val ID_USER_HISTORY_BASE = 0x10000000
    val ID_USER_FAVORITES_BASE = 0x20000000
    val ID_USER_FOLLOWS_BASE = 0x30000000

    private fun check(id:Int){
        when(id){
            ID_API,
            ID_CACHE_MEMORY,
            ID_CAHCE_PERSISTENT,
            ID_CAHCE_PERSISTENT_GLOBAL,
            ID_CAHCE_TEST_DEMO,
            ID_USER_HISTORY_BASE,
            ID_USER_FAVORITES_BASE,
            ID_USER_FOLLOWS_BASE -> {
            }
        }
    }
}
