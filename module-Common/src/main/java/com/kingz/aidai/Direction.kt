package com.kapplication.aitest.aidai

import androidx.annotation.IntDef

object Direction {
    const val LEFT = 0
    const val TOP = 1
    const val RIGHT = 2
    const val BOTTOM = 3
    const val FRONT = 4
    const val LEFT_TOP = 5

    @IntDef(
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        FRONT,
        LEFT_TOP
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class DIRECTION
}