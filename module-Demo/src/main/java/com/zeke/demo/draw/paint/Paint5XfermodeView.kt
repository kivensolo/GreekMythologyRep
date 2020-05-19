package com.zeke.demo.draw.paint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.zeke.demo.BasePracticeView


/**
 * author: King.Z <br>
 * date:  2020/3/15 14:27 <br>
 * description:  <br>
 *     "Xfermode" 其实就是 "Transfer mode"，用 "X" 来代替 "Trans" 是一些美国人喜欢用的简写方式。
 *     通俗地说，其实就是要以绘制的内容作为源图像，以 View 中已有的内容作为目标图像，
 *     选取一个 PorterDuff.Mode 作为绘制内容的颜色处理方案。
 *
 *  重点在离屏缓冲：
 *     Canvas.saveLayer()  可以做短时的离屏缓冲
 *
 *     View.setLayerType() 是直接把整个 View 都绘制在离屏缓冲中。
 *          setLayerType(LAYER_TYPE_HARDWARE) 是使用 GPU 来缓冲，
 *          setLayerType(LAYER_TYPE_SOFTWARE) 是直接直接用一个 Bitmap 来缓冲。
 */

class Paint5XfermodeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {
    var rectBitmap:Bitmap = Bitmap.createBitmap(250,250,Bitmap.Config.RGB_565)
    var circleBitmap:Bitmap = Bitmap.createBitmap(250,250,Bitmap.Config.RGB_565)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // saveLayer() 可以做短时的离屏缓冲
        val saved = canvas.saveLayer(RectF(clipBounds), paint)
        paint.color = Color.BLUE
        canvas.drawBitmap(rectBitmap, 50f, 50f, paint)

        paint.color = Color.RED
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawBitmap(circleBitmap, 150f, 150f, paint)
        paint.xfermode = null

        canvas.restoreToCount(saved)
    }
}