package com.zeke.ktx.fragments

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import com.kingz.customdemo.R
import com.zeke.ktx.base.BaseFragment
import com.zeke.ktx.base.presenter.IPresenter
import com.zeke.ktx.base.view.IView
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

    override fun initView() {
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        tableLayout = rootView.findViewById(R.id.tab_layout)
        tableLayout!!.setPadding(0, 0, 0, 0)
        tableLayout!!.visibility = View.GONE

        coverView = rootView.findViewById(R.id.tab_cover)

        viewPager = rootView.findViewById(R.id.viewpager)
        viewPager!!.adapter = viewPagerAdapter

        // tableLayout 与 ViewPager 绑定
        tableLayout!!.setupWithViewPager(viewPager)

        loadStatusView = rootView.findViewById(R.id.load_status)
        loadStatusView!!.showProgress()
    }

    override fun initData() {
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
        viewPagerAdapter!!.notifyDataSetChanged()
    }

    open fun onGetItemByPostion(position: Int){}

    inner class ViewPagerAdapter(fm: FragmentManager)
        : FragmentPagerAdapter(fm) {

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
