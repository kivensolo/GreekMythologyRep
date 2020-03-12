package com.zeke.ktx.activity.glide

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions

open class BaseDemoView : View {
    companion object{
        // var IMG_WIDTH:Int
        // var IMG_HIGHT:Int
        const val IMG_WIDTH = 400
        const val IMG_HIGHT = 300
    }
    constructor(context: Context) : super(context){
        // IMG_WIDTH = UIUtils.dip2px(context,400f)
        // IMG_HIGHT = UIUtils.dip2px(context,300f)
        layoutParams  = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    open fun getRequestOption(): RequestOptions{
        return RequestOptions().override(IMG_WIDTH, IMG_HIGHT)
    }
}