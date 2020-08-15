package com.zeke.home.fragments

import android.util.Log
import com.kingz.module.common.BaseActivity
import com.kingz.module.home.R
import com.zeke.home.contract.RecomPageContract
import com.zeke.home.entity.HomeRecomData
import com.zeke.home.presenters.RecomPresenter

/**
 * author：KingZ
 * date：2019/12/29
 * description：首页-推荐页展示的Fragemnt
 * 此部分暂时MVP
 */
class HomeRecomFragment : HomeBaseFragment<RecomPresenter>(), RecomPageContract.View {

    init {
        mPresenter = RecomPresenter(this)
    }

    override fun showRecomInfo(data: MutableList<HomeRecomData>?) {
        Log.d(TAG, "showDemoPageInfo onResult.")
        if(data == null || data.size == 0){
            showEmpty()
            return
        }
        fragmentList.clear()
        titleList.clear()

        data.forEach lit@ {
            titleList.add(it.name)
            when(it.type) {
                "recom" -> fragmentList.add(HomeWanAndroidFragment())
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

    override fun onViewCreated() {
        super.onViewCreated()
        tableLayout?.setSelectedTabIndicatorColor(resources.getColor(R.color.hub_yellow))
        val normalColor = resources.getColor(android.R.color.white)
        val selectedColor = resources.getColor(R.color.hub_yellow)
        tableLayout?.setTabTextColors(normalColor, selectedColor)

        mPresenter.getPageContent(activity!!)
    }

}
