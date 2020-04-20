package com.zeke.ktx.api.callback

/**
 * author：KingZ
 * date：2020/2/22
 * description：数据层回调接口
 */
interface IDataResponse<T> {
    fun onSuccess(data: T)
    fun onError(code: Int, msg: String, data: T)
}