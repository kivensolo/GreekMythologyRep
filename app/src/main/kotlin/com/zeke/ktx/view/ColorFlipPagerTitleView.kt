package com.zeke.ktx.view

import android.content.Context
import com.zeke.kangaroo.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

/**
 * author：KingZ
 * date：2020/2/16
 * description：颜色翻转的PagerTitleView
 */
class ColorFlipPagerTitleView(ctx:Context) : SimplePagerTitleView(ctx) {
    private var mChangePercent = 0.5f
    override fun onLeave(index: Int, totalCount: Int,
                         leavePercent: Float, leftToRight: Boolean) {
        if (leavePercent >= mChangePercent) {
            setTextColor(mNormalColor)
        } else {
            setTextColor(mSelectedColor)
        }
    }

    override fun onEnter(index: Int, totalCount: Int,
                         enterPercent: Float, leftToRight: Boolean) {
        if (enterPercent >= mChangePercent) {
            setTextColor(mSelectedColor)
        } else {
            setTextColor(mNormalColor)
        }
    }

    override fun onSelected(index: Int, totalCount: Int) {}

    override fun onDeselected(index: Int, totalCount: Int) {}

    fun getChangePercent(): Float {
        return mChangePercent
    }

    fun setChangePercent(changePercent: Float) {
        mChangePercent = changePercent
    }
}