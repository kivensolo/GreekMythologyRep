package com.kingz.library.player.render

import android.annotation.SuppressLint
import android.content.Context
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.kingz.library.player.IPlayer

/**
 * SurfaceView Player
 */
@SuppressLint("ViewConstructor")
class SurfaceViewRender(context: Context,
                        override val iPlayer: IPlayer) :

    SurfaceView(context), SurfaceHolder.Callback, IRender, IPlayer by iPlayer {

    override var renderCallback: RenderCallback? = null

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        renderCallback?.surfaceChanged(width, height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        renderCallback?.surfaceDestroyed()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        renderCallback?.surfaceCreated()
        iPlayer.setDisplayHolder(holder)
    }

    init {
        holder.addCallback(this)
    }

}