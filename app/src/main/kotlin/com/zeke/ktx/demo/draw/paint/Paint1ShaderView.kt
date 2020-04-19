package com.zeke.ktx.demo.draw.paint

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.zeke.ktx.demo.BasePracticeView
import com.zeke.ktx.demo.modle.CardItemConst

/**
 * author: King.Z <br>
 * date:  2020/3/15 14:27 <br>
 * description:  <br>
 *     展示着色器的效果
 *  LinearGradient 线性渐变
 *  RadialGradient 辐射渐变
 *  SweepGradient 扫描渐变
 *
 *  Shader.TileMode:
 *   - CLAMP 画布大于画的图形时，图形只显示一个，其他的由图形边缘颜色填充
 *   - MIRROR 纵横像镜像
 *   - REPEAT 纵横重复
 */
class Paint1ShaderView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    val radius: Float = 200f

    val content:String
        get() = "Paint.setColor/ARGB()和Shader 都可设置颜色"

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 注意此处的y坐标值，是base_line的值  不是左上角
        canvas.drawText(content, 10f, 50f, paint)

        paint.shader = shaderCLAMP
        canvas.drawCircle(200f, 350f, radius, paint)

        paint.shader = shaderMIRROR
        canvas.drawCircle(780f, 350f, radius, paint)
    }

    override fun getViewHeight(): Int {
        return CardItemConst.LARGE_HEIGHT
    }
}