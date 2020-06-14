package com.kingz.library.container

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import com.kingz.library.entity.ScreenMode
import com.kingz.library.player.IPlayer
import com.kingz.library.player.exo.ExoPlayer
import com.kingz.library.player.render.IRender
import com.kingz.library.player.render.SurfaceRenderCallback
import com.kingz.library.player.render.TextureViewRender
import test.chinaiptv.com.player_library.R

/**
 * @author zeke.wang
 * @date 2020/4/28
 * @maintainer zeke.wang
 * @desc:
 * 支持小窗的PlayerView容器
 * NOTE: parent容器需限定为FrameLayout类型
 */
open class PlayerView( context: Context, val parent:FrameLayout, val viewRect: Rect? = null,
        val player: IPlayer = ExoPlayer(context),
        render: IRender = TextureViewRender(context, player),
        var isLargestView: (isFullScreen: Boolean, playerView: PlayerView) -> Unit = { _, _ -> },
        val containerSizeChange: ((rect: Rect) -> Unit) = { _ -> },
        var onBackDown: (playerView: PlayerView) -> Boolean = { false }
) : FrameLayout(context), SurfaceRenderCallback {

    companion object {
        private const val TAG = "PlayerView"
    }

    //播放器视图的显示模式(默认为小屏)
    private var screenMode: ScreenMode = ScreenMode.SMALL
    private var drawFocus = true
    private var focusLineDrawable: Drawable? = null

    init {
        Log.d(TAG, "init")
        clipToPadding = false
        clipChildren = false

        focusLineDrawable = context.resources.getDrawable(R.drawable.play_focus_bg)

        val pms = LayoutParams(1, 1)
        viewRect?.apply {
            pms.width = width
            pms.height = height
            pms.leftMargin = left
            pms.topMargin = top
        }
        parent.addView(this,pms)

        render.renderCallback = this
        addView(render as View, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    //Player callback

    //Render Callback
    override fun surfaceChanged(width: Int, height: Int) {
        Log.d(TAG,"surfaceChanged() [$width * $height]")
        isLargestView(isFullScreen(), this)
    }

    override fun surfaceDestroyed() {
        Log.d(TAG,"surfaceDestroyed()")
    }

    override fun surfaceCreated() {
        Log.d(TAG,"surfaceCreated()")
    }

    fun isFullScreen():Boolean {
        return screenMode == ScreenMode.FULL
    }

    fun setScreenMode(mode:ScreenMode){
        drawFocus = (mode == ScreenMode.SMALL)
        screenMode = mode
        requestLayout()
    }

    /**
     * 调整Surface大小为铺满父容器(不一定是全屏状态)
     */
    fun changeSurfaceSizeFull() {
        adjustSurfaceSize(MATCH_PARENT, MATCH_PARENT, 0, 0)
    }

    /**
     * 调整Surface大小为最小
     */
    fun changeSurfaceSizeSmallest() {
        adjustSurfaceSize(1, 1, 0, 0)
    }

    /**
     * 调整Surface在parent的位置、大小
     */
    fun adjustSurfaceSize(width: Int, height: Int, marginLeft: Int, marginTop: Int) {
        layoutParams = (layoutParams as LayoutParams).apply {
            this.width = width
            this.height = height
            this.leftMargin = marginLeft
            this.topMargin = marginTop

            containerSizeChange(Rect(marginLeft,
                    marginTop,
                    marginLeft + width,
                    marginTop + height))
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        //焦点框绘制在最上层
        if (drawFocus) {
            drawFrame(canvas, width, height)
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        //全屏模式 控制蒙层存在 按键事件分发
        return if (isFullScreen()) {
            if (viewRect != null && event.action == KeyEvent.ACTION_DOWN
                    && event.keyCode == KeyEvent.KEYCODE_BACK) {
                if (onBackDown(this)) {
                    return true
                }
            }
            return super.dispatchKeyEvent(event)
        } else {
            super.dispatchKeyEvent(event)
        }
    }

    private fun drawFrame(canvas: Canvas, w: Int, h: Int) { //焦点框绘制在最上层
        if (hasFocus()) {
            Log.d(TAG, "draw focus Frame")
            drawDrawable(canvas, focusLineDrawable, w, h)
        }
    }

    private val rect = Rect(0, 0, 0, 0)

    private fun drawDrawable(canvas: Canvas, drawable: Drawable?, w: Int, h: Int) {
        if (drawable == null) {
            return
        }
        drawable.getPadding(rect)
        //焦点框距中间图片的间距
        val padding = 0
        drawable.setBounds(
            -rect.left - padding, -rect.top - padding, w + rect.right + padding,
            h + rect.bottom + padding
        )
        drawable.draw(canvas)
    }

}