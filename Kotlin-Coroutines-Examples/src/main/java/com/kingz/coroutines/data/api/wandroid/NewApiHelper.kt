package com.kingz.coroutines.data.api.wandroid

import com.kingz.coroutines.data.api.ApiServiceUtil

/**
 * @author zeke.wang
 * @date 2020/7/7
 * @maintainer zeke.wang
 * @desc:
 */
object NewApiHelper {
//    val wanAndroidService:WAndroidApi = ApiServiceUtil.getApiService()
    val wanAndroidService = ApiServiceUtil.getApiService<WAndroidApi>()

}