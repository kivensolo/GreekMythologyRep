package com.kingz.module.common.widgets

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * author：ZekeWang
 * date：2021/12/6
 * description：
 * 使用内部拦截法。
 * 解决ViewPager嵌套ViewPager时，子ViewPager无法滑动的问题。
 */
class ChildViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewPager(context, attrs) {
    /** 触摸时当前的点  */
    var curP: PointF = PointF()
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val curPosition: Int
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                curP.x = ev.x
                //告知父类先不进行拦截
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                curPosition = currentItem
                val count = adapter?.count ?: 0
                var disallowParent = true
                if (curPosition == 0 && ev.x > curP.x) {
                    disallowParent = false //第一页&&往左翻页
                } else if(curPosition == (count - 1) && ev.x < curP.x) {
                    disallowParent = false //最后一页&&往右翻页
                }
                parent.requestDisallowInterceptTouchEvent(disallowParent)
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}