package com.kingz.base.response

interface BaseResponseCode {

    companion object {
        /**
         * 成功
         */
        const val SUCCESS = 0

        /**
         * 参数错误
         */
        const val PARAMS_ERROR = "400"

        /**
         * 没有登录（token错误）
         */
        const val AUTH_ERROR = "401"

        /**
         * 请求被拒绝，token过期
         */
        const val FORBIDDEN_ERROR = "403"

        /**
         * 资源未找到
         */
        const val NOT_FOUND_ERROR = "404"

        /**
         * 服务器错误
         */
        const val SERVER_ERROR = "500"
    }
}