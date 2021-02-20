package com.zeke.home.wanandroid

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * author: King.Z <br>
 * date:  2021/2/20 21:42 <br>
 * description:  <br>
 */
class ScrollAwareFABBehavior: FloatingActionButton.Behavior(){
        override fun onStartNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: FloatingActionButton,
            directTargetChild: View,
            target: View,
            axes: Int,
            type: Int
        ): Boolean {
            return axes == ViewCompat.SCROLL_AXIS_VERTICAL
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
            super.onNestedScroll(coordinatorLayout, child,target,
                dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed, type, consumed)
            if (dyConsumed >= 0 && child.visibility == View.VISIBLE) {
                child.hide()
            } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
                child.show()
            }
        }

    }