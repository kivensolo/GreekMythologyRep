package com.zeke.demo.draw.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.zeke.demo.BasePracticeView
import com.zeke.demo.R
import com.zeke.demo.model.CardItemConst

/**
 * author: King.Z <br>
 * date:  2020/3/15 14:27 <br>
 * description:  <br>
 *     展示BitmapShader着色器的效果;
 *     其实也就是用 Bitmap 的像素来作为图形或文字的填充.
 */
class Paint2BitmapShaderView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {
    var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bg10)

    private var shader: BitmapShader = BitmapShader(bitmap, // 用来做模板的 Bitmap 对象
            Shader.TileMode.CLAMP, // 横向的 TileMode
            Shader.TileMode.CLAMP) // 纵向的 TileMode
    override fun onDraw(canvas: Canvas) {
        //        练习内容：使用 BitmapShader 方法画自定义效果的Bitmap
        super.onDraw(canvas)
        paint.shader = shader
        // 只画出circle范围内的Bitmap图像(bitmap为原尺寸大小)，效果和Canvas.drawBitmap()一样
        canvas.drawCircle(550f, 300f, 250f, paint)
    }

    override fun getViewHeight(): Int {
        return CardItemConst.LARGE_HEIGHT
    }
}