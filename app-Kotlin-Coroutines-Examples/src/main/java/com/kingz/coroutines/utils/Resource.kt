package com.kingz.coroutines.utils

import com.kingz.base.response.ResponseResult
import com.kingz.base.response.Status

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): ResponseResult<T> {
            return ResponseResult(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): ResponseResult<T> {
            return ResponseResult(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): ResponseResult<T> {
            return ResponseResult(Status.LOADING, data, null)
        }

    }

}