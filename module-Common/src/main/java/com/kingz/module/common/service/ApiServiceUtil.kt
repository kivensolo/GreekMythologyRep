package com.kingz.module.common.service

import com.zeke.network.retrofit.mannager.Api

/**
 * @author zeke.wang
 * @date 2020/7/7
 * @maintainer zeke.wang
 * @desc: ApiService创建的工具类
 */
object ApiServiceUtil {

    private val map = mutableMapOf<Class<*>, Any?>()

    inline fun <reified T> getApiService() =
        getServiceByType(T::class.java)

    fun <T> getServiceByType(type: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return if (map.containsKey(type)) {
            map[type] as T
        } else {
            val tempApi = Api.getInstance().build().create(type)
            map[type] = tempApi
            tempApi
        }
    }
}