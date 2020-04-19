package com.zeke.ktx.demo.draw.paint

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import com.kingz.customdemo.R
import com.zeke.ktx.demo.BasePracticeView
import com.zeke.ktx.demo.modle.CardItemConst

/**
 * author: King.Z <br>
 * date:  2020/3/15 14:27 <br>
 * description:  <br>
 *     混合着色器所谓混合，就是把两个 Shader 一起使用。
 *     Note: ComposeShader() 在硬件加速下是不支持两个相同类型的 Shader 的
 */
class Paint3ComposeShaderView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    init {
        setLayerType(LAYER_TYPE_HARDWARE, paint)
    }

    // 第一个 Shader：头像的 Bitmap
    var bitmap1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.cat_m)
    private var shader1: BitmapShader = BitmapShader(bitmap1, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)


    private var shader2:LinearGradient = LinearGradient(380f, 45f, 600f, 550f,
            Color.parseColor("#5FCDDA"),Color.parseColor("#FED094"),
            Shader.TileMode.CLAMP)

    // 第二个 Shader：从上到下的线性渐变（由透明到黑色
    // var bitmap2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.arduino)
    // private var shader2: BitmapShader = BitmapShader(bitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

    /**
     * shader1: dst
     * shader2: src
     */
    private var composeShader: ComposeShader = ComposeShader(shader1, shader2, PorterDuff.Mode.SRC_OVER)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.shader = composeShader
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.saveLayer(0f,0f,1000f,600f,paint)
        }
        // 只画出circle范围内的Bitmap图像(bitmap为原尺寸大小)，效果和Canvas.drawBitmap()一样
        canvas.drawCircle(550f, 300f, 250f, paint)
        canvas.restore()
    }

    override fun getViewHeight(): Int {
        return CardItemConst.LARGE_HEIGHT
    }
}