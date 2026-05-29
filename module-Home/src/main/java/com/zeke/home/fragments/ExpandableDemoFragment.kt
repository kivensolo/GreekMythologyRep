package com.zeke.home.fragments

import android.app.ActivityOptions
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kingz.module.common.BaseActivity
import com.kingz.module.common.base.BaseFragment
import com.kingz.module.common.bean.DemoGroup
import com.kingz.module.common.bean.DemoSample
import com.kingz.module.common.router.Router
import com.kingz.module.home.R
import com.zeke.home.adapter.DemoGroupAdapter
import com.zeke.home.contract.DemoContract
import com.zeke.home.presenters.DemoPresenter

/**
 * author：KingZ
 * date：2020/2/15
 * uodated：2026/5/29
 * description：Demo样例Fragment，使用RecyclerView展示分组可折叠列表
 */
class ExpandableDemoFragment : BaseFragment(), DemoContract.View, View.OnClickListener {

    private var mPresenter: DemoPresenter = DemoPresenter(this)
    private var adapter: DemoGroupAdapter? = null
    private var recyclerView: RecyclerView? = null

    override val isShown: Boolean
        get() = activity != null && (activity as BaseActivity).isActivityShow && isVisible

    override fun hideLoading() {}

    override fun showLoading() {}

    override fun showError() {}

    override fun showEmpty() {}

    override fun showMessage(tips: String) {}

    override fun getLayoutId(): Int {
        return R.layout.fragment_all_demo
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = rootView?.findViewById(R.id.recycler_view)
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            isNestedScrollingEnabled = false // 滚动事件让 RecyclerView 自己处理滚动。
            (itemAnimator as? DefaultItemAnimator)?.let {
                it.supportsChangeAnimations = false //禁止 change 时的交叉淡入淡出
                it.addDuration = 0  // 插入子项时不再有淡入动画
                it.removeDuration = 0 //移除子项立即移除，无淡出延迟
            }
        }

        adapter = DemoGroupAdapter { sample -> navigateToPage(sample) }
        recyclerView?.adapter = adapter

        mPresenter.getDemoInfo(activity!!)
    }

    override fun onResume() {
        super.onResume()
        adapter?.updateThemeColor(com.kingz.module.common.setting.SettingUtil.getAppThemeColor())
    }

    override fun showDemoInfo(data: MutableList<DemoGroup>) {
        adapter?.setData(data)
    }

    override fun onClick(v: View?) {}

    private fun navigateToPage(sample: DemoSample) {
        val opts = ActivityOptions.makeCustomAnimation(activity, R.anim.fade, R.anim.hold)
        activity?.overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit)

        if (sample.isRouterMode()) {
            Router.startActivity(sample.registKey)
        } else {
            val intent = sample.buildIntent(activity!!)
            if (intent == null) {
                Toast.makeText(activity,
                    "Target page resolve failed.Please confirm class path!",
                    Toast.LENGTH_SHORT).show()
            } else {
                startActivity(intent, opts.toBundle())
            }
        }
    }
}
