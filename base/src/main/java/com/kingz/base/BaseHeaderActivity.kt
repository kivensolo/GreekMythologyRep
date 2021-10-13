package com.kingz.base

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.zeke.kangaroo.utils.ScreenDisplayUtils
import kotlinx.android.synthetic.main.view_page_header.*


abstract class BaseHeaderActivity : BaseVMActivity() {

//    protected lateinit var headerBinding: ViewPageHeaderBinding

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
//        headerBinding = ViewPageHeaderBinding.inflate(LayoutInflater.from(this))
        toolbar_left?.setOnClickListener {
            finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            StatusBarUtils.setStatusBar(this, resources.getColor(R.color.stats_bar_color, null))
        }
    }

    fun setTitle(title: String?) {
//        headerBinding.tvTitle.text = title ?: ""
        tvTitle?.text = title ?: ""
    }

    fun setRightViewBackgroundAlpha(alpha: Float) {
//        headerBinding.ivRight.alpha = alpha
        ivRight?.alpha = alpha
    }

     fun setRightViewVisibility(visibility: Int) {
//        headerBinding.ivRight.visibility = visibility
        ivRight?.visibility = visibility
    }

    fun setRightText(text: String) {
        ivRight?.text = text
    }

    fun setRightClickListener(listener: View.OnClickListener) {
        ivRight?.setOnClickListener(listener)
    }

    fun setRightDrawableVisibility(visibility: Int) {
        ivRight?.setCompoundDrawablesWithIntrinsicBounds(
            0, 0, if (visibility == View.VISIBLE) R.drawable.ic_more_vert else 0, 0
        )
    }

    fun setRightDrawableRes(@DrawableRes icon: Int) {
        ivRight?.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0)
    }

    fun setHeaderBackgroundColor(@ColorInt color: Int) {
        toolbar?.setBackgroundColor(color)
    }

    fun setHeaderMarginTopInImmersion(
        rootLayout: ConstraintLayout,
        marginTop: Int = ScreenDisplayUtils.getStatusHeight(this)){
        ConstraintSet().run {
            clone(rootLayout)
            connect(
                R.id.toolbar, ConstraintSet.TOP,
                ConstraintSet.PARENT_ID, ConstraintSet.TOP,
                marginTop
            )
            applyTo(rootLayout)
        }
    }
}