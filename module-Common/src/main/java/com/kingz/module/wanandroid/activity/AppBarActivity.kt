package com.kingz.module.wanandroid.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.base.BaseVMActivity
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.R
import com.kingz.module.common.router.RouterConfig
import com.kingz.module.wanandroid.WADConstants
import com.kingz.module.wanandroid.fragemnts.UserCollectionFragment
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModelV2
import com.zeke.kangaroo.utils.ToastUtils
import com.zeke.kangaroo.utils.ZLog
//import kotlinx.android.synthetic.main.view_page_header.*

/**
 * author：ZekeWang
 * date：2021/2/28
 * description：带有AppBar的Layout页面
 */
@Route(path = RouterConfig.PAGE_COMMON_APPBAR)
class AppBarActivity : BaseVMActivity() {
    // 当前fragment
    private var curFragment: Fragment? = null
    private var mFragmentManager: FragmentManager? = null

    override val viewModel: WanAndroidViewModelV2 by viewModels {
        ViewModelFactory.build { WanAndroidViewModelV2() }
    }

    override fun getContentLayout(): Int = R.layout.layout_appbar_recycler_list_page

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mFragmentManager = supportFragmentManager
        findViewById<View>(R.id.ivLeft)?.setOnClickListener {
            val trans: FragmentTransaction = mFragmentManager!!.beginTransaction()
            trans.remove(curFragment!!)
            trans.commitAllowingStateLoss()
            finish()
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        val intent = intent
        if (intent == null) {
            ToastUtils.show(baseContext, "intent data is null，finish page.")
            finish()
            return
        }
        val type = intent.getIntExtra(WADConstants.Key.KEY_FRAGEMTN_TYPE, 0)
        if (type == 0) {
            ToastUtils.show(baseContext, "非法类型参数")
            return
        }
        ZLog.d("type = $type")
        when (type) {
            WADConstants.Type.TYPE_TAB_COLLECT -> { //1
                findViewById<TextView>(R.id.tvTitle)?.setText(R.string.mine_collect)
                switchFragment(UserCollectionFragment())
            }
        }

    }

    private fun switchFragment(targetFragment: Fragment?) {
        switchFragment(targetFragment, null)
    }

    /**
     * 统一切换fragment的方法,不实例化多个Fragment，避免卡顿,
     * Fragment中执行状态切换后的回调onHiddenChanged(boolean hidden)
     *
     * @param targetFragment 跳转目标fragment
     */
    private fun switchFragment(targetFragment: Fragment?, args: Bundle?) {
        if (targetFragment == null) {
            ZLog.d("参数为空")
            return
        }
        if (mFragmentManager != null) {
            val trans: FragmentTransaction = mFragmentManager!!.beginTransaction()
            // 转场自定义动画
           /* trans.setCustomAnimations(
                R.anim.translate_right_to_center,
                R.anim.translate_center_to_left, R.anim.translate_left_to_center,
                R.anim.translate_center_to_right
            )*/
            if (!targetFragment.isAdded) { // 首次执行curFragment为空，需要判断
                if (curFragment != null) {
                    trans.hide(curFragment!!)
                }
                trans.add(R.id.flContainer, targetFragment,targetFragment::class.java.simpleName)
                // 按返回键会移除掉次fragment
//                trans.addToBackStack(targetFragment::class.java.simpleName)
                //trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);    //设置动画效果
            } else {
                //targetFragment = mFragmentManager.findFragmentByTag(targetFragment.getClass().getSimpleName());
                trans.hide(curFragment!!).show(targetFragment)
            }
            trans.commitAllowingStateLoss()
            curFragment = targetFragment
        }
    }
}