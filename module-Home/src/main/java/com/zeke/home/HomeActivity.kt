package com.zeke.home

import android.Manifest
import android.app.Instrumentation
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayoutMediator
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.kingz.base.factory.ViewModelFactory
import com.kingz.database.entity.BaseEntity
import com.kingz.module.common.ext.startActivity
import com.kingz.module.common.router.Router
import com.kingz.module.common.router.RouterConfig
import com.kingz.module.common.setting.SettingUtil
import com.kingz.module.common.utils.PermissionUtils
import com.kingz.module.common.utils.RandomUtils
import com.kingz.module.home.BuildConfig
import com.kingz.module.home.R
import com.kingz.module.wanandroid.WADConstants
import com.kingz.module.wanandroid.activity.AppBarActivity
import com.module.slide.SuperSlidingPaneLayout
import com.zeke.eyepetizer.fragemnts.EyepetizerHomeFragment
import com.zeke.home.fragments.home.HomeContainerFragment
import com.zeke.home.fragments.home.HomeLiveFragment
import com.zeke.home.fragments.home.HomeSystemFragment
import com.zeke.home.model.HomeSongModel
import com.zeke.home.service.NSDService
import com.zeke.home.wanandroid.viewmodel.HomeViewModel
import com.zeke.kangaroo.utils.ToastUtils
import com.zeke.kangaroo.zlog.ZLog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.slide_menu_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.String


/**
 * 首页
 */
@Route(path = RouterConfig.PAGE_MAIN)
class HomeActivity : AppBarActivity(), View.OnClickListener{

    companion object {
        const val TAG = "HomeActivity"

        val tabMap = linkedMapOf(
            "知识" to R.drawable.ic_knowlege_nor,
            "直播" to R.drawable.ic_live_nor,
            "体系" to R.drawable.publish_add,
            "开眼" to R.drawable.ic_eyepetizer_nor,
            "我的" to R.drawable.ic_knowlege_nor
        )
        private val FRAGMENT_KNOWLEGE = 0x00
        private val FRAGMENT_LIVE = 0x01
        private val FRAGMENT_SYSTEM = 0x02
        private val FRAGMENT_EYEPETIZER = 0x03
        private val FRAGMENT_USER = 0x04
    }

    private lateinit var panelSlidelLsr: HomePanelSlideLsr
    private var mIsDoubleClieckLogout= false
    private val MSG_CLICK_LOGOUT_PASS = 0x0001
    private var mHandler:Handler = Handler(object : Handler.Callback {
        override fun handleMessage(msg: Message?): Boolean {
            when(msg!!.what){
                MSG_CLICK_LOGOUT_PASS -> {
                    mIsDoubleClieckLogout = false
                    return true
                }
            }
            return false
        }

    })

    override val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.build { HomeViewModel() }
    }

    override fun getContentLayout(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        initViewPager()
        initSlidingPaneLayout()
        initSlideMenuView()
    }

    private fun initSlideMenuView() {
        ivLogo?.setOnClickListener {
            val result = PermissionUtils.verifyReadAndWritePermissions(this, 0)
            //TODO 进行本地图片选择或者跳转
            ZLog.d("verifyReadAndWritePermissions = $result")
        }

        tvCollect?.setOnClickListener {
            startActivity<AppBarActivity> {
                val bundle = Bundle()
                bundle.putInt(WADConstants.Key.KEY_FRAGEMTN_TYPE,
                    WADConstants.Type.TYPE_TAB_COLLECT)
                it.putExtras(bundle)
            }
        }

        setting?.setOnClickListener {
            Router.startActivity(RouterConfig.PAGE_SETTING)
        }

        tvLogout?.setOnClickListener {
            if (mIsDoubleClieckLogout) {
                mIsDoubleClieckLogout = false
                launchIO { viewModel.userLogout() }
                return@setOnClickListener
            }
            showToast(getStringFromRes(R.string.logout_confirm_tips))
            mIsDoubleClieckLogout = true
            val msg = Message.obtain()
            msg.what = MSG_CLICK_LOGOUT_PASS
            mHandler.sendMessageDelayed(msg, 2 * 1000)
        }
    }

    //动态申请【外部目录读写权限】
    private fun requestPermission() {
        PermissionUtils.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        XXPermissions.with(this)
            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<kotlin.String>?, all: Boolean) {
                    if (all) {
                        ToastUtils.show(baseContext, "获取储存权限成功")
                    } else {
                        ToastUtils.show(baseContext, "获取部分权限成功，但部分权限未正常授予")
                    }
                }

                override fun onDenied(permissions: MutableList<kotlin.String>?, never: Boolean) {
                    super.onDenied(permissions, never)
                    if (never) {
                        ToastUtils.show(baseContext, "被永久拒绝授权，请手动授予储存权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(this@HomeActivity, permissions)
                    } else {
                        ToastUtils.show(baseContext, "获取储存权限失败")
                    }
                }
            })
    }

    private fun initSlidingPaneLayout() {
        panelSlidelLsr = HomePanelSlideLsr()
        slidPanelLayout?.setPanelSlideListener(panelSlidelLsr)
        slidPanelLayout?.sliderFadeColor = ContextCompat.getColor(this, R.color.black_transparent)
        slidPanelLayout?.coveredFadeColor = ContextCompat.getColor(this, R.color.transparent)
        tvVersion?.text = String.format("v%s", BuildConfig.VERSION_NAME)
    }
