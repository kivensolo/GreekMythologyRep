package com.kingz.module.common.widgets

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

class BottomNavigationBehavior(
    context: Context?,
    attrs: AttributeSet?
) : CoordinatorLayout.Behavior<View>(context, attrs) {
    private var outAnimator: ObjectAnimator? = null
    private var inAnimator // 属性动画，用于垂直方向平移
            : ObjectAnimator? = null

    /**
     * 垂直滑动
     * 这个方法主要用于监听协调布局的子view的滚动事件，当此方法返回true，表示要消耗此动作，
     * 继而执行下面的onNestedPreScroll方法，我们在代码中返回的是，滚动轴是不是竖直滚动轴。如果是的话，就返回true
     */
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View, directTargetChild: View,
        target: View, axes: Int, type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    /**
     * 当用户上滑的时候，隐藏底部菜单栏，这里使用了动画退出，使用了ObjectAnimator.ofFloat方法，第一个是view对象，
     * 指的就是bottom，第二个是Y轴的变化，第三个是Y轴变化的多少，接下来设置动画秒数。
     */
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout, child: View,
        target: View, dx: Int, dy: Int, consumed: IntArray,
        type: Int
    ) {
        if (Math.abs(dy) < 10) {
            return
        }
        if (dy > 0) { // 上滑隐藏
            if (outAnimator == null) {
                outAnimator = ObjectAnimator.ofFloat(
                    child,
                    "translationY",
                    0f,
                    child.height.toFloat()
                )
                outAnimator?.duration = 200
            }
            if (!outAnimator!!.isRunning && child.translationY <= 0) {
                outAnimator!!.start()
            }
        } else if (dy < 0) { // 下滑显示
            if (inAnimator == null) {
                inAnimator = ObjectAnimator.ofFloat(
                    child,
                    "translationY",
                    child.height.toFloat(),
                    0f
                )
                inAnimator?.duration = 200
            }
            if (!inAnimator!!.isRunning && child.translationY >= child.height) {
                inAnimator!!.start()
            }
        }
    }
}