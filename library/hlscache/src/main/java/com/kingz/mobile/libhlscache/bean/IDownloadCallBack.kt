package com.kingz.mobile.libhlscache.bean

/**
 * 下载结果回调
 */
interface IDownloadCallBack {
    fun onDownloadError(id: String?, e: Exception?)
    fun onDownloadFinish(id: String?)
}