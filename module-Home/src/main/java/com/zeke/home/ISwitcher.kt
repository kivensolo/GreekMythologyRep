package com.zeke.home

import android.view.View
import androidx.annotation.IntDef

/**
 * description：Fragment切换的接口
 */
interface ISwitcher : View.OnClickListener{

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    @IntDef(
        TYPE_KNOWLEGA,
        TYPE_LIVE,
        TYPE_EYEPETIZER,
        TYPE_MINE
    )
    annotation class ButtomType

    fun switchFragment(@ButtomType type: Int)

    companion object {
        const val TYPE_KNOWLEGA = 0
        const val TYPE_LIVE = 1
        const val TYPE_EYEPETIZER = 2
        const val TYPE_MINE = 3
    }
}
