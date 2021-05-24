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
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.kingz.base.BaseVMActivity
import com.kingz.base.factory.ViewModelFactory
import com.kingz.database.entity.BaseEntity
import com.kingz.module.common.ext.startActivity
import com.kingz.module.common.router.RPath
import com.kingz.module.common.router.Router
import com.kingz.module.common.utils.PermissionUtils
import com.kingz.module.common.utils.RandomUtils
import com.kingz.module.home.BuildConfig
import com.kingz.module.home.R
import com.kingz.module.wanandroid.WADConstants
import com.kingz.module.wanandroid.activity.AppBarActivity
import com.module.slide.SuperSlidingPaneLayout
import com.module.tools.ColorUtils
import com.zeke.home.eyepetizer.fragemnts.EyepetizerContentFragment
import com.zeke.home.fragments.home.HomeContainerFragment
import com.zeke.home.fragments.home.HomeLiveFragment
import com.zeke.home.model.HomeSongModel
import com.zeke.home.service.NSDService
import com.zeke.home.wanandroid.viewmodel.HomeViewModel
import com.zeke.kangaroo.utils.ZLog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_bottom_layout.*
import kotlinx.android.synthetic.main.slide_menu_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.String


/**
 * 首页
 */
@Route(path = RPath.PAGE_MAIN)
class HomeActivity : BaseVMActivity(),ISwitcher {

    // 知识专栏(玩Android、Demo等)
    private lateinit var homeKnowlegeFragment: HomeContainerFragment
    // 视频直播
    private lateinit var homeLiveFragment: HomeLiveFragment
    // 开眼视频
    private lateinit var homeOpenEyeFragment: EyepetizerContentFragment

    private lateinit var panelSlidelLsr: HomePanelSlidelLsr

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

        initFragments()

        initBottomTab()

        initSlidingPaneLayout()

        initSlideMenuView()

//        requestPermission()
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
    }

    private fun initSlidingPaneLayout() {
        panelSlidelLsr = HomePanelSlidelLsr()
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

    /**
     * 初始化首页的Fragment
     */
    private fun initFragments() {
        homeKnowlegeFragment = HomeContainerFragment()
        homeLiveFragment = HomeLiveFragment()
        homeOpenEyeFragment = EyepetizerContentFragment()

        with(supportFragmentManager.beginTransaction()){
            add(R.id.content, homeKnowlegeFragment)
            add(R.id.content, homeLiveFragment)
            add(R.id.content, homeOpenEyeFragment)
            commit()
        }

        supportFragmentManager.beginTransaction()
            .show(homeKnowlegeFragment)
            .hide(homeLiveFragment)
            .hide(homeOpenEyeFragment)
            .commit()
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.userInfoLiveData.observe(this, Observer {
            ZLog.d("userInfoLiveData onChanged: $it")
            if (it == null) {
                Router.startActivity(RPath.PAGE_LOGIN)
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

    private fun initBottomTab() {
        //默认选中页
        tabKnowlegaPage.isChecked = true
        tabKnowlegaPage.setTextColor(ContextCompat.getColor(this, R.color.white))

        tabKnowlegaPage.setOnClickListener(this)
        tabLive.setOnClickListener(this)
        tabPublish.setOnClickListener(this)
        tabEyetizer.setOnClickListener(this)
        tabMine.setOnClickListener(this)
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

    override fun switchFragment(@ISwitcher.ButtomType type: Int) {
        ZLog.d("switchFragment:$type")
        resetTabState()
        when (type) {
            ISwitcher.TYPE_KNOWLEGA -> {
//                ZLog.d("switchFragment:" + BitmapUtils.native_get_Hello())
                tabKnowlegaPage.setTextColor(ContextCompat.getColor(this, R.color.white))
                fragmentsChange(
                    homeKnowlegeFragment,
                    homeLiveFragment,
                    homeOpenEyeFragment
                )
            }
            ISwitcher.TYPE_LIVE -> {
                tabLive.setTextColor(ContextCompat.getColor(this, R.color.white))
                fragmentsChange(
                    homeLiveFragment,
                    homeKnowlegeFragment,
                    homeOpenEyeFragment
                )
            }
            ISwitcher.TYPE_EYEPETIZER -> {
                //setNavigationBarColor(resources.getColor(R.color.google_red))
                fragmentsChange(
                    homeOpenEyeFragment,
                    homeKnowlegeFragment,
                    homeLiveFragment
                )
            }
            ISwitcher.TYPE_MINE -> fragmentsChange(
                null,
                homeKnowlegeFragment,
                homeLiveFragment,
                homeOpenEyeFragment
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
            R.id.iv_title_menu -> {
                if (slidPanelLayout?.isOpen == true) {
                    slidPanelLayout?.closePane()
                } else {
                    slidPanelLayout?.openPane()
                }
            }
            R.id.iv_toolbar_right -> {
                //TODO 进行搜索页跳转
            }
            // ----- 老AppBarLayout

            R.id.tvVersion -> {
                clickVersion(true)
            }

            // ---- 新版Buttom
            R.id.tabKnowlegaPage -> switchFragment(ISwitcher.TYPE_KNOWLEGA)
            R.id.tabLive -> switchFragment(ISwitcher.TYPE_LIVE)
            R.id.tabEyetizer -> switchFragment(ISwitcher.TYPE_EYEPETIZER)
            R.id.tabMine -> switchFragment(ISwitcher.TYPE_MINE)
        }
    }

    private fun resetTabState() {
        val unselectedColor = ContextCompat.getColor(this, R.color.color_divider)
        tabKnowlegaPage.setTextColor(unselectedColor)
        tabLive.setTextColor(unselectedColor)
        tabEyetizer.setTextColor(unselectedColor)
        tabMine.setTextColor(unselectedColor)
    }

    private fun clickVersion(isClick: Boolean) {
        if (isClick) { addHeart(1) }
    }

    inner class HomePanelSlidelLsr : SuperSlidingPaneLayout.SimplePanelSlideListener() {
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

    /**
     * 测试修改导航栏效果
     */
    private fun setNavigationBarColor(@ColorInt color:Int){
        val barParams = ImmersionBar.with(this).barParams
        if(!barParams.hideNavigationBar){
            window.navigationBarColor = ColorUtils.blendARGB(
                color,
                barParams.navigationBarColorTransform,
                barParams.navigationBarAlpha
            )
        }
    }
}

