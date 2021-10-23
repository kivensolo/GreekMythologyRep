package com.kingz.graphics

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.kingz.customdemo.R
import com.zeke.kangaroo.utils.UIUtils

class PathSampleView @JvmOverloads constructor(
    context: Context?, attrs: AttributeSet? = null
) : View(context, attrs) {
    var pathPaint: Paint = Paint()
    var path: Path = Path()
    var round: Float = 25f
    @ColorInt
    var startColor = Color.parseColor("#ffE84749")
    @ColorInt
    var endColor = Color.parseColor("#FF00ff00")
    var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ironman_1)
    var rectArray = arrayOf(
        RectF(dp2px(5f), dp2px(5f), dp2px(55f), dp2px(155f)),
        RectF(dp2px(65f), dp2px(5f), dp2px(115f), dp2px(155f)),
        RectF(dp2px(125f), dp2px(5f), dp2px(175f), dp2px(155f)),
        RectF(dp2px(185f), dp2px(5f), dp2px(235f), dp2px(155f)),
        RectF(dp2px(245f), dp2px(5f), dp2px(295f), dp2px(155f))

//        RectF(dp2px(5f), dp2px(5f), dp2px(55f), dp2px(155f)),
//        RectF(dp2px(5f), dp2px(165f), dp2px(55f), dp2px(306f)),
//        RectF(dp2px(5f), dp2px(316f), dp2px(55f), dp2px(456f))
    )

    private fun dp2px(dpValue: Float): Float {
        return UIUtils.dip2px( dpValue).toFloat()
    }
    init {
        pathPaint.color = startColor
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
        setShader(null)
        rectArray.forEach {
            pathPaint.strokeCap = Paint.Cap.ROUND
            path.addRoundRect(
                it,
                floatArrayOf(
                    round, round, round, round,
                    0f, 0f, 0f, 0f
                ),
                Path.Direction.CCW
            )
            canvas.drawPath(path, pathPaint)
            path.rewind()
        }
    }

    private fun setShader(it: RectF?) {
        pathPaint.shader = BitmapShader(bitmap,
            Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
//        pathPaint.shader = LinearGradient(
//            it.left, it.top, it.right, it.bottom,
//            startColor,
//            endColor,
//            Shader.TileMode.CLAMP
//        )
    }
}