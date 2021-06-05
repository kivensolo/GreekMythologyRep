package com.kingz.module.common.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * author：KingZ
 * date：2019/7/31
 * description：媒体资源参数Bean
 */
class MediaParams : Parcelable {
    var videoId: String? = null
    var videoType: String? = null
    var videoUrl: String? = null
    var videoName: String? = null
    var videoBkg: String? = null

    constructor()
    protected constructor(`in`: Parcel) {
        videoId = `in`.readString()
        videoType = `in`.readString()
        videoUrl = `in`.readString()
        videoName = `in`.readString()
        videoBkg = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    /**
     * 序列化过程
     */
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(videoId)
        dest.writeString(videoType)
        dest.writeString(videoUrl)
        dest.writeString(videoName)
        dest.writeString(videoBkg)
    }

    enum class VideoType {
        VOD, LIVE
    }

    enum class QualityType(var qualityName: String) {
        LOW("流畅 180P"), STD("标清 270P"), HD("高清 480P"), SD("超清 720P"), UHD("蓝光 1080P");

        companion object {
            fun value(quality: String?): QualityType {
                if ("LOW".equals(quality, ignoreCase = true)) {
                    return LOW
                } else if ("STD".equals(quality, ignoreCase = true)) {
                    return STD
                } else if ("HD".equals(quality, ignoreCase = true)) {
                    return HD
                } else if ("SD".equals(quality, ignoreCase = true)) {
                    return SD
                } else if ("4K".equals(quality, ignoreCase = true)) {
                    return UHD
                }
                return STD
            }
        }

    }

    companion object CREATOR : Parcelable.Creator<MediaParams> {
        const val PARAMS_KEY = "playParams"

        override fun createFromParcel(parcel: Parcel): MediaParams {
            return MediaParams(parcel)
        }

        override fun newArray(size: Int): Array<MediaParams?> {
            return arrayOfNulls(size)
        }
    }
}