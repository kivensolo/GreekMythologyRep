package com.zeke.home.fragments

import android.util.Log
import androidx.annotation.StringDef
import com.kingz.module.common.BaseActivity
import com.kingz.module.home.R
import com.zeke.home.contract.RecomPageContract
import com.zeke.home.entity.TemplatePageData
import com.zeke.home.presenters.RecomPresenter

/**
 * author：KingZ
 * date：2019/12/29
 * description：首页-推荐分类页展示的Fragemnts
 * 此部分暂时MVP
 */
class HomeRecomFragment : HomeBaseFragment<RecomPresenter>(), RecomPageContract.View {


    companion object{
        // 目前支持的TYPE
        const val TYPE_WAN_ANDROID = "wanAndroid"
        const val TYPE_DEMO = "demo"
        const val TYPE_MAGICINDICATOR = "magicIndicator"
    }
    init {
        mPresenter = RecomPresenter(this)
    }

    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    @StringDef(
        TYPE_WAN_ANDROID,TYPE_DEMO, TYPE_MAGICINDICATOR
    )
    annotation class PageRecomType

    override fun showRecomInfo(data: MutableList<TemplatePageData>?) {
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
                TYPE_WAN_ANDROID -> fragmentList.add(HomeWanAndroidFragment())
                TYPE_DEMO -> fragmentList.add(ExpandableDemoFragment())
                TYPE_MAGICINDICATOR -> fragmentList.add(MagicIndicatorDemoFragment())
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
