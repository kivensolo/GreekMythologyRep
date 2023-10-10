package com.zeke.home.fragments

import android.app.ActivityOptions
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import android.widget.Toast
import com.kingz.module.common.BaseActivity
import com.kingz.module.common.base.BaseFragment
import com.kingz.module.common.bean.DemoGroup
import com.kingz.module.common.bean.DemoSample
import com.kingz.module.common.router.Router
import com.kingz.module.common.setting.SettingUtil
import com.kingz.module.home.R
import com.zeke.home.adapter.DemoFragmentExpandableListAdapter
import com.zeke.home.contract.DemoContract
import com.zeke.home.presenters.DemoPresenter
import com.zeke.kangaroo.view.animation.AnimatedExpandableListView
import com.zeke.kangaroo.zlog.ZLog


/**
 * author：KingZ
 * date：2020/2/15
 * description：动画伸缩扩展列表的Demo样例Fragment
 */
class ExpandableDemoFragment : BaseFragment(), DemoContract.View,
    ExpandableListView.OnChildClickListener, View.OnClickListener {

    private var mPresenter: DemoPresenter = DemoPresenter(this)
    private var expandAdapter: DemoFragmentExpandableListAdapter? = null
    private var listView: AnimatedExpandableListView? = null


    override val isShown: Boolean
        get() = activity != null && (activity as BaseActivity).isActivityShow && isVisible

    override fun hideLoading() {}

    override fun showLoading() {}

    override fun showError() {
    }

    override fun showEmpty() {
    }

    override fun showMessage(tips: String) {}

    override fun getLayoutId(): Int {
        return R.layout.fragment_all_demo
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listView = rootView?.findViewById(android.R.id.list)
        expandAdapter = DemoFragmentExpandableListAdapter(context)
        listView?.apply {
            setAdapter(expandAdapter)
            setOnGroupClickListener(ImpOnGroupClickListener())
            setOnChildClickListener(this@ExpandableDemoFragment)
        }
        mPresenter.getDemoInfo(activity!!)
    }

    override fun onResume() {
        super.onResume()
        expandAdapter?.updateCheckedColor(SettingUtil.getAppThemeColor())
    }


    override fun showDemoInfo(data: MutableList<DemoGroup>) {
        ZLog.d(TAG, "showRecomInfo()~~")
        expandAdapter!!.setSampleGroups(data)
    }


    override fun onClick(v: View?) {
    }

    override fun onChildClick(parent: ExpandableListView?, v: View?, groupPosition: Int, childPosition: Int, id: Long): Boolean {
        val opts = ActivityOptions.makeCustomAnimation(activity, R.anim.fade, R.anim.hold)
//		ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
        activity!!.overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit)
        val data = expandAdapter!!.getChild(groupPosition, childPosition) as DemoSample
        if(data.isRouterMode()){
            Router.startActivity(data.path)
        }else{
            val intent = data.buildIntent(activity!!)
            if (intent == null) {
                Toast.makeText(activity,
                    "Target page resolve failed.Please confirm class path!",
                    Toast.LENGTH_SHORT).show()
            } else {
                startActivity(intent, opts.toBundle())
            }
        }

        return true
    }

    private inner class ImpOnGroupClickListener : ExpandableListView.OnGroupClickListener {

        override fun onGroupClick(parent: ExpandableListView, v: View, groupPosition: Int, id: Long): Boolean {
            // We call collapseGroupWithAnimation(int)/expandGroupWithAnimation(int) to animate group
            // expansion/collapse.
            if (listView!!.isGroupExpanded(groupPosition)) {
                listView!!.collapseGroupWithAnimation(groupPosition)
            } else {
                listView!!.expandGroupWithAnimation(groupPosition)
            }
            return true
        }
    }

}