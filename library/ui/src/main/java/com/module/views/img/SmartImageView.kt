package com.module.views.img

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.module.views.R


/**
 * author：ZekeWang
 * date：2021/7/2
 * description：
 * - 支持自定义圆角
 * - 支持圆角模式选择(Clip或者Xfermode)
 */
class SmartImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
        //Image render mode.
        const val DEFAULT_MODE_CLIP = 0
        const val DEFAULT_MODE_XFERMODE = 1 //此模式目前不支持每个圆角自定义
    }

    /**
     * painter of all drawing things
     */
    private var paint = Paint()
    private var modeDST_IN: Xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    private var modeSRC_IN: Xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private var circleBitmap: Bitmap? = null

    private var roundRect: RectF = RectF()
    private var bounds: RectF = RectF()
    private var strokeOffset = 0f
    var clipPath = Path()
//    private var raduis = 0f
    /**
     * Default render mode is use clip.
     */
    private var renderMode: Int = DEFAULT_MODE_CLIP

    //-------- PropParsers --------
    private var borderProparser: ParsedStyle_Border

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SmartImageView)
        renderMode = ta.getInt(R.styleable.SmartImageView_render_mode, 0)
        borderProparser = SmartPropParser(context, ta).getParsedValue(R.styleable.SmartImageView_border)
        ta.recycle()


        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        paint.isDither = true
        paint.isAntiAlias = true
        paint.strokeWidth = borderProparser.size
        strokeOffset = paint.strokeWidth / 2
    }

    private fun createCircleBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        paint.style = Paint.Style.FILL
        if (borderProparser.radiiArrayMode) {
            clipPath.addRoundRect(
                bounds,
                borderProparser.radii,
                Path.Direction.CW
            )
        } else {
            clipPath.addRoundRect(
                bounds,
                borderProparser.xRadius,
                borderProparser.yRadius,
                Path.Direction.CW
            )
        }
        canvas.drawPath(clipPath, paint)
        return bitmap
    }

    override fun onDraw(canvas: Canvas) {
        bounds.set(0f, 0f, width.toFloat(), height.toFloat())
        roundRect.set(
            0f + strokeOffset,
            0f + strokeOffset,
            width.toFloat() - strokeOffset,
            height.toFloat() - strokeOffset
        )
        if (renderMode == DEFAULT_MODE_CLIP) {
            val saveLayer = canvas.saveLayer(bounds, null)
            if(borderProparser.radiiArrayMode){
                clipPath.addRoundRect(
                    bounds,
                    borderProparser.radii,
                    Path.Direction.CW
                )
            }else{
                clipPath.addRoundRect(
                    bounds,
                    borderProparser.xRadius,
                    borderProparser.yRadius,
                    Path.Direction.CW
                )
            }
            canvas.clipPath(clipPath)
            super.onDraw(canvas)
            clipPath.rewind()
            drawBorders(canvas)
            canvas.restoreToCount(saveLayer)

        } else if (renderMode == DEFAULT_MODE_XFERMODE) {
            if (circleBitmap == null) {
                circleBitmap = createCircleBitmap()
            }
            val saveLayer = canvas.saveLayer(bounds, null)
            super.onDraw(canvas)
            paint.xfermode = modeDST_IN
            paint.style = Paint.Style.FILL
            canvas.drawBitmap(circleBitmap!!, 0f, 0f, paint)
            paint.xfermode = modeSRC_IN
            drawBorders(canvas)
            paint.xfermode = null
            canvas.restoreToCount(saveLayer)
        }
    }

    private fun drawBorders(canvas: Canvas) {
        drawRectFBorder(canvas, borderProparser.size.toInt(),
            borderProparser.color, bounds,
            borderProparser.radii)
    }

    private fun drawRectFBorder(canvas: Canvas, borderWidth: Int,
                                borderColor: Int, rectF: RectF,
                                radii: FloatArray) {
        initBorderPaint(borderWidth, borderColor)
        clipPath.addRoundRect(rectF, radii, Path.Direction.CCW)
        canvas.drawPath(clipPath, paint)
        clipPath.reset()
    }

    private fun initBorderPaint(borderWidth: Int, borderColor: Int) {
        clipPath.reset()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth.toFloat() * 2
        paint.color = borderColor
    }


}