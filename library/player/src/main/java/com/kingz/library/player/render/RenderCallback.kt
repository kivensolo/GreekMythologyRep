package com.kingz.library.player.render

/**
 * 渲染View回调
 */
interface RenderCallback {

    fun surfaceChanged(width: Int, height: Int)

    fun surfaceDestroyed()

    fun surfaceCreated()

}