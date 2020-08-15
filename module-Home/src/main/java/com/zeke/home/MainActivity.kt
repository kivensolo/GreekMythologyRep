package com.zeke.home

import android.Manifest
import android.app.Instrumentation
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.base.BaseVMActivity
import com.kingz.base.factory.ViewModelFactory
import com.kingz.database.entity.BaseEntity
import com.kingz.module.common.base.BaseFragment
import com.kingz.module.common.router.RPath
import com.kingz.module.home.R
import com.zeke.home.fragments.HomeLiveFragment
import com.zeke.home.fragments.HomeRecomFragment
import com.zeke.home.fragments.ISwitcher
import com.zeke.home.model.HomeSongModel
import com.zeke.home.repository.HomePageRepository
import com.zeke.home.viewmodel.MainPageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Route(path = RPath.PAGE_MAIN, group = "modle-home")
class MainActivity : BaseVMActivity<HomePageRepository, MainPageViewModel>(), ISwitcher {

    private lateinit var homeVodFragment: HomeRecomFragment
    private lateinit var homeLiveFragment: HomeLiveFragment

    override val viewModel: MainPageViewModel by viewModels {
        ViewModelFactory.build { MainPageViewModel() }
    }

    override fun getContentView() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        initFragments()
        initBottom()
    }

    override fun initData(savedInstanceState: Bundle?) {
        permissionCheck()

        //TestCode
        lifecycleScope.launch(Dispatchers.IO) {
            HomeSongModel<BaseEntity>().testInsertData()
        }
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

}

