package com.zeke.ktx.activity.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.kingz.customdemo.R
import com.zeke.kangaroo.utils.ZLog


class GlideCircleView : BaseDemoView {
    constructor(context: Context) : super(context){
        postInvalidate()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val circleCrop = getRequestOption().circleCrop()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.YELLOW)


        val simpleTarget: SimpleTarget<Bitmap?> = object : SimpleTarget<Bitmap?>() {

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                ZLog.d("onResourceReady")
            }
        }

        Glide.with(this)
                .asBitmap()
                .load(R.drawable.marvel_qiyi)
                .apply(circleCrop)
                .into(simpleTarget)

    }



}