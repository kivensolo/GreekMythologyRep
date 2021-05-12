package com.zeke.play.activities

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.LinearLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.module.common.CommonApp.Companion.getInstance
import com.kingz.module.common.bean.MediaParams
import com.zeke.module_player.R
import com.zeke.play.PlayerActivity
import com.zeke.play.VideoInfo
import com.zeke.play.fragment.PlayFragment
import com.zeke.play.fragment.VodDetailFragment
import com.zeke.play.fragment.VodDetailFragment.Companion.newInstance
import com.zeke.play.fragment.VodInfoFragment
import com.zeke.play.presenter.VodInfoPresenter
import java.util.concurrent.TimeUnit

/**
 * author：KingZ
 * date：2019/4/23
 * description：手机版本的详情页
 * 基于exo播放器组件
 */
@Route(path = "/module_MPlayer/detailPage")
class DetailPageActivty : PlayerActivity() {
    //播放区域的Fragment
    private var playFragment: PlayFragment? = null
    private var vodInfoFragment: VodInfoFragment? = null
    private var vodInfoPresenter: VodInfoPresenter? = null
    //影片详情介绍的Fragment
    private var vodDetailFragment: VodDetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initFragment()
        }
    }

    override fun getLayoutId(): Int = R.layout.detail_page

    /**
     * 初始化横竖屏
     */
    override fun initRotation() { //保存默认竖屏参数
        portraitParams = findViewById<View>(R.id.player_content).layoutParams as LinearLayout.LayoutParams
        //获取当前系统横竖屏值
        val autoRotation = Settings.System.getInt(
            contentResolver,
            Settings.System.ACCELEROMETER_ROTATION,
            0
        )
        getInstance().postDelayToMainLooper(Runnable {
            if (!isFinishing && autoRotation == 1) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }, TimeUnit.SECONDS.toMillis(2))
    }

    private fun initFragment() {
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        // 播放区域
        playFragment = fm.findFragmentByTag(TAG_VOD_PLAY) as PlayFragment?
        if (playFragment == null) { // 嫦娥探月
            // http://video.chinanews.com/flv/2019/04/23/400/111773_web.mp4
            val mediaParams = MediaParams()
            mediaParams.videoUrl = "https://vfx.mtime.cn/Video/2020/05/14/mp4/200514070325395613_1080.mp4"
            playFragment = PlayFragment.newInstance(mediaParams)
            fragmentTransaction.add(R.id.player_content, playFragment!!, TAG_VOD_PLAY)
        }
        // 影片信息区域
        vodInfoFragment = fm.findFragmentByTag(TAG_VOD_INFO) as VodInfoFragment?
        if (vodInfoFragment == null) {
            vodInfoFragment = VodInfoFragment()
            fragmentTransaction.add(R.id.content_layout, vodInfoFragment!!, TAG_VOD_INFO)
        } else {
            vodInfoPresenter = VodInfoPresenter(vodInfoFragment)
        }
        fragmentTransaction.show(vodInfoFragment!!)
        fragmentTransaction.commit()
    }

    override fun onDestroy() {
        playFragment?.onDestroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //目前是横屏的话
            playFragment?.onBackPressed()
        } else if (vodDetailFragment != null
            && vodDetailFragment!!.isAdded
            && vodDetailFragment!!.isVisible) {
            // 隐藏影片详情简介fragment
            supportFragmentManager.beginTransaction().remove(vodDetailFragment!!).commit()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * 展示&收起 详情页
     * @param isShow    是否显示
     */
    fun showOrDismissVideoDetail(isShow: Boolean, videoInfo: VideoInfo?) {
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        vodDetailFragment = fm.findFragmentByTag(TAG_VOD_DETAIL) as VodDetailFragment?
        if (vodDetailFragment == null) {
            vodDetailFragment = newInstance(videoInfo)
        }
        if (isShow) {
            if (vodDetailFragment?.isAdded == false) {
                fragmentTransaction.add(R.id.content_layout,vodDetailFragment!!,TAG_VOD_DETAIL)
            }
            fragmentTransaction.show(vodDetailFragment!!)
        } else {
            fragmentTransaction.remove(vodDetailFragment!!)
        }
        fragmentTransaction.commit()
    }

    companion object {
        const val TAG = "DetailPageActivty"
    }
}