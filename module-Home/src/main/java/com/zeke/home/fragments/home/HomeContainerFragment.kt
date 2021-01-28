package com.zeke.home.fragments.home

import android.util.Log
import androidx.annotation.StringDef
import androidx.fragment.app.Fragment
import com.kingz.module.common.BaseActivity
import com.kingz.module.home.R
import com.zeke.home.contract.RecomPageContract
import com.zeke.home.entity.TemplatePageData
import com.zeke.home.fragments.ExpandableDemoFragment
import com.zeke.home.fragments.MagicIndicatorDemoFragment
import com.zeke.home.fragments.WanAndroidHomeFragment
import com.zeke.home.presenters.RecomPresenter

/**
 * author：KingZ
 * date：2019/12/29
 * description：首页-推荐分类页展示的Fragemnts
 * 此部分暂时MVP
 */
class HomeContainerFragment : HomeBaseFragment<RecomPresenter>(), RecomPageContract.View {


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
        TYPE_WAN_ANDROID,
        TYPE_DEMO,
        TYPE_MAGICINDICATOR
    )
    annotation class PageRecomType

    override fun showRecomInfo(data: MutableList<TemplatePageData>?) {
        Log.d(TAG, "showDemoPageInfo onResult.")
        if(data == null || data.size == 0){
            showEmpty()
            return
        }
        viewPagerAdapter?.setData(data)
        notifyPagerAdapterDataChanged()
    }

    /**
     * 根据数据类型创建Fragment页面
     */
    override fun createPageFragment(data: TemplatePageData, position: Int): Fragment {
        return when (data.type) {
            TYPE_WAN_ANDROID -> WanAndroidHomeFragment()
            TYPE_DEMO -> ExpandableDemoFragment()
            TYPE_MAGICINDICATOR -> MagicIndicatorDemoFragment()
            else -> ExpandableDemoFragment()
        }
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
