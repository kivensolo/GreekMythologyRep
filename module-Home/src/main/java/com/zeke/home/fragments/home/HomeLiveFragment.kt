package com.zeke.home.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.kingz.module.common.BaseActivity
import com.kingz.module.home.R
import com.zeke.home.contract.LiveContract
import com.zeke.home.entity.Live
import com.zeke.home.entity.TemplatePageData
import com.zeke.home.entity.TimeTableData
import com.zeke.home.fragments.SimplePageContentFragment
import com.zeke.home.presenters.LivePresenter
import com.zeke.kangaroo.zlog.ZLog
import java.util.*

/**
 * date：2019/12/29
 * description：首页 - 直播 Fragment
 */
class HomeLiveFragment : HomeBaseFragment<LivePresenter>(), LiveContract.View {

    // 频道分组数据 <频道分组, 频道列表>
    private var channleData: HashMap<String, ArrayList<Live>>

    init {
        mPresenter = LivePresenter(this)
        channleData = HashMap()
    }

    override val isShown: Boolean
        get() = activity != null && (activity as BaseActivity).isActivityShow && isVisible
//    @Override
//    public boolean isShown() {
//        return getActivity() != null && ((BaseActivity) getActivity()).isActivityShow() && isVisible();
//    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_live_tab
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) // 有公共控件可以复用 调用一次super
        // 具体子页面对公共View组件的设置
        tableLayout?.apply {
            tabMode = TabLayout.MODE_SCROLLABLE
            setSelectedTabIndicatorColor(resources.getColor(R.color.text_blue))
            setTabTextColors(resources.getColor(android.R.color.white),
                resources.getColor(R.color.text_blue))
        }
        fetchLiveInfo()
    }

    private fun fetchLiveInfo() {
        mPresenter.getLiveInfo(activity!!)
    }

    override fun showLiveInfo(data: MutableList<Live>?) {
        ZLog.d("showLiveInfo onResult.  currentThread=" + Thread.currentThread().name)
        val groupTempList:ArrayList<Live> = ArrayList()
        val titleList:ArrayList<TemplatePageData> = ArrayList()
        if (data != null) {
            for (live in data) {
                if (!live.isTitle) {
                    // 当前分类下的频道数据列表搜集
                    groupTempList.add(live)
                } else {
                    if (groupTempList.size > 0) {
                        // 保存上次搜集的频道数据
                        channleData[titleList[titleList.size - 1].name] = groupTempList.clone() as ArrayList<Live>
                    }
                    groupTempList.clear()
                    // 直播频道分类数据
                    titleList.add(TemplatePageData(name = live.name))
                }
            }
        }

        if(groupTempList.size > 0){
            channleData[titleList[titleList.size-1].name] = groupTempList.clone() as ArrayList<Live>
        }
        groupTempList.clear()
        if(titleList.size > 0){
            hideLoading()
        }
        viewPagerAdapter?.setData(titleList)
    }

    override fun createPageFragment(data: TemplatePageData, position: Int): Fragment {
        val fragment = SimplePageContentFragment()
        val pageTitle = viewPagerAdapter?.getPageTitle(position)
        val channelList = channleData[pageTitle]
        fragment.mRV.addAll(channelList)
        fragment.mRV.notifyDataSetChanged()
        return fragment
    }

    override fun showTimeTable(data: MutableList<TimeTableData>) {
    }

    override fun showVideo(url: String) {
    }

}
