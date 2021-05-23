package com.zeke.home.eyepetizer.fragemnts

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.kingz.base.adapter.BasePagerAdapter
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.base.IRvScroller
import com.kingz.module.home.R
import com.kingz.module.wanandroid.bean.EyepetizerTabListInfo
import com.kingz.module.wanandroid.bean.EyepetizerTabListInfo.TabInfoBean.TabListBean
import com.kingz.module.wanandroid.fragemnts.CommonFragment
import com.kingz.module.wanandroid.viewmodel.EyepetizerViewModel
import com.zeke.kangaroo.utils.ZLog

/**
 * time: 2021-5-23 11:38
 * description：首页开眼视频的Fragment
 */
class EyepetizerContentFragment : CommonFragment<EyepetizerViewModel>(), IRvScroller {
    private var tableLayout: TabLayout? = null
    private var coverView: View? = null
    private var viewPager: ViewPager? = null
    private var currentFragment: Fragment? = null
    private var viewPagerAdapter: PagerAdapter? = null

    override fun getLayoutResID() = R.layout.fragment_metrail_tab

    override val viewModel: EyepetizerViewModel by viewModels {
        ViewModelFactory.build { EyepetizerViewModel() }
    }

    override fun initView() {
        super.initView()
        viewPagerAdapter = PagerAdapter(childFragmentManager, PageCreator())
        tableLayout = rootView?.findViewById(R.id.tab_layout)
//        coverView = rootView?.findViewById(R.id.tab_cover)
        viewPager = rootView?.findViewById(R.id.viewpager)
        viewPager?.adapter = viewPagerAdapter

        // tableLayout 与 ViewPager 绑定
        tableLayout?.apply {
            tabMode = TabLayout.MODE_SCROLLABLE
            setPadding(0, 0, 0, 0)
            setupWithViewPager(viewPager)
            setSelectedTabIndicatorColor(resources.getColor(R.color.hub_yellow))
            val normalColor = resources.getColor(android.R.color.white)
            val selectedColor = resources.getColor(R.color.hub_yellow)
            setTabTextColors(normalColor, selectedColor)
            visibility = View.GONE
        }

        loadStatusView = rootView?.findViewById(R.id.load_status)
        loadStatusView?.showProgress()
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.tabListLiveData.observe(this, Observer { result ->
            ZLog.d("tabListLiveData onObserver: $result")
            dismissLoading()
            if(result != null){
                val tabListInfo = result as EyepetizerTabListInfo
                viewPagerAdapter?.setData(tabListInfo.tabInfo?.tabList)
            }
        })
    }

    override fun lazyInit() {
        super.lazyInit()
        //TODO initData
        viewModel.getTabList()
    }

    override fun dismissLoading() {
        super.dismissLoading()
        tableLayout?.visibility = View.VISIBLE
    }

    /**
     * 根据具体数据创建对应页面的Fragment
     */
    fun createPageFragment(data: TabListBean, position: Int):Fragment?{
        return SimplePageContentVMFragment()
    }

    inner class PagerAdapter(fm: FragmentManager, creator: PagerFragCreator<TabListBean>):
        BasePagerAdapter<TabListBean>(fm, creator)

    inner class PageCreator : BasePagerAdapter.PagerFragCreator<TabListBean> {
        override fun createFragment(data: TabListBean, position: Int): Fragment? {
            ZLog.d("createFragment +1: $data")
            return createPageFragment(data, position)
        }

        override fun createTitle(data: TabListBean): String {
            ZLog.d("createTitle +1: ${data.name}")
            return data.name
        }
    }

    override fun scrollToTop() {
//        RvUtils.smoothScrollTop(mRecyclerView)
    }

    override fun scrollToTopRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
