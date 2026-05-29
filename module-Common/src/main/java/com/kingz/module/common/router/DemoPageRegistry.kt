package com.kingz.module.common.router

/**
 * Demo页面路径注册表
 *
 * key: 稳定的页面标识（NavigationData中使用）
 * value: Activity 的完整类路径
 *
 * 由各业务模块通过 DemoPageConfig 注册自身的页面路径，
 * 上层模块通过 key 查找，无需感知具体的类路径变化。
 */
object DemoPageRegistry {

    private val pageMap = mutableMapOf<String, String>()

    fun register(key: String, clazz: Class<*>) {
        pageMap[key] = clazz.name
    }

    fun getPath(key: String): String? = pageMap[key]

    fun contains(key: String): Boolean = pageMap.containsKey(key)
}
