package com.kingz.library.player

/**
 * Track信息
 */
class TrackInfo {
    enum class TrackType {
        AUDIO, SUBTITLE
    }

    var trackType: TrackType? = null
    var index = 0
    var language: String? = null

    override fun toString(): String {
        return "TrackInfo{mTrackType = $trackType, mIndex = $index, mLanguage = $language}"
    }
}