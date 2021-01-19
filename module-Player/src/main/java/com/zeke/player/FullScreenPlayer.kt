package com.zeke.player

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.zeke.module_player.R
import com.zeke.play.MediaParams
import com.zeke.play.PlayerActivity
import com.zeke.play.fragment.PlayFragment

/**
 * author：ZekeWang
 * date：2021/1/19
 * description：直播播放器
 */
@Route(path = "/module_MPlayer/playerPage")
class FullScreenPlayer : PlayerActivity() {
    private var playFragment: PlayFragment? = null

    companion object {
        const val EXTRA_PLAY_URL = "play_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.full_player_page
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun initRotation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    private fun initFragment() {
        val playUrl = intent.getStringExtra(EXTRA_PLAY_URL)
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        // 播放区域
        playFragment = fm.findFragmentByTag(TAG_VOD_PLAY) as PlayFragment?
        if (playFragment == null) {
            val mediaParams = MediaParams()
            mediaParams.videoUrl = playUrl
            playFragment = PlayFragment.newInstance(mediaParams)
            fragmentTransaction.add(R.id.root_layout, playFragment!!, TAG_LIVE_PLAY)
        }
        fragmentTransaction.show(playFragment!!)
        fragmentTransaction.commit()
    }

    override fun onDestroy() {
        playFragment?.onDestroy()

        super.onDestroy()
    }
}