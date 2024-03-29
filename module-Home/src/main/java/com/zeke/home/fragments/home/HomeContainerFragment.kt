package com.zeke.home.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringDef
import androidx.fragment.app.Fragment
import com.kingz.module.common.BaseActivity
import com.kingz.module.home.R
import com.module.slide.SuperSlidingPaneLayout
import com.zeke.home.contract.RecomPageContract
import com.zeke.home.entity.TemplatePageData
import com.zeke.home.fragments.ExpandableDemoFragment
import com.zeke.home.fragments.MagicIndicatorDemoFragment
import com.zeke.home.presenters.RecomPresenter
import com.zeke.home.wanandroid.WanAndroidHomeFragment
import com.zeke.kangaroo.zlog.ZLog

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

    override fun getLayoutId(): Int = R.layout.fragment_tab_pager

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
        hideLoading()
        viewPagerAdapter?.setData(data)
    }

    /**
     * 根据数据类型创建Fragment页面
     */
    override fun createPageFragment(data: TemplatePageData, position: Int): Fragment {
        return when (data.type) {
            TYPE_WAN_ANDROID -> {
                currentFragment = WanAndroidHomeFragment()
                //FIXME 兼容处理页面初次加载时不触发viewpagger onPageSelected的事件，导致无法点击fab后无效果。
                return currentFragment as WanAndroidHomeFragment
            }
            TYPE_DEMO -> ExpandableDemoFragment()
            TYPE_MAGICINDICATOR -> MagicIndicatorDemoFragment()
            else -> ExpandableDemoFragment()
        }
    }

    override val isShown: Boolean
        get() = activity != null && (activity as BaseActivity)
                .isActivityShow && isVisible

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.getPageContent(activity!!)
        //Menu
        tableLayout?.findViewById<View>(R.id.iv_tab_menu)?.setOnClickListener {
            activity?.findViewById<SuperSlidingPaneLayout>(R.id.slidPanelLayout)?.apply {
                openPane()
            }
        }
        rootViewInitFABInflate()
    }


    private fun rootViewInitFABInflate() {
        val fabView = rootView?.findViewById<View>(R.id.floating_action_btn)
        ZLog.d("kingz initFABInflate: fabView=[$fabView]")
        fabView?.setOnClickListener {
            if (currentFragment is WanAndroidHomeFragment) {
                (currentFragment as WanAndroidHomeFragment).scrollToTop()
            }
        }
    }

}
