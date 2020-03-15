package com.zeke.ktx.fragments.home

import android.support.design.widget.TabLayout
import android.util.Log
import com.kingz.customdemo.R
import com.zeke.ktx.base.BaseActivity
import com.zeke.ktx.fragments.HomeBaseFragment
import com.zeke.ktx.player.contract.LiveContract
import com.zeke.ktx.player.entity.Live
import com.zeke.ktx.player.entity.TimeTableData
import com.zeke.ktx.player.presenter.LivePresenter
import java.util.*

/**
 * date：2019/12/29
 * description：首页 - 直播 Fragment
 */
class HomeLiveFragment : HomeBaseFragment<LivePresenter>(), LiveContract.View {

    // 频道分组数据
    protected var channleData: HashMap<String, ArrayList<Live>>

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

    override fun onViewCreated() {
        super.onViewCreated() // 有公共控件可以复用 调用一次super
        // 具体子页面对公共View组件的设置
        tableLayout!!.tabMode = TabLayout.MODE_SCROLLABLE
        tableLayout!!.setSelectedTabIndicatorColor(resources.getColor(R.color.text_blue))
        tableLayout!!.setTabTextColors(resources.getColor(android.R.color.white),
                resources.getColor(R.color.text_blue))
        fetchLiveInfo()
    }

    private fun fetchLiveInfo() {
        mPresenter.getLiveInfo(activity!!)
    }

    override fun showLiveInfo(data: MutableList<Live>?) {
        Log.d("HomeLive", "showRecomInfo onResult.")
        fragmentList.clear()
        titleList.clear()
        val groupList:ArrayList<Live> = ArrayList()
        if (data != null) {
            for (live in data) {
                if(live.getItemType() == 0){
                    if(groupList.size > 0){
                        channleData[titleList[titleList.size-1]] = groupList.clone() as java.util.ArrayList<Live>
                    }
                    groupList.clear()
                    titleList.add(live.name)
                    val homeVodItemFragment = SimplePageContentFragment()
                    fragmentList.add(homeVodItemFragment)
                }else{
                    groupList.add(live)
                }
            }
        }

        if(groupList.size > 0){
            channleData[titleList[titleList.size-1]] = groupList.clone() as java.util.ArrayList<Live>
        }
        groupList.clear()

        refreshViewPagerData()
    }

    override fun onGetItemByPostion(position: Int) {
        super.onGetItemByPostion(position)
        val fragment = fragmentList[position]
        if(fragment is SimplePageContentFragment){
            val pageTitle = viewPagerAdapter!!.getPageTitle(position)
            val channelList = channleData[pageTitle]
            fragment.mRV.addAll(channelList)
            fragment.mRV.notifyDataSetChanged()
        }
    }

    override fun showTimeTable(data: MutableList<TimeTableData>) {
    }

    override fun showVideo(url: String) {
    }

}
