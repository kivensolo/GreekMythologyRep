package com.zeke.ktx.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.StringRes
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.kingz.customdemo.R
import com.zhy.autolayout.utils.AutoUtils

/**
 * description: 加载、重试、错误状态的视图
 */
class LoadStatusView : FrameLayout {

    private var progress: View? = null
    private var error: TextView? = null
    private var errorIcon: Drawable? = null
    private var noDataIcon: Drawable? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        progress = ProgressBar(context)
//        progress.setIndeterminateDrawable()

        // 错误图标初始化
        errorIcon = resources.getDrawable(R.drawable.ic_load_error)
        errorIcon?.setBounds(0, 0,
                errorIcon!!.minimumWidth,
                errorIcon!!.minimumHeight)
        error = TextView(context)
        error!!.setTextAppearance(context, R.style.txt_normal_dark)
        error!!.gravity = Gravity.CENTER
        error!!.compoundDrawablePadding = AutoUtils.getPercentHeightSize(96)
        error!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSize(44).toFloat())
        error!!.setLineSpacing(-1f, 1.5f)
        error!!.setCompoundDrawables(null, errorIcon, null, null)

        noDataIcon = resources.getDrawable(R.drawable.ic_no_data)
        noDataIcon?.setBounds(0, 0,
                noDataIcon!!.minimumWidth,
                noDataIcon!!.minimumHeight)

        val progressParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        progressParams.gravity = Gravity.CENTER
        addView(progress, progressParams)

        val errorParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        errorParams.gravity = Gravity.CENTER
        addView(error, errorParams)
    }

    fun showProgress() {
        this.visibility = View.VISIBLE
        progress!!.visibility = View.VISIBLE
        error!!.visibility = View.GONE
    }

    fun showError(){
        showError(null)
    }

    fun showError(listener: View.OnClickListener?) {
        this.visibility = View.VISIBLE
        val errorString = resources.getString(R.string.networl_error)
        val spannableString = SpannableString(errorString)
        //实例化ForegroundColorSpan对象并设置前景色
        val errorSpan = ForegroundColorSpan(Color.parseColor("#5f5f5f"))
        spannableString.setSpan(errorSpan, 0, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)//设置字体颜色
        //实例化ForegroundColorSpan对象并设置前景色
        val error2Span = ForegroundColorSpan(Color.parseColor("#989898"))
        spannableString.setSpan(error2Span, 8, errorString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)//设置字体颜色
        error!!.text = spannableString
        error!!.setOnClickListener(listener)
        error!!.visibility = View.VISIBLE
        progress!!.visibility = View.GONE
    }


    fun showEmpty() {
        showEmpty(R.string.tips_data_empty,null)
    }

    fun showEmpty(@StringRes string: Int,
                  onClickListener: View.OnClickListener?) {
        this.visibility = View.VISIBLE
        val str = resources.getString(string)
        if (str.contains("\n")) {
            val spannableString = SpannableString(str)
            //实例化ForegroundColorSpan对象并设置前景色
            val errorSpan = ForegroundColorSpan(Color.parseColor("#5f5f5f"))
            spannableString.setSpan(errorSpan, 0,
                    str.indexOf("\n"),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE)//设置字体颜色
            //实例化ForegroundColorSpan对象并设置前景色
            val error2Span = ForegroundColorSpan(Color.parseColor("#989898"))
            spannableString.setSpan(error2Span, str.indexOf("\n"),
                    str.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE)//设置字体颜色
            error!!.text = spannableString
        } else {
            error!!.text = str
        }
        error!!.setOnClickListener(onClickListener)
        error!!.visibility = View.VISIBLE
        progress!!.visibility = View.GONE
    }

    fun dismiss() {
        this.visibility = View.GONE
        progress!!.visibility = View.GONE
        error!!.visibility = View.GONE
    }

}