//   推迟到Web Fragment初始化之后

    override fun initData(savedInstanceState: Bundle?) {
        permissionCheck()

        //TestCode
        launchIO {
            HomeSongModel<BaseEntity>().testInsertData()
        }

        viewModel.getUserInfo()
        NSDService().init(baseContext)
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.userInfoLiveData.observe(this, Observer {
            ZLog.d("userInfoLiveData onChanged: $it")
            if (it == null) {
                Router.startActivity(RouterConfig.PAGE_LOGIN)
                finish()
                return@Observer
            }
            tvUser.text = it.username
            tvLogout.visibility = View.VISIBLE
        })
    }

    override fun onDestroy() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    private fun initViewPager() {
        //禁止左右滑动翻页
//        contentViewPager.isUserInputEnabled = false
        contentViewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = tabMap.size

            override fun createFragment(position: Int): Fragment = when (position) {
                FRAGMENT_KNOWLEGE -> HomeContainerFragment()
                FRAGMENT_LIVE -> HomeLiveFragment()
                FRAGMENT_SYSTEM -> HomeSystemFragment()
                FRAGMENT_EYEPETIZER -> EyepetizerHomeFragment()
                else -> HomeLiveFragment()  //TODO 后续接入新的Fragment
            }
        }
        contentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                ZLog.d("onPageSelected position=${position}")
            }
        })

        // Bind tablayout & viewpager2
        TabLayoutMediator(appBottomTabLayout,contentViewPager){ tab, position ->
            val key = tabMap.keys.elementAt(position)
            tab.text = key
            tab.setIcon(tabMap[key]!!)
        }.attach()

         // 设置tabIcon和tabText的间距
        for (i in 0 .. appBottomTabLayout.tabCount) {
            val params = appBottomTabLayout.getTabAt(i)?.view?.getChildAt(0)?.layoutParams as LinearLayout.LayoutParams?
            params?.bottomMargin = 3
            appBottomTabLayout.getTabAt(i)?.view?.getChildAt(0)?.layoutParams = params
        }

        // 拦截长按操作
        val tabStrip = appBottomTabLayout.getChildAt(0) as LinearLayout
        for (i in 0 until tabStrip.childCount) {
            tabStrip.getChildAt(i).setOnLongClickListener { true }
        }
        appBottomTabLayout.foregroundTintList
    }

    private fun permissionCheck() {
        val pm = packageManager
        val permissionResult = pm.checkPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            packageName
        )
        if (PackageManager.PERMISSION_GRANTED != permissionResult) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                0x1001
            )
        }
    }

    private fun fragmentsChange(show: Fragment?, vararg hide: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        for (fragment in hide) {
            fragmentTransaction.hide(fragment)
        }
        if (show != null) {
            fragmentTransaction.show(show)
        }
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        // 应用保活  使back按键触发的时候，不让系统finish当前Activity
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        ZLog.d("OnClick view_id=" + v?.id)
        when (v?.id) {
            // ----- 老AppBarLayout
            /*R.id.iv_title_menu -> {
                if (slidPanelLayout?.isOpen == true) {
                    slidPanelLayout?.closePane()
                } else {
                    slidPanelLayout?.openPane()
                }
            }
            R.id.iv_toolbar_right -> {
                //TODO 进行搜索页跳转
            }*/
            // ----- 老AppBarLayout

            R.id.tvVersion -> {
                clickVersion(true)
            }
        }
    }

    private fun clickVersion(isClick: Boolean) {
        if (isClick) { addHeart(1) }
    }

    inner class HomePanelSlideLsr : SuperSlidingPaneLayout.SimplePanelSlideListener() {
        override fun onPanelOpened(panel: View?) {
            super.onPanelOpened(panel)
            addHeart(RandomUtils.INSTANCE.random(5, 15))
        }
    }

    private fun addHeart(count: Int) {
        repeat(count) {
            flutteringLayout.addHeart()
        }
    }

    /**
     * 模拟Home键发送
     */
    private fun MockHomKey() {
        fun sendKeyEvent(keyCode: Int) {
            try {
                val inst = Instrumentation()
                inst.sendKeyDownUpSync(keyCode)
            } catch (e: Exception) {
                Log.e("", "Exception when sendKeyEvent:$e")
            }
        }
        lifecycleScope.launch(Dispatchers.IO) { sendKeyEvent(KeyEvent.KEYCODE_HOME) }
    }

    override fun initImmersionBar() {
        // Home页面设置Navigation消失无效
//        ImmersionBar.with(this)
//            .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
//            .navigationBarColor(R.color.colorPrimaryDark)
//            .init()
    }

    override fun initColor() {
        super.initColor()
        mThemeColor = SettingUtil.getAppThemeColor()
        findViewById<View>(R.id.slideMenuTopBkgLayout).apply {
            setBackgroundColor(mThemeColor)
        }
    }
}

