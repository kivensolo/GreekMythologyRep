package com.zeke.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.module.common.bean.MediaParams
import com.kingz.module.common.router.RPath
import com.zeke.module_player.R
import com.zeke.module_player.databinding.FullPlayerPageBinding
import com.zeke.play.PlayerActivity
import com.zeke.play.fragment.PlayFragment

/**
 * author：ZekeWang
 * date：2021/1/19
 * description：直播播放器
 */
@Route(path = RPath.PAGE_PLAYER)
class FullScreenPlayer : PlayerActivity() {
    private var playFragment: PlayFragment? = null
    private lateinit var fullPlayerPageBinding: FullPlayerPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initFragment()
        }
    }

    override fun getLayoutId(): Int {
        return INVALID_LAYOUT_ID
    }

    override fun getLayoutView(): View {
        fullPlayerPageBinding = FullPlayerPageBinding.inflate(LayoutInflater.from(this))
        return fullPlayerPageBinding.root
    }

    override fun initRotation() {}

    private fun initFragment() {
        val mediaParams = intent.getParcelableExtra<MediaParams>(MediaParams.PARAMS_KEY)
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        // 播放区域
        playFragment = fm.findFragmentByTag(TAG_VOD_PLAY) as PlayFragment?
        if (playFragment == null) {
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