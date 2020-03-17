package com.zeke.ktx.demo.draw.paint

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import com.kingz.customdemo.R
import com.zeke.kangaroo.utils.BitMapUtils
import com.zeke.ktx.demo.draw.BasePracticeView


/**
 * author: King.Z <br>
 * date:  2020/3/15 14:27 <br>
 * description:  <br>
 *     MaskFilter是设置在绘制层上方的附加效果。
 *     setColorFilter(filter)是对每个像素的颜色进行过滤;
 *     setMaskFilter(filter) 则是基于整个画面来进行过滤。
 *
 *  MaskFilter 有两种：
 *      BlurMaskFilter 和 EmbossMaskFilter。
 *
 *      EmbossMaskFilter(float[] direction, float ambient, float specular, float blurRadius) 的参数里，
 *      direction 是一个 3 个元素的数组，指定了光源的方向；
 *      ambient 是环境光的强度，数值范围是 0 到 1；
 *      specular 是炫光的系数；
 *      blurRadius 是应用光线的范围。
 */

class Paint9MaskFilterView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    private var paintBlur = Paint(paint)
    private var paintCopy = Paint(paint)
    var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.zy_smile)

    private var blurMaskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.NORMAL)

    var embossMaskFilter = EmbossMaskFilter(floatArrayOf(0f, 1f, 1f),
            0.2f, 8f, 10f)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            paint.color = resources.getColor(R.color.dodgerblue,null)
        }
        canvas.drawText("BlurMaskFilter:", 10f, 50f, paint)
        canvas.drawText("EmbossMaskFilter(浮雕效果):", 400f, 50f, paint)

        val zoomImg = BitMapUtils.setZoomImg(bitmap, 450, 290)
        paintBlur.maskFilter = blurMaskFilter
        canvas.drawBitmap(zoomImg, 20f, 70f, paintBlur)

        paintCopy.maskFilter = embossMaskFilter
        canvas.drawBitmap(zoomImg, 530f, 70f, paintCopy)
    }

    override fun getViewHeight(): Int {
        return 400
    }
}