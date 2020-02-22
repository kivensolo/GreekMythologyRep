package com.zeke.ktx.fragments.home

import android.util.Log
import com.kingz.customdemo.R
import com.zeke.ktx.base.BaseActivity
import com.zeke.ktx.fragments.CommonPageFragment
import com.zeke.ktx.fragments.ExpandableDemoFragment
import com.zeke.ktx.fragments.HomeBaseFragment
import com.zeke.ktx.fragments.MagicIndicatorDemoFragment
import com.zeke.ktx.player.contract.RecomPageContract
import com.zeke.ktx.player.entity.HomeRecomData
import com.zeke.ktx.player.presenter.RecomPresenter

/**
 * author：KingZ
 * date：2019/12/29
 * description：首页-推荐页展示的Fragemnt
 */
class HomeRecomFragment : HomeBaseFragment<RecomPresenter>(),RecomPageContract.View {

    init {
        mPresenter = RecomPresenter(this)
    }


    override fun initData() {
        mPresenter.getPageContent(activity!!)
    }

    override fun showRecomInfo(data: MutableList<HomeRecomData>?) {
        Log.d(TAG, "showDemoPageInfo onResult.")
        if(data == null || data.size == 0){
            showEmpty()
            return
        }
        fragmentList.clear()
        titleList.clear()

        data?.forEach lit@ {
            titleList.add(it.name)
            when(it.type) {
                "recom" -> fragmentList.add(CommonPageFragment())
                "demo" -> fragmentList.add(ExpandableDemoFragment())
                "magicIndicator" -> fragmentList.add(MagicIndicatorDemoFragment())
            }
        }
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
