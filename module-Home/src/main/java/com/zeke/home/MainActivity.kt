package com.zeke.home

import android.Manifest
import android.app.Instrumentation
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.core.app.ActivityCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.database.entity.BaseEntity
import com.kingz.module.common.BaseActivity
import com.kingz.module.common.base.BaseFragment
import com.kingz.module.common.router.RPath
import com.kingz.module.home.R
import com.zeke.home.fragments.HomeLiveFragment
import com.zeke.home.fragments.HomeRecomFragment
import com.zeke.home.fragments.ISwitcher
import com.zeke.home.model.HomeSongModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * KT版本首页
 */
@Route(path = RPath.PAGE_MAIN,group = "modle-home")
class MainActivity : BaseActivity(), ISwitcher {

    private var homeVodFragment: HomeRecomFragment? = null
    private var homeLiveFragment: HomeLiveFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragment()
        initBottom()
        permissionCheck()

        //Test Code
        // val apiManager = ApiManager.i()
        // val gitHubService = apiManager.setApi(GitHubService::class.java)
        // val userInfoObservable = gitHubService.getUserInfo("kivensolo")
        // apiManager.setSubscribe(userInfoObservable, object :SingleObserver<GitHubUserInfo>{
        //     override fun onSubscribe(d: Disposable) {
        //     }
        //
        //     override fun onError(e: Throwable) {
        //         ZLog.e("user onError")
        //     }
        //     override fun onSuccess(t: GitHubUserInfo) {
        //         ZLog.d("onSuccess  user is ${t.name}")
        //     }
        // })

        //TestCode
        HomeSongModel<BaseEntity>().testInsertData()
    }

    private fun initFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTS = fragmentManager.beginTransaction()
        homeVodFragment = HomeRecomFragment()
        homeLiveFragment = HomeLiveFragment()
        fragmentTS.add(R.id.content, homeVodFragment!!)
            .show(homeVodFragment!!)
        fragmentTS.add(R.id.content, homeLiveFragment!!)
            .hide(homeLiveFragment!!)
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
                Log.e("","Exception when sendKeyEvent:$e")
            }
        }
        GlobalScope.launch(Dispatchers.IO) { sendKeyEvent(KeyEvent.KEYCODE_HOME) }
    }
}

