package com.zeke.network.response

/**
 * author：KingZ
 * date：2020/2/22
 * description：数据层回调接口
 */
interface IRequestResponse<T> {
    fun onSuccess(data: T)
    fun onError(code: Int, msg: String, data: T)
}