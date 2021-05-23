package com.kingz.playerdemo.extractor

import android.media.MediaExtractor
import android.media.MediaFormat
import android.util.Log

/**
 * author：ZekeWang
 * date：2021/4/16
 * description： Android MediaExtractor音视频的Exractor
 */
class AMExtractor(playUrl: String) {
    private val TAG: String = "PlayerDemoActivity"
    lateinit var mMediaExtractor: MediaExtractor
    var videoTrackId: Int = 0
    var audioTrackId: Int = 0
    var videoFormat: MediaFormat? = null
    var audioFormat: MediaFormat? = null

    init {
        Log.i(TAG, "Init AMExtractor with file:$playUrl")
        try {
            mMediaExtractor = MediaExtractor()
            mMediaExtractor.setDataSource(playUrl)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // 遍历轨道
        for (idx in 0 until mMediaExtractor.trackCount) {
            // 获取指定track的MediaFormat
            val mediaFormat = mMediaExtractor.getTrackFormat(idx)
            val mime = mediaFormat.getString(MediaFormat.KEY_MIME)

            if (mime.startsWith("video/")) {
                Log.w(TAG, "Find video track, MIME=$mime ; " +
                        "MediaFormat=$mediaFormat; ")
                videoTrackId = idx
                videoFormat = mediaFormat
            } else if (mime.startsWith("audio")) {
                Log.w(TAG, "Find Audio track, MIME=$mime ; " +
                        "MediaFormat=$mediaFormat; ")
                audioTrackId = idx
                audioFormat = mediaFormat
            }
        }
    }

    /**
     * 选择指定轨道
     * 由Decoder对象在Config之前调用
     */
    fun selectTrack(trackId: Int) {
        mMediaExtractor.selectTrack(trackId)
    }

    fun release(){
        mMediaExtractor.release()
    }
}