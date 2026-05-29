package com.zeke.demo.draw.flash

import android.graphics.Color
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.module.UIUtil
import com.module.drawable.MarqueeBorderDrawable

data class FlashAttrsBean(
    val mode:String = "",
    //日落暖光配色：橙红 → 番茄红 → 金色 → 橙红 → 亮番茄红，从暗到亮的暖色渐变，在深色背景上会很好看
    val colors:IntArray = intArrayOf(
        Color.parseColor("#0DFF4500"),
        Color.parseColor("#33FF6347"),
        Color.parseColor("#66FFD700"),
        Color.parseColor("#CCFF4500"),
        Color.parseColor("#FFFF6347"),
    ),
    val angle:Int = 0,
    val duration:Int = 4000,
    val interval:Int = 500,
    val repeatCount:Int = -1,
    val gradientRatio:Float = 0.6f,
    val borderWidth:Int = 5,
    val autoRun:Boolean = true
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FlashAttrsBean

        if (mode != other.mode) return false
        if (!colors.contentEquals(other.colors)) return false
        if (angle != other.angle) return false
        if (duration != other.duration) return false
        if (interval != other.interval) return false
        if (repeatCount != other.repeatCount) return false
        if (gradientRatio != other.gradientRatio) return false
        if (borderWidth != other.borderWidth) return false
        if (autoRun != other.autoRun) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mode.hashCode()
        result = 31 * result + colors.contentHashCode()
        result = 31 * result + angle
        result = 31 * result + duration
        result = 31 * result + interval
        result = 31 * result + repeatCount
        result = 31 * result + gradientRatio.hashCode()
        result = 31 * result + borderWidth
        result = 31 * result + autoRun.hashCode()
        return result
    }
}

class FlashDemoRecyclerAdapter(layoutResId: Int, data: MutableList<FlashAttrsBean>?) :
    BaseQuickAdapter<FlashAttrsBean, FlashDemoRecyclerAdapter.FLashViewHolder>(layoutResId, data) {

    override fun convert(holder: FLashViewHolder, item: FlashAttrsBean) {

        val flashEnhanceView = holder.itemView as FlashEnhanceView
        flashEnhanceView.let {
            it.mMode = item.mode
            it.mFlashColors = item.colors
            it.mFlashBeginAngle = item.angle
            it.mAnimationDuration = item.duration
            it.mAnimationInterval = item.interval
            it.mRepeatCount = item.repeatCount
            it.mGradientRatio = item.gradientRatio
            it.mBorderFlashWidth =  UIUtil.dip2px(it.context, item.borderWidth.toFloat())
            it.mFlashStarted = item.autoRun
        }

        val itemPosition = getItemPosition(item)
        if(itemPosition == (data.size -1)){
            //最后一个，是用来测 MarqueeBorderDrawable 与FlashEnhanceView效果差别的
            flashEnhanceView.background = MarqueeBorderDrawable(autoRun = true)
        }
    }


    inner class FLashViewHolder(view: View) : BaseViewHolder(view) {
    }
}