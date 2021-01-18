package com.zeke.home.fragments

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.kingz.module.common.IView
import com.kingz.module.common.base.BaseFragment
import com.kingz.module.common.base.IPresenter
import com.kingz.module.home.R
import java.util.*

/**
 * time: 2020-2-8 11:38
 * description：首页的几个Fragment的父类
 * 使用 TabLayout + viewPager + fragment 实现切页展示
 */
open class HomeBaseFragment<T : IPresenter> : BaseFragment(), IView {
    val TAG:String = HomeBaseFragment::class.java.simpleName
    lateinit var mPresenter: T
    protected var tableLayout: TabLayout? = null
    protected var coverView: View? = null
    protected var viewPager: ViewPager? = null
    protected var currentFragment: Fragment? = null
    protected var viewPagerAdapter: ViewPagerAdapter? = null

    // 每页内容的Fragment
    protected var fragmentList: MutableList<Fragment> = ArrayList()
    // Title的数据List
    protected var titleList: MutableList<String> = ArrayList()

    override fun getLayoutId(): Int {
        return R.layout.fragment_tab
    }

    override fun onViewCreated() {
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        tableLayout = rootView?.findViewById(R.id.tab_layout)
        tableLayout!!.setPadding(0, 0, 0, 0)
        tableLayout!!.visibility = View.GONE

        coverView = rootView?.findViewById(R.id.tab_cover)

        viewPager = rootView?.findViewById(R.id.viewpager)
        viewPager!!.adapter = viewPagerAdapter

        // tableLayout 与 ViewPager 绑定
        tableLayout!!.setupWithViewPager(viewPager)

        loadStatusView = rootView?.findViewById(R.id.load_status)
        loadStatusView!!.showProgress()
    }

    override val isShown: Boolean
        get() = TODO("not implemented")

    override fun showLoading() {
        loadStatusView?.showProgress()
    }

    override fun hideLoading() {
        loadStatusView?.dismiss()
        tableLayout?.visibility = View.VISIBLE
        coverView?.visibility = View.VISIBLE
    }

    override fun showError() {
        tableLayout?.visibility = View.GONE
        loadStatusView?.showError()
    }

    override fun showEmpty() {
        tableLayout?.visibility = View.GONE
        loadStatusView?.showEmpty()
    }

    override fun showMessage(tips: String) {
    }

    fun refreshViewPagerData() {
        if(titleList.size > 0){
            hideLoading()
        }
        viewPagerAdapter?.notifyDataSetChanged()
    }

    open fun onGetItemByPostion(position: Int){}

    inner class ViewPagerAdapter(fm: FragmentManager)
        : FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItemId(position: Int): Long {
            return fragmentList[position].hashCode().toLong()
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            // FIXME 为啥会调用多次？
//            Log.d("kingz","setPrimaryItem pos=$position")
            currentFragment = `object` as Fragment
            super.setPrimaryItem(container, position, `object`)
        }

        override fun getItem(position: Int): Fragment {
            // 好像只有第一次加载的时候才会调用
//            Log.d("kingz","getItem pos=$position")
            onGetItemByPostion(position)
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }
    }
}
