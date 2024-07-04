package com.zeke.home.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.setting.SettingUtil
import com.kingz.module.home.R
import com.kingz.module.home.databinding.FragmentSystemBinding
import com.kingz.module.wanandroid.fragemnts.CommonFragment
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModelV2
import com.zeke.home.wanandroid.KnowledgeTreeFragment
import com.zeke.home.wanandroid.viewmodel.HomeViewModel
import com.zeke.kangaroo.zlog.ZLog

/**
 * date：2022/01/28
 * description：首页 - 体系 Fragment
 */
class HomeSystemFragment : CommonFragment<WanAndroidViewModelV2>() {
    private lateinit var bindinbg:FragmentSystemBinding

    private val titleList = mutableListOf<String>()
    private val fragmentList = mutableListOf<Fragment>()
    private val systemPagerAdapter: SystemPagerAdapter by lazy {
        SystemPagerAdapter(childFragmentManager, titleList, fragmentList)
    }

    override val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.build { HomeViewModel() }
    }

    override fun getLayoutResID(): Int {
        TODO("Not yet implemented")
    }
    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindinbg = FragmentSystemBinding.inflate(layoutInflater)
        rootView = bindinbg.root
        return rootView
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.systemLiveData.observe(this, androidx.lifecycle.Observer {
            if (it == null) {
                ZLog.d("artical LiveData request error. result is null.")
                showErrorStatus()
                return@Observer
            }
        })
    }

    override fun initView() {
        super.initView()
        titleList.add(getString(R.string.knowledge_system))
        titleList.add(getString(R.string.navigation))
        fragmentList.add(KnowledgeTreeFragment.getInstance())
        fragmentList.add(KnowledgeTreeFragment.getInstance())

        bindinbg.viewPager.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(bindinbg.tabLayout))
            adapter = systemPagerAdapter
        }
        bindinbg.tabLayout.run {
            setupWithViewPager(bindinbg.viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(bindinbg.viewPager))
            addOnTabSelectedListener(onTabSelectedListener)
        }
        refreshColor()
    }

    fun refreshColor() {
        bindinbg.tabLayout.setBackgroundColor(SettingUtil.getAppThemeColor())
    }

    override fun initData() {
        viewModel.getSystemInfo()
    }

    /**
     * onTabSelectedListener
     */
    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
            tab?.let {
                bindinbg.viewPager.setCurrentItem(it.position, false)
            }
        }
    }

    class SystemPagerAdapter(
        fm: FragmentManager,
        private val titleList: MutableList<String>,
        private val fragmentList: MutableList<Fragment>
    ) : FragmentPagerAdapter(fm) {

        override fun getItem(i: Int): Fragment = fragmentList[i]

        override fun getCount(): Int = fragmentList.size

        override fun getPageTitle(position: Int): CharSequence? = titleList[position]

    }
}
