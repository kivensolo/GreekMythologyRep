package com.zeke.demo.draw.paint

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import com.zeke.demo.R
import com.zeke.demo.base.BasePracticeView
import com.zeke.kangaroo.utils.BitMapUtils
import com.zeke.kangaroo.utils.UIUtils


/**
 * author: King.Z <br>
 * date:  2020/3/15 14:27 <br>
 * description:  <br>
 *     setShadowLayer(float radius, float dx, float dy, int shadowColor)
 *     radius 是阴影的模糊范围； dx dy 是阴影的偏移量； shadowColor 是阴影的颜色。
 *     注意事项：
 *     - 在硬件加速开启的情况下， setShadowLayer() 只支持文字的绘制，
 *       文字之外的绘制必须关闭硬件加速才能正常绘制阴影。
 *     - 如果ArrayObjectAdapter shadowColor 是半透明的，阴影的透明度就使用 shadowColor 自己的透明度；
 *       而如果 shadowColor 是不透明的，阴影的透明度就使用 paint 的透明度。
 *
 *     如果要清除阴影层，使用 clearShadowLayer() 。
 */

class Paint8ShadowLayerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {
    var bitmap: Bitmap = BitMapUtils.decodeResource(resources, R.drawable.arduino, 350,280)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val zoomImg = BitMapUtils.setZoomImg(bitmap, 350, 280)
        paint.textSize = UIUtils.dip2px(context,32f).toFloat()
        paint.setShadowLayer(10f, 0f, 0f, Color.RED)
        canvas.drawText("JetPack", 80f, 180f, paint)
        canvas.drawBitmap(zoomImg, 500f, 50f, paint)

    }
}