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
 *     ColorFilter：为绘制设置颜色过滤。颜色过滤的意思，
 *     就是为绘制的内容设置一个统一的过滤策略，
 *     然后 Canvas.drawXXX() 方法会对每个像素都进行过滤后再绘制出来。
 *
 *     ColorFilter 并不直接使用，而是使用它的子类
 *     - LightingColorFilter(int mul, int add)
 *          用来模拟简单的光照效果的,mul 和 add 都是和颜色值格式相同的 int 值，<br>
 *          其中 mul 用来和目标像素相乘，add 用来和目标像素相加
 *          一个「保持原样」的「基本 LightingColorFilter 」，mul 为 0xffffff，add 为 0x000000（也就是0）
 *          计算过程:
 *          R' = R * 0xff / 0xff + 0x0 = R // R' = R
 *          G' = G * 0xff / 0xff + 0x0 = G // G' = G
 *          B' = B * 0xff / 0xff + 0x0 = B // B' = B
 *
 *     - PorterDuffColorFilter
 *     - ColorMatrixColorFilter
 *       见奇异博士那个demo
 *
 *       //FIXME 此View引起卡顿
 */
class Paint4ColorFilterView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {
    var bop:BitmapFactory.Options = BitmapFactory.Options()
    var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.cat_m)
    var rectF: RectF = RectF(0f, 0f, 1080f, 600f)

    init {
        bop.inJustDecodeBounds = true
    }

    // 蓝色部分被移除 蓝色部分是0   同时让红色(即猫女的嘴唇)增强
    var lightingColorFilter: ColorFilter = LightingColorFilter(0xffff00, 0x000000)

    // private var shader: BitmapShader = BitmapShader(bitmap, // 用来做模板的 Bitmap 对象
    //         Shader.TileMode.CLAMP, // 横向的 TileMode
    //         Shader.TileMode.CLAMP) // 纵向的 TileMode
    override fun onDraw(canvas: Canvas) {
        //        练习内容：使用 BitmapShader 方法画自定义效果的Bitmap
        super.onDraw(canvas)

        paint.colorFilter
        paint.colorFilter = lightingColorFilter
        // 只画出circle范围内的Bitmap图像(bitmap为原尺寸大小)，效果和Canvas.drawBitmap()一样
        canvas.drawBitmap(bitmap, null, rectF, paint)

        paint.color = Color.WHITE
        // paint.style = Typeface.BOLD
        canvas.drawText("LightingColorFilter", 20f, 50f, paint)
    }

    override fun getViewHeight(): Int {
        return CardItemConst.LARGE_HEIGHT
    }
}