package com.zeke.ktx.modules.home.fragments

import android.support.annotation.IntDef

/**
 * description：Fragment切换的接口
 */
interface ISwitcher {

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    @IntDef(TYPE_VOD,
            TYPE_LIVE,
            TYPE_VIP,
            TYPE_MINE)
    annotation class ButtomType

    fun switchFragment(@ButtomType type: Int)

    companion object {

        const val TYPE_VOD = 0
        const val TYPE_LIVE = 1
        const val TYPE_VIP = 2
        const val TYPE_MINE = 3
    }
}
