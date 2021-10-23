package com.zeke.eyepetizer.fragemnts

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.kingz.base.adapter.BasePagerAdapter
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.wanandroid.fragemnts.CommonFragment
import com.zeke.eyepetizer.bean.EyepetizerTabListInfo
import com.zeke.eyepetizer.bean.TabListBean
import com.zeke.eyepetizer.viewmodel.EyepetizerViewModel
import com.zeke.kangaroo.zlog.ZLog
import com.zeke.moudle_eyepetizer.R

/**
 * time: 2021-5-23 11:38
 * description：开眼视频的首页Fragment
 */
class EyepetizerHomeFragment : CommonFragment<EyepetizerViewModel>(){
    private var tableLayout: TabLayout? = null
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
        viewPager = rootView?.findViewById(R.id.viewpager)
        viewPager?.adapter = viewPagerAdapter

        // tableLayout 与 ViewPager 绑定
        tableLayout?.apply {
            setPadding(0, 0, 0, 0)
            setupWithViewPager(viewPager)
            visibility = View.GONE
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.tabListLiveData.observe(this, Observer { result ->
            ZLog.json("TabListLiveData onObserver","$result")
            dismissLoading()
            if(result != null){
                val tabListInfo = result as EyepetizerTabListInfo
                viewPagerAdapter?.setData(tabListInfo.tabInfo.tabList)
            }
        })
    }

    override fun initData() {
        super.initData()
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
        //TODO
        return EyepetizerPagerFragment(data.apiUrl)
    }

    inner class PagerAdapter(fm: FragmentManager, creator: PagerFragCreator<TabListBean>):
        BasePagerAdapter<TabListBean>(fm, creator)

    inner class PageCreator : BasePagerAdapter.PagerFragCreator<TabListBean> {
        override fun createFragment(data: TabListBean, position: Int): Fragment? {
            ZLog.d("createFragment +1: $data")
            return createPageFragment(data, position)
        }

        override fun createTitle(data: TabListBean): String {
            return data.name
        }
    }
}
