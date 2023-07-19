package com.kingz.module.wanandroid.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
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
import com.zeke.kangaroo.utils.ColorCompatUtils
import com.zeke.kangaroo.utils.ToastUtils
import com.zeke.kangaroo.zlog.ZLog
import java.lang.reflect.InvocationTargetException

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
    protected var pageTitle: String = ""
        set(value) {
            setToolBarTitle(value)
            field = value
        }

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
        mThemeColor = if (SettingUtil.isNightMode()) {
            ColorCompatUtils.getColor(resources, R.color.colorPrimary)
        } else {
            SettingUtil.getAppThemeColor()
        }
        StatusBarUtil.setColor(this, mThemeColor, 0)
        if (supportActionBar != null) {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(mThemeColor))
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
        //TODO 发送颜色变换的Even事件
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
        setToolBarTitle(intent.getStringExtra(WADConstants.EXTRA_SHOW_FRAGMENT_TITLE) ?: pageTitle)
        doFragmentInflate(intent)
        initToolBar()
    }

    private fun initToolBar() {
        findViewById<Toolbar>(R.id.toolbar).run {
            //enable action bar support.
            setSupportActionBar(this)
            supportActionBar?.apply {
                // 屏蔽默认title
                setDisplayShowTitleEnabled(false)
                // 设置toolBar内置左上角logo是否显示
                setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    protected fun setToolBarTitle(title: String){
        findViewById<TextView>(R.id.tvTitle)?.text = title
    }

    // <editor-fold defaultstate="collapsed" desc="Fragment处理">
    private fun doFragmentInflate(intent: Intent) {
        val type = intent.getIntExtra(WADConstants.Key.KEY_FRAGEMTN_TYPE, 0)
        val name: String = intent.getStringExtra(WADConstants.EXTRA_SHOW_FRAGMENT_NAME) ?: defaultFragmentName
        val initArguments: Bundle = intent.getBundleExtra(WADConstants.EXTRA_SHOW_FRAGMENT_ARGUMENTS) ?: Bundle()
        ZLog.d("type = $type")
        when (type) {
            //兼容以前的代码  TODo 此类逻辑可下沉
            WADConstants.Type.TYPE_TAB_COLLECT -> { //1
                findViewById<Toolbar>(R.id.toolbar)?.apply {
                    title = getString(R.string.mine_collect)
                }
                switchFragment(UserCollectionFragment())
            }
            else -> {
                switchFragment(name){
                    it.arguments = initArguments
                }
            }
        }
    }

    protected fun switchFragment(fragmentName: String, block: (frg: Fragment) -> Unit) {
        val factory = supportFragmentManager.fragmentFactory
        val fragment = factory.instantiate(classLoader, fragmentName)
        fragment.apply {
           block(this)
        }
        switchFragment(fragment)
    }

    /**
     * 统一切换fragment的方法,不实例化多个Fragment，避免卡顿,
     * Fragment中执行状态切换后的回调onHiddenChanged(boolean hidden)
     *
     * @param targetFragment 跳转目标fragment
     */
    private fun switchFragment(targetFragment: Fragment, addtoBackStack: Boolean = false) {
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
            trans.add(R.id.flContainer, targetFragment, targetFragment::class.java.simpleName)
            // 放入后退栈，若只有一个fragment,还是会先隐藏
            if(addtoBackStack){
                trans.addToBackStack(targetFragment::class.java.simpleName)
            }
        } else {
            //targetFragment = mFragmentManager.findFragmentByTag(targetFragment.getClass().getSimpleName());
            trans.hide(curFragment!!).show(targetFragment)
        }
        trans.commitAllowingStateLoss()
        curFragment = targetFragment
    }

    protected fun switchFragmentWithTag(tag: String):Fragment?{
        return supportFragmentManager.findFragmentByTag(tag)
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
        resultTo: Fragment?, resultRequestCode: Int, title: String?
    ) {
        val intent = onBuildStartFragmentIntent(fragmentName, args, title)
        if (resultTo == null) {
            startActivity(intent)
        } else {
            resultTo.startActivityForResult(intent, resultRequestCode)
        }
    }
// </editor-fold>

 // <editor-fold defaultstate="collapsed" desc="菜单设置">
    /**
     * 选项菜单显示前事件
     * @param menu
     * @return
     */
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        ZLog.i(TAG, "onPrepareOptionsMenu: ")
        if (menu != null) {
            setIconEnable(menu, true)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    /**
     * 选项菜单关闭后事件
     * @param menu
     */
    override fun onOptionsMenuClosed(menu: Menu?) {
        ZLog.i(TAG, "onOptionsMenuClosed: ")
        super.onOptionsMenuClosed(menu)
    }

    /**
     * Icon显示（Android 4.0以上版本需要反射方式方可显示图标，4.0之前不需要）
     * @param menu
     * @param enable
     */
    protected open fun setIconEnable(menu: Menu?, enable: Boolean) {
        if (menu != null) {
            //实现菜单title与Icon同时显示效果
            val menuClass = menu.javaClass
            if ("MenuBuilder" == menuClass.simpleName ) {
                try {
                    val method = menuClass.getDeclaredMethod(
                        "setOptionalIconsVisible",
                        java.lang.Boolean.TYPE
                    )
                    method.isAccessible = true
                    method.invoke(menu, enable)
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                }
            }
        }
    }
 // </editor-fold>

}