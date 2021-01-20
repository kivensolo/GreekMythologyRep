package com.zeke.play.constants

import androidx.annotation.StringDef

/**
 * author：ZekeWang
 * date：2021/1/20
 * description：
 */
@StringDef(PlayMode.LIVE, PlayMode.VOD)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
internal annotation class PlayMode {
    companion object {
        const val LIVE = "live"
        const val  VOD = "vod"
    }
}