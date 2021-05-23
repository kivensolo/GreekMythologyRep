package com.zeke.home.fragments.home

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.kingz.base.adapter.BasePagerAdapter
import com.kingz.module.common.IView
import com.kingz.module.common.base.BaseFragment
import com.kingz.module.common.base.IPresenter
import com.kingz.module.home.R
import com.zeke.home.entity.TemplatePageData

/**
 * time: 2020-2-8 11:38
 * description：首页的几个Fragment的父类
 * 使用 TabLayout + viewPager + fragment 实现切页展示
 */
abstract class HomeBaseFragment<T : IPresenter> : BaseFragment(), IView {
    lateinit var mPresenter: T
    protected var tableLayout: TabLayout? = null
    protected var coverView: View? = null
    protected var viewPager: ViewPager? = null
    protected var currentFragment: Fragment? = null
    protected var viewPagerAdapter: HomePagerAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_tab
    }

    override fun onViewCreated() {
        viewPagerAdapter = HomePagerAdapter(childFragmentManager, PageCreator())
        tableLayout = rootView?.findViewById(R.id.tab_layout)
        tableLayout?.setPadding(0, 0, 0, 0)
        tableLayout?.visibility = View.GONE

//        coverView = rootView?.findViewById(R.id.tab_cover)

        viewPager = rootView?.findViewById(R.id.viewpager)
        viewPager?.adapter = viewPagerAdapter

        // tableLayout 与 ViewPager 绑定
        tableLayout?.setupWithViewPager(viewPager)

        loadStatusView = rootView?.findViewById(R.id.load_status)
        loadStatusView?.showProgress()
    }

    override val isShown: Boolean
        get() = TODO("not implemented")

    override fun showLoading() {
        loadStatusView?.showProgress()
    }

    override fun hideLoading() {
        loadStatusView?.dismiss()
        tableLayout?.visibility = View.VISIBLE
//        coverView?.visibility = View.VISIBLE
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

    fun notifyPagerAdapterDataChanged() {
        if(viewPagerAdapter?.hasData()!!){
            hideLoading()
        }
        viewPagerAdapter?.notifyDataSetChanged()
    }


    /**
     * 根据具体数据创建对应页面的Fragment
     */
    abstract fun createPageFragment(data: TemplatePageData, position: Int):Fragment

    inner class HomePagerAdapter(fm: FragmentManager,
                                 creator: PagerFragCreator<TemplatePageData>) :
        BasePagerAdapter<TemplatePageData>(fm, creator)

    inner class PageCreator : BasePagerAdapter.PagerFragCreator<TemplatePageData> {
        override fun createFragment(data: TemplatePageData, position: Int): Fragment {
            return createPageFragment(data, position)
        }

        override fun createTitle(data: TemplatePageData): String {
            return data.name
        }
    }
}
