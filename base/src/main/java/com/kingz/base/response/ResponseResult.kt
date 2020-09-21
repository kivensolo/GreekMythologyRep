package com.kingz.base.response

data class ResponseResult<out T>(val status: Status,
                                 val data: T?,
                                 val message: String?) {

    companion object {

        fun <T> response(data: T?): ResponseResult<T> {
            if(data != null){
                if(true){ // 判断数据code是不是业务code
                    return success(data)
                }else{ // 业务不正常的code
                    return failure(data,"错误信息从返回中拿")
                }
            }
            return error("接口数据异常", null)
        }

        fun <T> success(data: T?): ResponseResult<T> {
            return ResponseResult(Status.SUCCESS, data, null)
        }

        fun <T> failure(data: T?,msg: String): ResponseResult<T> {
            return ResponseResult(Status.FAILURE, data, msg)
        }

        fun <T> error(msg: String, data: T?): ResponseResult<T> {
            return ResponseResult(Status.ERROR, data, msg)
        }

        fun <T> loading(): ResponseResult<T> {
            return ResponseResult(Status.LOADING, null, null)
        }

        fun <T> loading(data: T?): ResponseResult<T> {
            return ResponseResult(Status.LOADING, data, null)
        }
    }

}