package com.zeke.network.domin.time

/**
 * @author zeke.wang
 * @date 2020/7/7
 * @maintainer zeke.wang
 * @desc:
 */
interface ITime {
    interface Get {
        fun connectTimeout(): Int
        fun readTimeout(): Int
        val isOpenSSLCheckValidity: Boolean
    }

    interface Set {
        fun setConnectTimeout(time: Int)
        fun setReadTimeout(time: Int)
        fun setOpenSSLCheckValidity(sslCheck: Boolean)
    }
}