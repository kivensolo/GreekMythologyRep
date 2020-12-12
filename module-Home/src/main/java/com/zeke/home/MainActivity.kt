package com.zeke.home

import android.Manifest
import android.app.Instrumentation
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.base.BaseVMActivity
import com.kingz.base.factory.ViewModelFactory
import com.kingz.database.entity.BaseEntity
import com.kingz.module.common.base.BaseFragment
import com.kingz.module.common.repository.WanAndroidRepository
import com.kingz.module.common.router.RPath
import com.kingz.module.common.utils.RandomUtils
import com.kingz.module.common.viewmodel.WanAndroidViewModel
import com.kingz.module.home.BuildConfig
import com.kingz.module.home.R
import com.module.slide.SuperSlidingPaneLayout
import com.module.slide.SuperSwipeRefreshLayout
import com.youth.banner.BannerConfig
import com.zeke.home.banner.BannerGlideImageLoader
import com.zeke.home.fragments.HomeLiveFragment
import com.zeke.home.fragments.HomeRecomFragment
import com.zeke.home.fragments.ISwitcher
import com.zeke.home.model.HomeSongModel
import com.zeke.kangaroo.utils.ZLog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.slide_menu_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.String

@Route(path = RPath.PAGE_MAIN)
class MainActivity : BaseVMActivity<WanAndroidRepository, WanAndroidViewModel>(), ISwitcher {

    private lateinit var homeVodFragment: HomeRecomFragment
    private lateinit var homeLiveFragment: HomeLiveFragment

    private lateinit var panelSlidelLsr: HomePanelSlidelLsr
    private var menuPanel: View? = null
    // 当前页数
    private var mCurPage = 1

    override val viewModel: WanAndroidViewModel by viewModels {
        ViewModelFactory.build { WanAndroidViewModel() }
    }

