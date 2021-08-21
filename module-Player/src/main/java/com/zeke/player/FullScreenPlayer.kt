package com.zeke.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.module.common.bean.MediaParams
import com.kingz.module.common.router.RouterConfig
import com.zeke.kangaroo.utils.ZLog
import com.zeke.module_player.R
import com.zeke.module_player.databinding.FullPlayerPageBinding
import com.zeke.play.PlayerActivity
import com.zeke.play.fragment.PlayFragment
import com.zeke.reactivehttp.base.BaseReactiveViewModel
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.internal.CustomAdapt

/**
 * author：ZekeWang
 * date：2021/1/19
 * description：直播播放器
 */
@Route(path = RouterConfig.PAGE_PLAYER)
class FullScreenPlayer : PlayerActivity(),CustomAdapt {
    private var playFragment: PlayFragment? = null
    private lateinit var fullPlayerPageBinding: FullPlayerPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        AutoSizeConfig.getInstance().isUseDeviceSize = true
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initFragment()
        }
    }

    override val viewModel: BaseReactiveViewModel
        get() = TODO("Use MVVM mode.")

    override fun getLayoutView(): View {
        fullPlayerPageBinding = FullPlayerPageBinding.inflate(LayoutInflater.from(this))
        return fullPlayerPageBinding.root
    }

    override fun initRotation() {}

    private fun initFragment() {
        val screenDensity:Float
        val screenDensityDpi:Int
        val SCREEN_WIDTH:Int
        val SCREEN_HEIGHT:Int
        resources.displayMetrics.let {
            SCREEN_WIDTH = it.widthPixels
            SCREEN_HEIGHT = it.heightPixels
            screenDensity = it.density
            screenDensityDpi = it.densityDpi
        }
         ZLog.i("SCREEN_SIZE = ($SCREEN_WIDTH,$SCREEN_HEIGHT)  " +
                 "Density=$screenDensity ,DensityDpi(Density*160)= $screenDensityDpi")

        val mediaParams = intent.getParcelableExtra<MediaParams>(MediaParams.PARAMS_KEY)
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        // 播放区域
        playFragment = fm.findFragmentByTag(TAG_VOD_PLAY) as PlayFragment?
        if (playFragment == null) {
            playFragment = PlayFragment.newInstance()
            playFragment?.setVideoInfo(mediaParams)
            fragmentTransaction.add(R.id.player_content, playFragment!!, TAG_LIVE_PLAY)
        }
        fragmentTransaction.show(playFragment!!)
        fragmentTransaction.commit()

        playFragment?.startPlay()
    }

    override fun onDestroy() {
        AutoSizeConfig.getInstance().isUseDeviceSize = false
        playFragment?.onDestroy()
        super.onDestroy()
    }

    override fun getContentLayout(): Int = R.layout.layout_invalid

    override fun initData(savedInstanceState: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 不按照宽度进行等比例适配, 现在以高度进行适配
     */
    override fun isBaseOnWidth(): Boolean {
        return false
    }

    /**
     * 返回高度适配的DP尺寸
     * 横屏模式下,高度以360DP为准(1080px)
     */
    override fun getSizeInDp(): Float {
        return 360F
    }
}