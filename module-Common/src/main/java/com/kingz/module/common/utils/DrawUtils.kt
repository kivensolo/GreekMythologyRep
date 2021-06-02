package com.kingz.module.common.utils

import android.graphics.Rect
import android.graphics.RectF

/**
 * author：ZekeWang
 * date：2021/6/2
 * description：绘制相关的工具类
 */
object DrawUtils {
// <editor-fold defaultstate="collapsed" desc="Rect Utils">

    // <editor-fold defaultstate="collapsed" desc="Rect Offset">
    fun offsetRectF(rc: RectF, offX: Float, offY: Float) {
        rc.left += offX
        rc.right += offX
        rc.top += offY
        rc.bottom += offY
    }

    fun offsetRect(rc: Rect, offX: Int, offY: Int) {
        rc.left += offX
        rc.right += offX
        rc.top += offY
        rc.bottom += offY
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Rect Copy">
    fun copyRect(src: RectF, dst: Rect) {
        dst.left = roundToInt(src.left)
        dst.top = roundToInt(src.top)
        dst.right = roundToInt(src.right)
        dst.bottom = roundToInt(src.bottom)
    }

    fun copyRect(src: RectF, dst: RectF) {
        dst.left = src.left
        dst.top = src.top
        dst.right = src.right
        dst.bottom = src.bottom
    }

    fun copyRect(src: Rect, dst: RectF) {
        dst.left = src.left.toFloat()
        dst.top = src.top.toFloat()
        dst.right = src.right.toFloat()
        dst.bottom = src.bottom.toFloat()
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Rect Cal">
    fun calRectWidth(rc: Rect): Int {
        return rc.right - rc.left
    }

    fun calRectWidth(rc: RectF): Float {
        return rc.right - rc.left
    }

    fun calRectHeight(rc: Rect): Int {
        return rc.bottom - rc.top
    }

    fun calRectHeight(rc: RectF): Float {
        return rc.bottom - rc.top
    }
// </editor-fold>

    // </editor-fold>

    private fun roundToInt(`val`: Float): Int {
        return if (`val` >= 0) {
            (`val` + 0.5f).toInt()
        } else {
            (`val` - 0.5f).toInt()
        }
    }
}