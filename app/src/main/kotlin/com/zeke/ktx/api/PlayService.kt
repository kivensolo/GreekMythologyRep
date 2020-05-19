package com.zeke.ktx.api

import com.zeke.home.entity.VideoItemData

interface PlayService {
    fun getVideoUrl(url: String,callBack: GetVideoUrlCallBack)

    fun getWebVideoUrl(url: String,callBack: GetVideoUrlCallBack)

    interface GetVideoUrlCallBack {
        fun videoUrl(url: String, plotText: String)
        fun videoWebData(data: List<VideoItemData>, plotText: String)
    }
}