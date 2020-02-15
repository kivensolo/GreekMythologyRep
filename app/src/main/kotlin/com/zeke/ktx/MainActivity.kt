package com.zeke.ktx

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import com.kingz.customdemo.R
import com.zeke.ktx.base.BaseActivity
import com.zeke.ktx.base.BaseFragment
import com.zeke.ktx.components.MainBottomController
import com.zeke.ktx.fragments.home.HomeDemoFragment
import com.zeke.ktx.fragments.home.HomeLiveFragment
import com.zeke.ktx.fragments.home.ISwitcher

/**
 * KT版本首页
 *
 */
class MainActivity : BaseActivity(), ISwitcher {

    private var homeVodFragment: HomeDemoFragment? = null
    private var homeLiveFragment: HomeLiveFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragment()
        initBottom()
        permissionCheck()
    }

    private fun initFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTS = fragmentManager.beginTransaction()
        homeVodFragment = HomeDemoFragment()
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
        val bottomController = MainBottomController(findViewById<View>(R.id.main_bottom_layout))
        bottomController.setListener(this)

    }

    private fun permissionCheck() {
        val pm = packageManager
        val permissionResult = pm.checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                packageName)
        if (PackageManager.PERMISSION_GRANTED != permissionResult) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0x1001)
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

}

