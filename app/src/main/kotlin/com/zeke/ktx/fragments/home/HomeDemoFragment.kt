package com.zeke.ktx.fragments.home

import android.util.Log
import com.kingz.customdemo.R
import com.zeke.ktx.base.BaseActivity
import com.zeke.ktx.fragments.HomeBaseFragment
import com.zeke.ktx.fragments.demos.ExpandableDemoFragment
import com.zeke.ktx.fragments.demos.MagicIndicatorDemoFragment
import com.zeke.ktx.player.contract.DemoContract
import com.zeke.ktx.player.entity.DemoGroup
import com.zeke.ktx.player.presenter.DemoPresenter

/**
 * author：KingZ
 * date：2019/12/29
 * description：首页-Demo展示的Fragemnt
 */
class HomeDemoFragment : HomeBaseFragment<DemoPresenter>(),
        DemoContract.View {

    init {
        mPresenter = DemoPresenter(this)
    }


    override fun initData() {
        mPresenter.getDemoInfo(activity!!)
    }

    override fun showDemoInfo(data: MutableList<DemoGroup>?) {
        Log.d(TAG, "showDemoPageInfo onResult.")
        fragmentList.clear()
        titleList.clear()

        val titleData = arrayOf("Android Demo","MagicIndicator Demo","第三页")
        titleList.addAll(titleData)
        //TODO 创建Demo列表的Fragment数据
        // 遍历数据  itleList.add(live.name)
        // fragmentList.add(homeVodItemFragment)

        val demoFragment = ExpandableDemoFragment()
        fragmentList.add(demoFragment)
        val indicatorFragment = MagicIndicatorDemoFragment()
        fragmentList.add(indicatorFragment)
//        val demoFragment2 = ExpandableDemoFragment()
//        fragmentList.add(demoFragment2)
//        val demoFragment3 = ExpandableDemoFragment()
//        fragmentList.add(demoFragment3)
        refreshViewPagerData()
    }

    override fun onGetItemByPostion(position: Int) {
        super.onGetItemByPostion(position)
        val fragment = fragmentList[position]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_tab
    }

    override val isShown: Boolean
        get() = activity != null && (activity as BaseActivity)
                .isActivityShow && isVisible

    override fun initView() {
        super.initView()
        tableLayout?.setSelectedTabIndicatorColor(resources.getColor(R.color.hub_yellow))
        val normalColor = resources.getColor(android.R.color.white)
        val selectedColor = resources.getColor(R.color.hub_yellow)
        tableLayout?.setTabTextColors(normalColor, selectedColor)
    }

}
