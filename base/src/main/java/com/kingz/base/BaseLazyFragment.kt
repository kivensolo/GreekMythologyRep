package com.kingz.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.zeke.kangaroo.zlog.ZLog
import com.zeke.reactivehttp.viewmodel.IUIActionEventObserver

/**
 * 实现Fragment懒加载控制的Base类,支持复杂的 Fragment 嵌套组合。
 * 比如 Fragment+Fragment、Fragment+ViewPager、ViewPager+ViewPager….等等。
 *
 * Fragment 完整生命周期：
 * onAttach -> onCreate ->
 * onCreatedView -> onActivityCreated -> onStart -> onResume
 * -> onPause -> onStop -> onDestroyView -> onDestroy -> onDetach
 * */
abstract class BaseLazyFragment : Fragment(),IUIActionEventObserver {

    /**
     * 当使用ViewPager+Fragment形式时，setUserVisibleHint会优先Fragment生命周期函数调用，
     * 所以这个时候就,会导致在setUserVisibleHint方法执行时就执行了懒加载，
     * 而不是在onResume方法实际调用的时候执行懒加载。所以需要这个变量进行控制
     */
    private var isActive = false

    /**
     * 是否对用户可见，即是否调用了setUserVisibleHint(boolean)方法
     */
    private var isVisibleToUser = false

    /**
     * 是否调用了setUserVisibleHint方法。
     * 处理show+add+hide模式下，默认可见Fragment不调用
     * onHiddenChanged方法，进而不执行懒加载方法的问题。
     */
    private var isCallUserVisibleHint = false

    /**
     * 是否已执行懒加载
     */
    private var isLoaded = false

    /**
     * 进行页面懒加载
     */
    abstract fun lazyInit()

    override fun onPause() {
        super.onPause()
        isActive = false
    }

    override fun onResume() {
        super.onResume()
        isActive = true
        if(!isCallUserVisibleHint) isVisibleToUser = !isHidden
        tryLazyInit()
    }

    /**
     * androidX版本推荐使用 FragmentTransaction#setMaxLifecycle(Fragment, Lifecycle.State)代替
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        isCallUserVisibleHint = true
        tryLazyInit()
    }

    /**
     * 当 Fragment隐藏的状态发生改变时，该函数将会被调用.
     * 隐藏：hidden为true
     * 显示：hidden为false
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isVisibleToUser = !hidden
        tryLazyInit()
    }

    /**
     * 尝试进行懒加载初始化
     */
    private fun tryLazyInit(){
//        if(!isLoaded && isVisibleToUser && isParentVisible() && isActive){
        if(!isLoaded && isVisibleToUser && isActive){
            ZLog.d("lazyInit:!!!")
            lazyInit()
            isLoaded = true

            //通知子Fragment进行懒加载
            dispatchParentVisibleState()
        }
    }

    /**
     * ViewPager场景下，判断父fragment是否可见
     */
    private fun isParentVisible(): Boolean {
        val fragment = parentFragment
        return fragment == null || (fragment is BaseLazyFragment && fragment.isVisibleToUser)
    }

    /**
     * ViewPager嵌套场景下，当前fragment可见时，
     * 如果其子fragment也可见，则让子fragment请求数据
     */
    private fun dispatchParentVisibleState() {
        val fragmentManager: FragmentManager = childFragmentManager
        val fragments: List<Fragment> = fragmentManager.fragments
        if (fragments.isEmpty()) {
            return
        }
        for (child in fragments) {
            if (child is BaseLazyFragment && child.isVisibleToUser) {
                child.tryLazyInit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
        isActive = false
        isVisibleToUser = false
        isCallUserVisibleHint = false
    }
}