package com.zeke.home

import android.content.Context
import com.zeke.kangaroo.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * author：KingZ
 * date：2020/2/16
 * description：带颜色渐变和缩放的指示器标题
 */
class ScaleTransitionPagerTitleView(ctx: Context) : ColorTransitionPagerTitleView(ctx) {
    private var mMinScale = 0.75f

    override fun onEnter(index: Int,
                         totalCount: Int,
                         enterPercent: Float,
                         leftToRight: Boolean) {
        super.onEnter(index, totalCount, enterPercent, leftToRight)    // 实现颜色渐变
        scaleX = mMinScale + (1.0f - mMinScale) * enterPercent
        scaleY = mMinScale + (1.0f - mMinScale) * enterPercent
    }

    override fun onLeave(index: Int,
                         totalCount: Int,
                         leavePercent: Float,
                         leftToRight: Boolean) {
        super.onLeave(index, totalCount, leavePercent, leftToRight)    // 实现颜色渐变
        scaleX = 1.0f + (mMinScale - 1.0f) * leavePercent
        scaleY = 1.0f + (mMinScale - 1.0f) * leavePercent
    }

    fun getMinScale(): Float {
        return mMinScale
    }

    fun setMinScale(minScale: Float) {
        mMinScale = minScale
    }
}