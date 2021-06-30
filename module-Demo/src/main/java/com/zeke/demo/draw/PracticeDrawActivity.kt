package com.zeke.demo.draw

import android.os.Bundle
import android.view.Menu
import com.zeke.demo.R
import com.zeke.demo.base.AbsDemoActivity
import com.zeke.demo.draw.base_api.*
import com.zeke.demo.draw.canvas.CanvasDemoViewGroup
import com.zeke.demo.draw.paint.*
import com.zeke.demo.draw.path.ShadowLineChartView
import com.zeke.demo.draw.text.*
import com.zeke.demo.model.CardItemModel
import com.zeke.demo.model.DemoContentModel
import java.util.*

/**
 * 关于绘制练习的页面
 */
class PracticeDrawActivity : AbsDemoActivity() {

    override fun inflatePageData() {
        // 1-1 基础api展示数据
        val cardData1: MutableList<CardItemModel> = ArrayList()
        cardData1.apply {
            add(CardItemModel("canvas.drawColor(Color.YELLOW)", Practice1ColorView(this@PracticeDrawActivity)))
            add(CardItemModel("drawCircle", Practice2CircleView(this@PracticeDrawActivity)))
            add(CardItemModel("drawRect", Practice3RectView(this@PracticeDrawActivity)))
            add(CardItemModel("drawPoint: ROUND、BUTT、SQUARE", Practice4PointView(this@PracticeDrawActivity)))
            add(CardItemModel("drawOval", Practice5OvalView(this@PracticeDrawActivity)))
            add(CardItemModel("drawLine", Practice6LineView(this@PracticeDrawActivity)))
            add(CardItemModel("drawArc", Practice7ArcView(this@PracticeDrawActivity)))
            add(CardItemModel("drawPath", Practice8PathView(this@PracticeDrawActivity)))
        }

        val cardData2: MutableList<CardItemModel> = ArrayList()
        cardData2.apply {
            add(CardItemModel("线性着色器展示", Paint1ShaderView(this@PracticeDrawActivity)))
            add(CardItemModel("BitmapShader", Paint2BitmapShaderView(this@PracticeDrawActivity)))
            add(CardItemModel("ComposeShader(未生效)", Paint3ComposeShaderView(this@PracticeDrawActivity)))
            add(CardItemModel("ColorFilter", Paint4ColorFilterView(this@PracticeDrawActivity)))
            add(CardItemModel("Xfermode(离屏缓冲 未生效)", Paint5XfermodeView(this@PracticeDrawActivity)))
        }

        val cardData3: MutableList<CardItemModel> = ArrayList()
        cardData3.apply {
            add(CardItemModel("简单效果使用", Paint6NormalEffectView(this@PracticeDrawActivity)))
            add(CardItemModel("PathEffect", Paint7PathEffectView(this@PracticeDrawActivity)))
            add(CardItemModel("PathEffectV2", ShadowLineChartView(this@PracticeDrawActivity)))
            add(CardItemModel("ShadowLayer(在下面附加效果)", Paint8ShadowLayerView(this@PracticeDrawActivity)))
            add(CardItemModel("MaskFilter(在上面附加效果)", Paint9MaskFilterView(this@PracticeDrawActivity)))
            add(CardItemModel("getPath", Paint10GetPathView(this@PracticeDrawActivity)))
        }

        val cardData4: MutableList<CardItemModel> = ArrayList()
        cardData4.apply {
            add(CardItemModel("文字效果绘制API", Paint11TextEffecsView(this@PracticeDrawActivity)))
            add(CardItemModel("文字尺寸绘制API", Paint12TextDimensionView(this@PracticeDrawActivity)))
            add(CardItemModel("FontMetric", Paint13FontMetricView(this@PracticeDrawActivity)))
            add(CardItemModel("drawTextOnPath()", Paint14TextOnPathView(this@PracticeDrawActivity)))
            add(CardItemModel("文字换行绘制 StaticLayout", Paint15TextStaticLayoutView(this@PracticeDrawActivity)))
        }

        val cardData5: MutableList<CardItemModel> = ArrayList()
        cardData5.add(CardItemModel("Canvas练习", CanvasDemoViewGroup(this@PracticeDrawActivity)))

        // 初始化Page数据
        with(pageModels) {
            add(DemoContentModel(getString(R.string.draw_1_1), cardData1))
            add(DemoContentModel(getString(R.string.draw_paint_color), cardData2))
            add(DemoContentModel(getString(R.string.draw_paint_effect), cardData3))
            add(DemoContentModel(getString(R.string.draw_text), cardData4))
            add(DemoContentModel("1.4 画布练习", cardData5))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
}
