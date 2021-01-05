package com.kingz.mobile.libhlscache.bean

import com.iheartradio.m3u8.data.PlaylistData
import com.kingz.mobile.libhlscache.utils.Tools
import java.util.*

/**
 * 多路流顶层信息
 */
data class MasterInfo(val id: String,
                 private val url: String,
                 val config: CacheVideoConfig) {

    private val subInfos: MutableList<SubInfo> = ArrayList()

    /**
     * 当前正在播放/缓存的流index
     */
    var curPlayIndex = 0

    /**
     * 当前播放的SubInfo
     */
    val curPlaySubInfo: SubInfo
        get() = subInfos[curPlayIndex]

    fun addSubInfos(playlists: List<PlaylistData>) {
        for (data in playlists) {
            addSubInfo(data.uri, data.streamInfo.bandwidth)
        }
    }

    private fun addSubInfo(url: String, bandWidth: Int) {
        val subInfo = SubInfo()
        subInfo.rawUrl = url
        subInfo.bandWidth = bandWidth
        subInfo.id =  "${id}_____${bandWidth}"
        subInfos.add(subInfo)
    }

    fun getSubInfos(): List<SubInfo> {
        return subInfos
    }

    inner class SubInfo {
        var bandWidth = 0
        var id: String? = null
        var rawUrl: String? = null
        val absoluteUrl: String
            get() = Tools.getAbsoluteUrl(url, rawUrl)
    }

}