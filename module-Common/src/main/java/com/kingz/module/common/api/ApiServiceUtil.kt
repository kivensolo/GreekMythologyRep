package com.kingz.module.common.api

import com.zeke.network.retrofit.mannager.Api

/**
 * @author zeke.wang
 * @date 2020/7/7
 * @maintainer zeke.wang
 * @desc: ApiService创建的工具类
 */
@Deprecated(message = "获取API类型的方式由BaseRemoteDataSource类提供," +
        "包括API缓存逻辑")
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