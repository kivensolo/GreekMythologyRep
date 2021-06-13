package com.kingz.library.player.render

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.TextureView
import com.kingz.library.player.IPlayer

/**
 * TextureView Player
 */
@SuppressLint("ViewConstructor")
class TextureViewRender( context: Context, override val iPlayer: IPlayer) :
        TextureView(context),
        TextureView.SurfaceTextureListener,
        IRender{
//        IPlayer by iPlayer {

    override var renderCallback: SurfaceRenderCallback? = null

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        renderCallback?.surfaceChanged(width, height)
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        renderCallback?.surfaceDestroyed()
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        renderCallback?.surfaceCreated()
        iPlayer.setSurface(Surface(surfaceTexture))
    }

    init {
        // TextureView.setSurfaceTextureListener(SurfaceTextureListener listener)
        surfaceTextureListener = this
    }

}