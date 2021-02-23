package com.kingz.module.common.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.abs

/**
 * author: King.Z <br>
 * date:  2021/2/20 21:42 <br>
 * description: constructor必须存在，否则无法被实例化填充 <br>
 *   实现列表下滑时,显示Toolbar和FloatingActionButton
 *   上滑列表时：隐藏Toolbar和FloatingActionButton
 */
class FloatingActionButtonBehavior(
    context: Context?,
    attrs: AttributeSet?
) : FloatingActionButton.Behavior(context, attrs)  {
    private var isVisible = false

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton, directTargetChild: View,
        target: View, axes: Int, type: Int
    ): Boolean {
        //被观察者（RecyclerView）发生滑动的开始的时候回调的
        //nestedScrollAxes:滑动关联轴，现在只关心垂直的滑动。
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(
                    coordinatorLayout, child, directTargetChild
                    , target, axes, type
                )
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(coordinatorLayout, child, target,
            dxConsumed, dyConsumed, dxUnconsumed,dyUnconsumed, type, consumed)
        if (abs(dyConsumed) < 40) {
            return
        }

        if (dyConsumed > 0 && isVisible) { //列表上滑
            onHide(child)
        } else if (dyConsumed < 0) {    //列表下滑
            onShow(child)
        }
    }

    private fun onHide(view: View) {
        isVisible = false
        view.visibility = View.INVISIBLE
    }

    private fun onShow(view: View) {
        isVisible = true
//        ViewCompat.animate(view).translationY(0f).interpolator = DecelerateInterpolator(3F)
        view.visibility = View.VISIBLE
    }

}