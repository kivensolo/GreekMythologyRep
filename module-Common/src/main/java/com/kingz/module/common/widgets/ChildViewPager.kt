package com.kingz.module.common.widgets

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

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
    private var touchDownPoint: PointF = PointF()
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val curPosition: Int
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownPoint.x = ev.x
                touchDownPoint.y = ev.y
                //告知父容器先不进行拦截
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                curPosition = currentItem
                val count = adapter?.count ?: 0
                var disallowParent = true
                val dx = ev.x - touchDownPoint.x
                val dy = ev.y - touchDownPoint.y
                var scrollFromLTR = false
                if(abs(dx) > abs(dy)){ //isHorizontal
                    scrollFromLTR = ev.x > touchDownPoint.x
                }
                if (curPosition == 0 && scrollFromLTR) {
                    disallowParent = false //第一页&&往左翻页
                } else if(curPosition == (count - 1) && !scrollFromLTR) {
                    disallowParent = false //最后一页&&往右翻页
                }
                parent.requestDisallowInterceptTouchEvent(disallowParent)
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}