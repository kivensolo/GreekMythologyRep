package com.kingz.base.response

class BaseResponse<T> {
    var errorCode: Int = 0
    var errorMsg: String = ""
    var data: T? = null
}