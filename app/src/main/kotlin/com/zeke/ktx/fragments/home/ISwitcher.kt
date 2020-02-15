package com.zeke.ktx.fragments.home

import android.support.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * description：Fragment切换的接口
 */
interface ISwitcher {

    @Retention(RetentionPolicy.SOURCE)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    @IntDef(TYPE_VOD,TYPE_LIVE,
            TYPE_VIP,TYPE_MINE)
    annotation class ButtomType

    fun switchFragment(@ButtomType type: Int)

    companion object {

        const val TYPE_VOD = 0
        const val TYPE_LIVE = 1
        const val TYPE_VIP = 2
        const val TYPE_MINE = 3
    }
}
