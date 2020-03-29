package com.zeke.ktx.demo.draw.text

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.kingz.customdemo.R
import com.zeke.kangaroo.utils.UIUtils
import com.zeke.kangaroo.utils.ZLog
import com.zeke.ktx.demo.draw.BasePracticeView
import com.zeke.ktx.demo.modle.CardItemConst

/**
 * author: King.Z <br>
 * date:  2020/3/29 14:27 <br>
 * description: FontMetric 测量文字尺寸类 <br>
 * 由于文字的绘制和图形或 Bitmap 的绘制比起来，尺寸的计算复杂得多，所以它有一整套的方法来计算文字尺寸。
 *     绝大多数的字符，它们的宽度都是要略微大于实际显示的宽度的.
 *     字符的左右两边会留出一部分空隙，用于文字之间的间隔，以及文字和边框的间隔.
 * baseline----基准点(让所有文字互相对齐)
 * Ascent -----baseline之上至字符最高处的距离
 * Descent -----baseline之下至字符最低处的距离
 * Leading -----上一行字符的descent到下一行的ascent之间的距离
 * Top ------最高字符到baseline的值
 * Bottom-----最低字符到baseline的值
 *
 * ascent + descent 可以看成文字的height
 * 获取height ： mPaint.ascent() + mPaint.descent()
 * 获取width ： mPaint.measureText(text)
 */
@Suppress("DEPRECATION")
class Paint13FontMetricView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BasePracticeView(context, attrs, defStyleAttr) {
    private var mDemoPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mDescPaint: Paint? = null
    private var fontMetrics: Paint.FontMetrics? = null
    private val bounds = Rect()
    private val text = "あリが中文Englishشكر"
    private var baselineY = 0f
    private val textDrawY = 100f
    var baseX = 0
    private val sampleTextSize = UIUtils.dip2px(context,32f).toFloat()
    private val descTextSize = UIUtils.dip2px(context,15f).toFloat()


    init{
        isFocusable = true
        initPaint()
    }

    private fun initPaint() {
        mDemoPaint.strokeWidth = 2f
        mDemoPaint.textSize = sampleTextSize
        mDemoPaint.isAntiAlias = true
        mDemoPaint.color = resources.getColor(R.color.black)
        mDemoPaint.typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
        mDescPaint = Paint(mDemoPaint)
        mDescPaint?.textSize = descTextSize
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        fontMetrics = mDemoPaint.fontMetrics
        baselineY = textDrawY - (fontMetrics!!.ascent + fontMetrics!!.descent) / 2
        ZLog.d("Paint13FontMetricView", "baselineY = $baselineY")

        drawLines(canvas, baselineY)
        mDemoPaint.color = Color.BLACK
        if (mDemoPaint.measureText(text) >= width) {
            canvas.drawText(text, 0f, baselineY, mDemoPaint)
        } else {
            canvas.drawText(text, width / 2 - mDemoPaint.measureText(text) / 2, baselineY,
                    this.mDemoPaint)
        }
        ZLog.d("Paint13FontMetricView", "drawText textsize = ${this.mDemoPaint.textSize}")

        drawTextPosDot(canvas, baselineY)
        drawCharDots(canvas)
        drawDescText(canvas)
    }

    /**
     * 绘制字符的点
     */
    private fun drawCharDots(canvas: Canvas) {
        val widths = FloatArray(text.length)
        val count = mDemoPaint.getTextWidths(text, 0, text.length, widths)
        //return the number of unichars / 每个字符的宽度放入widths中
        val pts = FloatArray(2 + count * 2)
        var x = width / 2 - mDemoPaint.measureText(text) / 2
        val y = baselineY
        pts[0] = x //基准点X坐标值
        pts[1] = y //基准点Y坐标值
        for (i in 0 until count) {
            x += widths[i]
            pts[2 + i * 2] = x
            pts[2 + i * 2 + 1] = y
        }
        mDemoPaint.color = resources.getColor(R.color.aqua)
        mDemoPaint.strokeWidth = 12f
        //(count + 1) << 1
        canvas.drawPoints(pts, 0, count + 1 shl 1, this.mDemoPaint)
    }

    private fun drawLines(canvas: Canvas, baselineY: Float) {
        mDemoPaint.strokeWidth = 2f
        drawLine(canvas, baseX, baselineY, resources.getColor(R.color.blue), mDemoPaint)
        drawLine(canvas, baseX, baselineY + fontMetrics!!.top, resources.getColor(R.color.black), mDemoPaint)
        drawLine(canvas, baseX, baselineY + fontMetrics!!.ascent, resources.getColor(R.color.skygreen), mDemoPaint)
        drawLine(canvas, baseX, baselineY + fontMetrics!!.descent, resources.getColor(R.color.green), mDemoPaint)
        drawLine(canvas, baseX, baselineY + fontMetrics!!.bottom, resources.getColor(R.color.gold), mDemoPaint)
        drawLine(canvas, baseX, textDrawY, resources.getColor(R.color.red), mDemoPaint)
    }

    private fun drawDescText(canvas: Canvas) {
        canvas.translate(0f, UIUtils.dip2px(context,80f).toFloat())
        drawInfoText(canvas, resources.getColor(R.color.black), "top|Max-ascent: ${fontMetrics!!.top}", mDescPaint!!)
        drawInfoText(canvas, resources.getColor(R.color.skygreen), "ascent: ${fontMetrics!!.ascent}", mDescPaint!!)
        drawInfoText(canvas, resources.getColor(R.color.blue), "baseline: 0", mDescPaint!!)
        drawInfoText(canvas, resources.getColor(R.color.red), "middle: ${(fontMetrics!!.ascent + fontMetrics!!.descent) / 2}",  mDescPaint!!)
        drawInfoText(canvas, resources.getColor(R.color.green), "descent: ${fontMetrics!!.descent}", mDescPaint!!)
        drawInfoText(canvas, resources.getColor(R.color.gold), "bottom|Max-Descent: ${fontMetrics!!.bottom}", mDescPaint!!)
    }

    /**
     * 绘制文本正确和错误的BaseLine的Y坐标
     */
    private fun drawTextPosDot(canvas: Canvas, baselineY: Float) {
        //Error text coordinate point
        mDemoPaint.color = resources.getColor(R.color.red)
        canvas.drawCircle(7f, baselineY + fontMetrics!!.ascent, 7f, this.mDemoPaint)
        //Correct text coordinate point
        mDemoPaint.color = resources.getColor(R.color.lime)
        canvas.drawCircle(7f, baselineY, 7f, this.mDemoPaint)
    }

    private fun drawInfoText(canvas: Canvas, color: Int, text: String,
                             mPaint: Paint) {
        canvas.translate(0f, 40f)
        mPaint.color = color
        canvas.drawText(text, 20f, 0f, mPaint)
    }

    private fun drawLine(canvas: Canvas, startX: Int, startY: Float, color: Int,
                         mPaint: Paint) {
        mPaint.color = color
        canvas.drawLine(startX.toFloat(), startY, width.toFloat(), startY, mPaint)
    }

    private fun drawBounds(canvas: Canvas) {
        mDemoPaint.getTextBounds(text, 0, text.length, bounds)
        mDemoPaint.color = -0x770078
        canvas.drawRect(bounds, this.mDemoPaint)
    }

    override fun getViewHeight(): Int {
        return CardItemConst.LARGE_HEIGHT
    }
}