    override fun getContentView() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initFragments()
        initBottom()
        initSlidingPaneLayout()
        initSwipeRefreshLayout()
        initBannerView()
    }

    private fun initBannerView() {
        banner?.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
        banner?.setImageLoader(BannerGlideImageLoader())
        banner?.setOnBannerListener { position ->
            // TODO 进行web页面跳转
        }
    }

    private fun initSlidingPaneLayout() {
        panelSlidelLsr = HomePanelSlidelLsr()
        slidPanelLayout?.setPanelSlideListener(panelSlidelLsr)
        slidPanelLayout?.sliderFadeColor = ContextCompat.getColor(this, R.color.black_transparent)
        slidPanelLayout?.coveredFadeColor = ContextCompat.getColor(this, R.color.transparent)
        tvVersion?.text = String.format("v%s", BuildConfig.VERSION_NAME)
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout?.setOnRefreshListener { direction ->
            if (direction == SuperSwipeRefreshLayout.Direction.TOP) {
                mCurPage = 1
                //TODO 进行banner数据获取
            }
            val vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(70)
        }
//        swipeRefreshLayout?.isRefreshing = true
    }

    override fun initData(savedInstanceState: Bundle?) {
        permissionCheck()

        //TestCode
        lifecycleScope.launch(Dispatchers.IO) {
            HomeSongModel<BaseEntity>().testInsertData()
        }
        viewModel.getUserInfo()

        registerNSDServer(55589)
    }

    /**
     * 初始化首页的Fragment
     */
    private fun initFragments() {
        val fragmentManager = supportFragmentManager
        val fragmentTS = fragmentManager.beginTransaction()
        homeVodFragment = HomeRecomFragment()
        homeLiveFragment = HomeLiveFragment()
        fragmentTS.add(R.id.content, homeVodFragment)
            .show(homeVodFragment)
        fragmentTS.add(R.id.content, homeLiveFragment)
            .hide(homeLiveFragment)
        fragmentTS.commit()
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.userInfoLiveData.observe(this, Observer {
            ZLog.d("userInfoLiveData onChanged: $it")
            if (it == null) {
                tvLogout.visibility = View.INVISIBLE
                return@Observer
            }
            tvUser.text = it.username
            tvLogout.visibility = View.VISIBLE
        })
    }

    /**
     * 初始化底部相关
     */
    private fun initBottom() {
        val bottomController =
            MainBottomController(findViewById<View>(R.id.main_bottom_layout))
        bottomController.setListener(this)
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

    override fun switchFragment(@ISwitcher.ButtomType position: Int) {
        when (position) {
            ISwitcher.TYPE_VOD -> fragmentsChage(homeVodFragment, homeLiveFragment as BaseFragment)
            ISwitcher.TYPE_LIVE -> fragmentsChage(homeLiveFragment, homeVodFragment as BaseFragment)
            ISwitcher.TYPE_VIP -> fragmentsChage(null, homeVodFragment as BaseFragment)
            ISwitcher.TYPE_MINE -> fragmentsChage(null, homeVodFragment as BaseFragment)
        }//fragmentsChage(homeLiveFragment,homeVodFragment,homeVipFragment,homeMineFragment);
        //                fragmentsChage(homeVipFragment,homeVodFragment,homeLiveFragment,homeMineFragment);
        //                fragmentsChage(homeMineFragment,homeVodFragment,homeLiveFragment,homeVipFragment);
    }

    private fun fragmentsChage(block: BaseFragment?, vararg none: BaseFragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        for (fragment in none) {
            fragmentTransaction.hide(fragment)
        }
        if (block != null) {
            fragmentTransaction.show(block)
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

    fun OnClick(view: View?) {
        ZLog.d("OnClick view_id=" + view?.id)
        when (view?.id) {
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
            R.id.tvTitle -> {
                clickVersion(true)
            }
        }
    }

    private fun clickVersion(isClick: Boolean) {
        if (isClick) {
            addHeart(1)
        }
    }

    inner class HomePanelSlidelLsr : SuperSlidingPaneLayout.SimplePanelSlideListener() {
        override fun onPanelOpened(panel: View?) {
            super.onPanelOpened(panel)
            addHeart(RandomUtils.INSTANCE.random(5, 15))
        }
    }

    private fun addHeart(count: Int) {
        for (i in 0 until count) {
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

    override fun onDestroy() {
        super.onDestroy()
        stopNSDServer()
    }


    private var mServiceName: kotlin.String? = null
    private var nsdManager : NsdManager? = null

    /**
     * 进行NSD服务注册
     * NSD的Server服务器端的注册和监听最好是放在子线程中
     * 或者使用Android服务类Service来注册监听。
     * 服务器端直接在Activity监听，回调，拿到里面的数据显示在Activity上面是有点问题的
     */
    private fun registerNSDServer(port: Int) {
        // Create the NsdServiceInfo object, and populate it.
        val serviceInfo = NsdServiceInfo().apply {
            serviceName = "KingZ-NsdChat"
            serviceType = "_nsdchat._tcp"
            setPort(port)
        }

        nsdManager = (getSystemService(Context.NSD_SERVICE) as NsdManager).apply {
            // 异步的
            registerService(serviceInfo,
                NsdManager.PROTOCOL_DNS_SD,
                registrationListener)
        }
    }

    /**
     * 注销NSD服务，让客户端无法搜索到我们的NSD服务器
     */
    private fun stopNSDServer(){
        nsdManager?.unregisterService(registrationListener)
    }


    // 实例化注册监听器
    private val registrationListener = object : NsdManager.RegistrationListener {

        override fun onServiceRegistered(NsdServiceInfo: NsdServiceInfo) {
            ZLog.d("Kingz","onServiceRegistered")
            // Save the service name. Android may have changed it in order to
            // resolve a conflict, so update the name you initially requested
            // with the name Android actually used.
            mServiceName = NsdServiceInfo.serviceName

            // 任何需要在服务注册后运行的代码都必须包含在 onServiceRegistered() 方法中，
            // 因为registerService是异步的
        }

        override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            ZLog.d("Kingz","onRegistrationFailed")
            // Registration failed! Put debugging code here to determine why.
        }

        override fun onServiceUnregistered(arg0: NsdServiceInfo) {
            ZLog.d("Kingz","onServiceUnregistered")
            // Service has been unregistered. This only happens when you call
            // NsdManager.unregisterService() and pass in this listener.
        }

        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            ZLog.d("Kingz","onUnregistrationFailed")
            // Unregistration failed. Put debugging code here to determine why.
        }
    }

}

