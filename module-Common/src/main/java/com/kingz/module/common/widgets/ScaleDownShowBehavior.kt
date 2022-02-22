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
 * description:
 *  FAB 行为控制器
 *  Behavior是CoordinatorLayout里面的一个内部类，
 *  通过它可以与 CoordinatorLayout的一个或者多个子View进行交互，
 *  包括 drag，swipes, flings等手势动作。
 *
 * 【注意】带两个参数的constructor必须存在，否则无法被反射的形式实例化 <br>
 *   实现列表下滑时,显示FloatingActionButton
 */
class FloatingActionButtonBehavior(
    context: Context?,
    attrs: AttributeSet?
) : FloatingActionButton.Behavior(context, attrs)  {
    private var isVisible = false

    //-------------- 方案1： onStartNestedScroll + onNestedScroll
    /**
     * 当CoordinatorLayout 的直接或者非直接子View开始准备嵌套滑动的时候会调用
     */
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton, directTargetChild: View,
        target: View, axes: Int, type: Int
    ): Boolean {
        //被观察者（RecyclerView）发生滑动的开始的时候回调的
        //nestedScrollAxes:滑动关联轴，现在只关心垂直的滑动。
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
    }

    /**
     * 当嵌套滑动的 时候，target尝试滑动或者正在滑动的 时候会调用
     */
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


    //-------------- 方案2： layoutDependsOn + onDependentViewChanged
    /**
     * 确定child View 是否有一个特定的兄弟View作为布局的依赖（即dependency）
     */
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        return super.layoutDependsOn(parent, child, dependency)
//        return dependency instanceof AppBarLayout;
    }

    /**
     * 当child View 的 dependent view 发生变化的时候，这个方法会调用
     * @param child  需要进行操作改变的view(即绑定此Behavior的View)
     * @param dependency  child的附属 ，即child可随着 dependency 的改变而改变
     */
    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        return super.onDependentViewChanged(parent, child, dependency)
        //示例：根据dependency top值的变化改变 child 的 translationY
//        float translationY = Math.abs(dependency.getTop());
//        child.setTranslationY(translationY);
//        return true;
    }


}