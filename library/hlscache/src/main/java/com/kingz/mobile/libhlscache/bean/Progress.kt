package com.kingz.mobile.libhlscache.bean

import kotlin.math.abs

/**
 * 下载进度
 */
data class Progress(
    /**
     * @return 视频 id , 为外部传入的 id 值.
     */
    var id: String,
    /**
     * 视频已经下载的时长.
     *
     * @return 时长，单位 s
     */
    val downloadedDuration: Float,
    /**
     * 视频总时长.
     *
     * @return 总时长，单位 s
     */
    val duration: Float,
    /**
     * 已经下载的字节数，不精确.
     *
     * @return 字节数，单位 byte
     */
    val downloadedBytes: Int,
    /**
     * 视频总字节数，不精确.
     *
     * @return 字节数，单位 byte
     */
    val totalBytes: Int,
    /**
     * 下载状态
     *
     * @see VideoInfo.DownLoadState
     */
    val state: Int
) {

    /**
     * @return 是否下载完成
     */
    val isDownloadFinish: Boolean
        get() = abs(duration - downloadedDuration) < 0.01

}