package com.zeke.home.fragments.home

import android.view.View
import androidx.annotation.CallSuper
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
import com.zeke.kangaroo.zlog.ZLog

/**
 * time: 2020-2-8 11:38
 * description：首页的几个Fragment的父类
 *
 *  MVP结构
 *
 * 使用 AppBarLayout + viewPager
 * |--AppBarLayout---|
 * |                 |
 * |    viewPager    |
 * |                 |
 * |-----------------|
 * 实现切页展示
 */
abstract class HomeBaseFragment<T : IPresenter> : BaseFragment(), IView {
    lateinit var mPresenter: T
    protected var tableLayout: TabLayout? = null
    protected var coverView: View? = null
    protected var viewPager: ViewPager? = null
    protected var currentFragment: Fragment? = null
    protected var viewPagerAdapter: HomePagerAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_tab_pager

    override fun onCreateViewReady() {
        super.onCreateViewReady()
        viewPagerAdapter = HomePagerAdapter(childFragmentManager, PageCreator())
        tableLayout = rootView?.findViewById(R.id.tab_layout)
        viewPager = rootView?.findViewById(R.id.viewpager)

        // viewPager 初始化
        viewPager?.run {
            adapter = viewPagerAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    ZLog.d("onPageScrolled()")
                }

                override fun onPageSelected(position: Int) {
                    currentFragment = viewPagerAdapter!!.getFragment(position)
                    ZLog.d("onPageSelected() position=$position, currentFragment=$currentFragment")
                }

                override fun onPageScrollStateChanged(state: Int) {
                    ZLog.d("onPageScrollStateChanged()")
                }
            })
        }

        //tabLayout 初始化
        with(tableLayout!!) {
            visibility = View.GONE

            // 设置文字默认及选中样式
            setSelectedTabIndicatorColor(resources.getColor(R.color.ic_green_light))
            val normalColor = resources.getColor(android.R.color.black)
            val selectedColor = resources.getColor(R.color.ic_green_light)
            setTabTextColors(normalColor, selectedColor)

            //tableLayout 与 ViewPager 绑定
            setupWithViewPager(viewPager)
        }

        loadStatusView = rootView?.findViewById(R.id.load_status)
        loadStatusView?.showLoading()

        val ivSearch: View? = rootView?.findViewById(R.id.ivSearch)
        ivSearch?.setOnClickListener {
//            SearchFragment().show(childFragmentManager, "searchFragment")
        }
    }
    @CallSuper
    override fun onFragmentRenderIsRender() {}

    override fun onDestroyView() {
        super.onDestroyView()
        viewPager?.clearOnPageChangeListeners()
    }

    override val isShown: Boolean
        get() = TODO("not implemented")

    override fun showLoading() {
        loadStatusView?.showLoading()
    }

    override fun hideLoading() {
        loadStatusView?.dismiss()
        tableLayout?.visibility = View.VISIBLE
//        coverView?.visibility = View.VISIBLE
    }

    override fun showError() {
        tableLayout?.visibility = View.GONE
        loadStatusView?.showError()
        //TODO 增加点击重试逻辑
    }

    override fun showEmpty() {
        tableLayout?.visibility = View.GONE
        loadStatusView?.showEmpty()
    }

    override fun showMessage(tips: String) {
    }

    /**
     * 根据具体数据创建对应页面的Fragment
     */
    abstract fun createPageFragment(data: TemplatePageData, position: Int):Fragment

    inner class HomePagerAdapter(
        fm: FragmentManager,
        creator: PagerFragCreator<TemplatePageData>
    ) : BasePagerAdapter<TemplatePageData>(fm, creator)

    inner class PageCreator : BasePagerAdapter.PagerFragCreator<TemplatePageData> {
        override fun createFragment(data: TemplatePageData, position: Int): Fragment {
            return createPageFragment(data, position)
        }

        override fun createTitle(data: TemplatePageData): String {
            return data.name
        }
    }
}
