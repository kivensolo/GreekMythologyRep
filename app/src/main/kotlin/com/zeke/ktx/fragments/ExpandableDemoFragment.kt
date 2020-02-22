package com.zeke.ktx.fragments

import android.app.ActivityOptions
import android.view.View
import android.widget.ExpandableListView
import android.widget.Toast
import com.kingz.adapter.DemoFragmentExpandableListAdapter
import com.kingz.customdemo.R
import com.zeke.kangaroo.utils.ZLog
import com.zeke.kangaroo.view.animation.AnimatedExpandableListView
import com.zeke.ktx.base.BaseActivity
import com.zeke.ktx.base.BaseFragment
import com.zeke.ktx.player.contract.DemoContract
import com.zeke.ktx.player.entity.DemoGroup
import com.zeke.ktx.player.entity.DemoSample
import com.zeke.ktx.player.presenter.DemoPresenter

/**
 * author：KingZ
 * date：2020/2/15
 * description：动画伸缩扩展列表的Demo样例Fragment
 */
class ExpandableDemoFragment : BaseFragment()
        , DemoContract.View
        , ExpandableListView.OnChildClickListener
        , View.OnClickListener {

    private var mPresenter: DemoPresenter = DemoPresenter(this)
    private var expandAdapter: DemoFragmentExpandableListAdapter? = null
    private var listView: AnimatedExpandableListView? = null

    companion object {
        //FIXME 如何去加载so库？
        // System.loadLibrary("testNative-lib")
    }

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

    override fun initView() {
        listView = rootView.findViewById(android.R.id.list)
        expandAdapter = DemoFragmentExpandableListAdapter(context)
        listView!!.setAdapter(expandAdapter)
        listView!!.setOnGroupClickListener(ImpOnGroupClickListener())
        listView!!.setOnChildClickListener(this)
    }


    override fun showDemoInfo(data: MutableList<DemoGroup>) {
        ZLog.d(TAG, "showRecomInfo()~~")
        expandAdapter!!.setSampleGroups(data!!)
    }


    override fun initData() {
        mPresenter.getDemoInfo(activity!!)
    }

    override fun onClick(v: View?) {
    }

    override fun onChildClick(parent: ExpandableListView?, v: View?, groupPosition: Int, childPosition: Int, id: Long): Boolean {
        val opts = ActivityOptions.makeCustomAnimation(activity, R.anim.fade, R.anim.hold)
//		ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
        activity!!.overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit)
        val data = expandAdapter!!.getChild(groupPosition, childPosition) as DemoSample
        val intent = data.buildIntent(activity!!)
        if (intent == null) {
            Toast.makeText(activity,
                    "Target page resolve failed.Please confirm class path!",
                    Toast.LENGTH_SHORT)
                    .show()
        } else {
            startActivity(intent, opts.toBundle())
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

    external fun stringFromJNI(): String
}