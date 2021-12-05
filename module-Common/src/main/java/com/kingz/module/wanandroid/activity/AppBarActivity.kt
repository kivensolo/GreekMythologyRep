package com.kingz.module.wanandroid.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.base.BaseVMActivity
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.R
import com.kingz.module.common.router.RouterConfig
import com.kingz.module.common.setting.SettingUtil
import com.kingz.module.common.utils.StatusBarUtil
import com.kingz.module.wanandroid.WADConstants
import com.kingz.module.wanandroid.fragemnts.UserCollectionFragment
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModelV2
import com.module.tools.ColorUtils
import com.zeke.kangaroo.utils.ToastUtils
import com.zeke.kangaroo.zlog.ZLog
//import kotlinx.android.synthetic.main.view_page_header.*

/**
 * author：ZekeWang
 * date：2021/2/28
 * description：带有AppBar的Layout页面
 * 支持跟随主题色变化状态栏和导航栏颜色
 */
@Route(path = RouterConfig.PAGE_COMMON_APPBAR)
open class AppBarActivity : BaseVMActivity() {
    // 当前fragment
    private var curFragment: Fragment? = null
    private val mFragmentManager: FragmentManager
        get() = supportFragmentManager

    protected var defaultFragmentName:String = ""
    protected var defaultFragmentTitle:String = ""
    /**
     * theme color
     */
    protected var mThemeColor: Int = SettingUtil.getAppThemeColor()

    override val viewModel: WanAndroidViewModelV2 by viewModels {
        ViewModelFactory.build { WanAndroidViewModelV2() }
    }

    override fun getContentLayout(): Int = R.layout.layout_appbar_with_container

    override fun onResume() {
        super.onResume()
        initColor()
    }

    open fun initColor() {
        mThemeColor = if (SettingUtil.getIsNightMode()) {
            resources.getColor(R.color.colorPrimary)
        } else {
            SettingUtil.getAppThemeColor()
        }
        StatusBarUtil.setColor(this, mThemeColor, 0)
        if (this.supportActionBar != null) {
            this.supportActionBar?.setBackgroundDrawable(ColorDrawable(mThemeColor))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = CircleView.shiftColorDown(mThemeColor)
//            // 最近任务栏上色
//            val tDesc = ActivityManager.TaskDescription(
//                    getString(R.string.app_name),
//                    BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher),
//                    mThemeColor)
//            setTaskDescription(tDesc)
            if (SettingUtil.getNavBar()) {
                window.navigationBarColor = ColorUtils.shiftColorDown(mThemeColor)
            } else {
                window.navigationBarColor = Color.BLACK
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initCustomNavigationIcon()
    }

    private fun initCustomNavigationIcon() {
        findViewById<View>(R.id.toolbar_left)?.setOnClickListener {
            val trans: FragmentTransaction = mFragmentManager.beginTransaction()
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
        val initTitle: String = intent.getStringExtra(WADConstants.EXTRA_SHOW_FRAGMENT_TITLE) ?: defaultFragmentTitle
        findViewById<TextView>(R.id.toolbar_left)?.text = initTitle
        doFragmentInflate(intent)
        findViewById<Toolbar>(R.id.toolbar).run {
//            title = initTitle
            setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp)

            //FIXME 啥意思？
//            setSupportActionBar(this)
            // 设置toolBar内置左上角logo是否显示
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

// <editor-fold defaultstate="collapsed" desc="Fragment处理">
    private fun doFragmentInflate(intent: Intent) {
        val type = intent.getIntExtra(WADConstants.Key.KEY_FRAGEMTN_TYPE, 0)
        val name: String = intent.getStringExtra(WADConstants.EXTRA_SHOW_FRAGMENT_NAME) ?: defaultFragmentName
        val initArguments: Bundle = intent.getBundleExtra(WADConstants.EXTRA_SHOW_FRAGMENT_ARGUMENTS) ?: Bundle()
        ZLog.d("type = $type")
        when (type) {
            //兼容以前的代码
            WADConstants.Type.TYPE_TAB_COLLECT -> { //1
                findViewById<Toolbar>(R.id.toolbar)?.apply {
                    title = getString(R.string.mine_collect)
                }
                switchFragment(UserCollectionFragment())
            }
            else -> {
                switchFragment(name, initArguments)
            }
        }
    }

    protected fun switchFragment(fragmentName:String, args: Bundle) {
        val factory = supportFragmentManager.fragmentFactory
        val fragment = factory.instantiate(classLoader, fragmentName)
        fragment.arguments = args
        switchFragment(fragment)
    }

    /**
     * 统一切换fragment的方法,不实例化多个Fragment，避免卡顿,
     * Fragment中执行状态切换后的回调onHiddenChanged(boolean hidden)
     *
     * @param targetFragment 跳转目标fragment
     */
    private fun switchFragment(targetFragment: Fragment) {
        val trans: FragmentTransaction = supportFragmentManager.beginTransaction()
        // 转场自定义动画
       /* trans.setCustomAnimations(
            R.anim.translate_right_to_center,
            R.anim.translate_center_to_left, R.anim.translate_left_to_center,
            R.anim.translate_center_to_right
        )*/
        //系统默认自带动画
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if (!targetFragment.isAdded) {
            //需要添加时，隐藏之前的fragement
            curFragment?.apply {
                trans.hide(this)
            }
            //Add和replace的区别
            trans.add(R.id.flContainer, targetFragment,targetFragment::class.java.simpleName)
            // 放入后退栈，若只有一个fragment,还是会先隐藏
//            trans.addToBackStack(targetFragment::class.java.simpleName)
        } else {
            //targetFragment = mFragmentManager.findFragmentByTag(targetFragment.getClass().getSimpleName());
            trans.hide(curFragment!!).show(targetFragment)
        }
        trans.commitAllowingStateLoss()
        curFragment = targetFragment
    }


    private fun onBuildStartFragmentIntent(fragmentName: String, args: Bundle?, title: String?): Intent {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.setClass(this, javaClass)
        intent.putExtra(WADConstants.EXTRA_SHOW_FRAGMENT_NAME, fragmentName)
        intent.putExtra(WADConstants.EXTRA_SHOW_FRAGMENT_ARGUMENTS, args)
        intent.putExtra(WADConstants.EXTRA_SHOW_FRAGMENT_TITLE, title)
        return intent
    }

    protected fun startWithFragment(
        fragmentName: String, args: Bundle?,
        resultTo: Fragment?, resultRequestCode: Int, title: String?) {
        val intent = onBuildStartFragmentIntent(fragmentName, args, title)
        if (resultTo == null) {
            startActivity(intent)
        } else {
            resultTo.startActivityForResult(intent, resultRequestCode)
        }
    }
// </editor-fold>

}