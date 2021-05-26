package com.kingz.module.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.module.views.loading.Win10LoaddingView

/**
 * description: 加载、重试、错误状态的视图
 */
class LoadStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0    // 注意这个attr的默认值,有的控件不一定是0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var progress : Win10LoaddingView?= null
//    private var progress: View? = null
    private lateinit var statusText: TextView
    private lateinit var errorIcon: Drawable
    private lateinit var noDataIcon: Drawable

    init {
        initView(context)
    }

    private fun initView(context: Context) {
//      progress = ProgressBar(context)
        progress = Win10LoaddingView(context).apply {
            setStrokeWidth(18)
            setColor(resources.getColor(R.color.skygreen_b))
            setDuration(3000)
            setType(Win10LoaddingView.LoadingType.DOTS)
        }
//        progress.setIndeterminateDrawable()

        // 错误图标初始化
        errorIcon = resources.getDrawable(R.drawable.ic_load_error, null)
        with(errorIcon) {
            setBounds(0, 0,errorIcon.minimumWidth,errorIcon.minimumHeight)
        }
        noDataIcon = resources.getDrawable(R.drawable.ic_no_data, null)
        with(noDataIcon) {
            setBounds(0, 0, noDataIcon.minimumWidth, noDataIcon.minimumHeight)
        }

        statusText = TextView(context)
        with(statusText) {
            setTextAppearance(context, R.style.txt_normal_dark)
            gravity = Gravity.CENTER
            compoundDrawablePadding = 96
            setTextSize(TypedValue.COMPLEX_UNIT_PX, 44f)
            setLineSpacing(-1f, 1.5f)
            setCompoundDrawables(null, errorIcon, null, null)
        }

        val progressParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        }
        addView(progress, progressParams)

        val errorParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT).apply{
            gravity = Gravity.CENTER
        }
        addView(statusText, errorParams)
    }

    fun showProgress() {
        visibility = View.VISIBLE
        progress?.apply {
            visibility = View.VISIBLE
            start()
        }
        statusText.visibility = View.GONE
    }

    fun showError(){
        showError(null)
    }

    fun showError(listener: OnClickListener?) {
        visibility = View.VISIBLE
        val errorString = resources.getString(R.string.network_error)
        val spannableString = SpannableString(errorString).apply {
            val errorSpan = ForegroundColorSpan(resources.getColor(R.color.dialog_text_gray))
            val error2Span = ForegroundColorSpan(resources.getColor(R.color.dialog_text))
            setSpan(errorSpan, 0, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(error2Span, 8, errorString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        statusText.apply {
            text = spannableString
            setOnClickListener(listener)
            visibility = View.VISIBLE
        }
        progress?.apply {
            visibility = View.GONE
            stop()
        }
    }


    fun showEmpty() {
        show(R.string.tips_data_empty,null)
    }

    fun show(@StringRes string: Int,
                  onClickListener: OnClickListener?) {
        this.visibility = View.VISIBLE
        val str = resources.getString(string)
        if (str.contains("\n")) {
            val spannableString = SpannableString(str)
            //实例化ForegroundColorSpan对象并设置前景色
            val errorSpan = ForegroundColorSpan(resources.getColor(R.color.dialog_text))
            spannableString.setSpan(errorSpan, 0,
                    str.indexOf("\n"),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE)//设置字体颜色
            //实例化ForegroundColorSpan对象并设置前景色
            val error2Span = ForegroundColorSpan(Color.parseColor("#989898"))
            spannableString.setSpan(error2Span, str.indexOf("\n"),
                    str.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE)//设置字体颜色
            statusText.text = spannableString
        } else {
            statusText.text = str
        }
        statusText.setOnClickListener(onClickListener)
        statusText.visibility = View.VISIBLE
        progress?.apply {
            stop()
            visibility = View.GONE
        }
    }

    fun dismiss() {
        visibility = View.GONE
        progress?.apply {
            stop()
            visibility = View.GONE
        }
        statusText.visibility = View.GONE
    }

}
