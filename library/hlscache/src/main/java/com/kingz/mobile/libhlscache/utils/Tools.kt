package com.kingz.mobile.libhlscache.utils

object Tools {
    @JvmStatic
    fun getAbsoluteUrl(baseUrl: String, rawUrl: String?): String {
        return if (rawUrl?.startsWith("http") == true) {
            rawUrl
        } else {
            baseUrl.substring(0, baseUrl.lastIndexOf('/')) + rawUrl
        }
    }
}