package com.zeke.player

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.module.common.bean.MediaParams
import com.kingz.module.common.router.RouterConfig
import com.zeke.kangaroo.zlog.ZLog
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
class SimplePlayerActivity : PlayerActivity(),CustomAdapt {
    private var playFragment: PlayFragment? = null
    private lateinit var fullPlayerPageBinding: FullPlayerPageBinding

    private val mFragmentLifecycleCallbacks: FragmentManager.FragmentLifecycleCallbacks =
        object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentPreAttached(
                @NonNull fm: FragmentManager,
                @NonNull f: Fragment,
                @NonNull context: Context
            ) {
                super.onFragmentPreAttached(fm, f, context)
                ZLog.i(TAG, "onFragmentPreAttached: " + f::class.java.simpleName)
            }

            override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment,savedInstanceState: Bundle?) {
                super.onFragmentActivityCreated(fm, f, savedInstanceState)
                ZLog.i(TAG, "onFragmentActivityCreated: " + f::class.java.simpleName)
                val mediaParams = intent.getParcelableExtra<MediaParams>(MediaParams.PARAMS_KEY)
                playFragment?.apply {
                    setVideoInfo(mediaParams)
                    startPlay()
                }
            }

            override fun onFragmentAttached(
                @NonNull fm: FragmentManager,
                @NonNull f: Fragment,
                @NonNull context: Context
            ) {
                super.onFragmentAttached(fm, f, context)
                ZLog.i(TAG, "onFragmentAttached: " + f::class.java.simpleName)
            }

            override fun onFragmentPreCreated(
                fm: FragmentManager,
                f: Fragment,
                savedInstanceState: Bundle?
            ) {
                super.onFragmentPreCreated(fm, f, savedInstanceState)
                ZLog.i(TAG, "onFragmentPreCreated: " + f::class.java.simpleName)
            }

            override fun onFragmentCreated(fm: FragmentManager,f: Fragment,savedInstanceState: Bundle?) {
                super.onFragmentCreated(fm, f, savedInstanceState)
                ZLog.i(TAG, "onFragmentCreated: " + f::class.java.simpleName)
            }

            override fun onFragmentViewCreated(@NonNull fm: FragmentManager,@NonNull f: Fragment,
                @NonNull v: View,@Nullable savedInstanceState: Bundle?) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState)
                ZLog.i(TAG, "onFragmentViewCreated: " + f::class.java.simpleName)
            }

            override fun onFragmentStarted(@NonNull fm: FragmentManager, @NonNull f: Fragment) {
                super.onFragmentStarted(fm, f)
                ZLog.i(TAG, "onFragmentStarted: " + f::class.java.simpleName)
            }

            override fun onFragmentResumed(@NonNull fm: FragmentManager, @NonNull f: Fragment) {
                super.onFragmentResumed(fm, f)
                ZLog.i(TAG, "onFragmentResumed: " + f::class.java.simpleName)
            }

            override fun onFragmentPaused(@NonNull fm: FragmentManager, @NonNull f: Fragment) {
                super.onFragmentPaused(fm, f)
                ZLog.i(TAG, "onFragmentPaused: " + f::class.java.simpleName)
            }

            override fun onFragmentStopped(@NonNull fm: FragmentManager, @NonNull f: Fragment) {
                super.onFragmentStopped(fm, f)
                ZLog.i(TAG, "onFragmentStopped: " + f::class.java.simpleName)
            }

            override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
                super.onFragmentSaveInstanceState(fm, f, outState)
                ZLog.i(TAG, "onFragmentSaveInstanceState: " + f::class.java.simpleName)
            }

            override fun onFragmentViewDestroyed(@NonNull fm: FragmentManager, @NonNull f: Fragment) {
                super.onFragmentViewDestroyed(fm, f)
                ZLog.i(TAG, "onFragmentViewDestroyed: " + f::class.java.simpleName)
            }

            override fun onFragmentDestroyed(@NonNull fm: FragmentManager, @NonNull f: Fragment) {
                super.onFragmentDestroyed(fm, f)
                ZLog.i(TAG, "onFragmentDestroyed: " + f::class.java.simpleName)
            }

            override fun onFragmentDetached(@NonNull fm: FragmentManager, @NonNull f: Fragment) {
                super.onFragmentDetached(fm, f)
                ZLog.i(TAG, "onFragmentDetached: " + f::class.java.simpleName)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        AutoSizeConfig.getInstance().isUseDeviceSize = true
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initFragment()
        }
    }

    override val viewModel: BaseReactiveViewModel
        get() = TODO("Use MVVM mode.")

    override fun initRotation() {}

    private fun initFragment() {
        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        // 播放区域
        playFragment = fm.findFragmentByTag(TAG_VOD_PLAY) as PlayFragment?
        if (playFragment == null) {
            playFragment = PlayFragment.newInstance()
            fragmentTransaction.add(R.id.player_content, playFragment!!, TAG_LIVE_PLAY)
        }
        fragmentTransaction.show(playFragment!!)
        fragmentTransaction.commit()
        supportFragmentManager.registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks,true)
    }

    override fun onDestroy() {
        AutoSizeConfig.getInstance().isUseDeviceSize = false
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks)
        playFragment?.onDestroy()
        super.onDestroy()
    }

    override fun getContentView(): View? {
        fullPlayerPageBinding = FullPlayerPageBinding.inflate(LayoutInflater.from(this))
        return fullPlayerPageBinding.root
    }

    override fun initData(savedInstanceState: Bundle?) {
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