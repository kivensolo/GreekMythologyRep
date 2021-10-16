package com.zeke.play.fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import com.kingz.module.common.base.BaseActivity
import com.kingz.module.common.base.BaseFragment
import com.kingz.module.common.base.IPresenter
import com.kingz.module.common.bean.MediaParams
import com.zeke.kangaroo.utils.ScreenDisplayUtils
import com.zeke.kangaroo.utils.VolumeUtils
import com.zeke.module_player.R
import com.zeke.play.BasePlayPop
import com.zeke.play.MediaPlayTool
import com.zeke.play.PlayerGestureListener
import com.zeke.play.gesture.IGestureCallBack
import com.zeke.play.presenter.PlayPresenter
import com.zeke.play.view.IPlayerView
import com.zeke.play.view.controller.PlayerUiSwitcher
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.internal.CustomAdapt
import me.jessyan.autosize.utils.ScreenUtils
import kotlin.math.abs
import kotlin.math.min

/**
 * author：KingZ
 * date：2019/7/30
 * description：播放器Fragment
 * 支持正常的播控操作
 */
class PlayFragment : BaseFragment(), IPlayerView, CustomAdapt {
    private var mediaParams: MediaParams? = null
    private val basePlayPop: BasePlayPop? = null
    //UI交互控制器
    var mUiSwitcher: PlayerUiSwitcher? = null
        private set

    /**
     * 播放行为控制器
     * 注意：在onFragmentViewCreated才会初始化
     */
    private var playPresenter: PlayPresenter? = null
    private var playView: SurfaceView? = null
    //手势控制
    private var gestureDetector: GestureDetector? = null
    private var gestureDetectorLsr: PlayerGestureListener? = null

    companion object {
        private const val ORIENTATION_CHANGE_DELAY_MS = 2000L

        fun newInstance(): PlayFragment {
            AutoSizeConfig.getInstance().isCustomFragment = true
            return PlayFragment()
        }
    }

    init {
        initGestureDetector()
    }

    private fun initGestureDetector() {
        gestureDetectorLsr = PlayerGestureListener(context, SurfaceGustureCallback())
        val screenSize = ScreenUtils.getScreenSize(context)
        gestureDetectorLsr?.setVideoWH(screenSize[0], screenSize[1])
        gestureDetector = GestureDetector(context, gestureDetectorLsr)
    }

    override fun setPresenter(presenter: IPresenter) {
        playPresenter = presenter as PlayPresenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mediaParams = arguments!!.getParcelable(MediaParams.PARAMS_KEY)
        }
    }

    override fun getLayoutId(): Int = R.layout.player_view_controller_basic_new

    @SuppressLint("ClickableViewAccessibility")
    override fun onFragmentRenderIsRender() {
        playView = rootView?.findViewById(R.id.surface_container)
        //playView.setOnClickListener(this);
        playView?.setOnTouchListener { _, event ->
            if (mUiSwitcher?.isLocked == false) {
                gestureDetector?.onTouchEvent(event)
            }
            if (event.action == MotionEvent.ACTION_UP) {
                if (mUiSwitcher!!.isUiComponentsVisible) {
                    if (mUiSwitcher!!.isSeekingByGesture) {
                        val durationOffSet = mUiSwitcher!!.seekingDurationByGesture
                        val currentPosition = playPresenter!!.currentPosition
                        val postion = currentPosition + durationOffSet
                        playPresenter?.apply {
                            this.seekTo(postion)
                            this.play()
                        }
                    }
                    mUiSwitcher?.setCompoentsVisible(false)
                }
                if (mUiSwitcher!!.isLocked) {
                    mUiSwitcher?.switchVisibleState()
                }
            }
            true
        }
        val mediaPlayer = MediaPlayTool.getInstance().mediaPlayerCore
        playPresenter = PlayPresenter(mediaPlayer, this)
        // TODO 根据类型初始化不同的UISwitcher
        mUiSwitcher = PlayerUiSwitcher(playPresenter, rootView).apply {
            setOnClickListener(this@PlayFragment)
            setVideoTitle(mediaParams?.videoName)
            setOnSeekBarChangeListener(playPresenter?.seekBarChangeListener)
        }
        playPresenter?.onCreateView()
    }

    override fun onClick(v: View) {
        repostControllersDismissTask(true)
        when(v.id){
            R.id.back_tv,
            R.id.cover_back -> { activity?.onBackPressed() }
            R.id.fullscreen_icon,
            R.id.fullscreen_icon_mask -> { switchScreenMode(true) }
            R.id.tv_quality,
            R.id.tv_quality_cover -> { //清晰度弹窗
//                basePlayPop = new QualityPop(getContext(), mediaParams, playPresenter.playEventManager);
//                basePlayPop.showAtLocation(getView(), Gravity.CENTER, 0, 0);
//                playerUiSwitcher.switchVisibleState();
            }
            R.id.play_pause -> {
                togglePlayState()
            }
            R.id.play_flow_tips -> {
                playPresenter?.play()
            }
            R.id.play_next -> {}
            R.id.player_setting -> {
                mUiSwitcher?.makeSettingViewVisible(true)
            }
        }
        basePlayPop?.setOnDismissListener { switchVisibleState() }
    }

    /**
     * 设置影片数据
     */
    fun setVideoInfo(mediaParams :MediaParams){
        this.mediaParams = mediaParams
        val bundle = Bundle()
        bundle.putParcelable(MediaParams.PARAMS_KEY, mediaParams)
        arguments = bundle

        //数据传递至presenter
        playPresenter?.setPlayParams(mediaParams)
    }

    fun startPlay(){
        playPresenter?.startPlay()
    }

    override fun onPause() {
        super.onPause()
        playPresenter?.onPause()
    }

    override fun onDestroy() {
        basePlayPop?.dismiss()
        mUiSwitcher?.repostControllersDismissTask(false)
        playPresenter?.onDestroyView()
        super.onDestroy()
    }

    override fun onBackPressed(): Boolean {
        if (mUiSwitcher!!.isSettingViewShow) {
            mUiSwitcher?.makeSettingViewVisible(false)
            return true
        }
        switchScreenMode(false)
        return false
    }

    /**
     * 切换播放器状态
     */
    private fun togglePlayState() {
        playPresenter?.togglePlay()
    }

    /**
     * 切换屏幕模式
     * @param isFull 是否全屏
     * @return 是否成功切换
     */
    private fun switchScreenMode(isFull: Boolean): Boolean {
        if (mActivity != null && !mUiSwitcher!!.isLocked) {
            val autoRotation = Settings.System.getInt(mActivity!!.contentResolver,
                Settings.System.ACCELEROMETER_ROTATION, 0
            )
            val orientation = if (isFull) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            mActivity?.requestedOrientation = orientation
            playView?.postDelayed({
                //2秒后，横竖屏在由重力感应决定
                if (isViewVisible && autoRotation == 1 && !mUiSwitcher!!.isLocked) {
                    mActivity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
            }, ORIENTATION_CHANGE_DELAY_MS)
            mUiSwitcher?.refreshViewState()
            return true
        }
        return false
    }

    override fun showLoading() {}
    override fun hideLoading() {}
    override fun showError(listener: View.OnClickListener?) {}
    override fun showEmpty(listener: View.OnClickListener?) {}
    override val isViewVisible: Boolean
        get() = activity != null && (activity as BaseActivity?)!!.isActivityShow && isViewVisible

    override fun showMessage(tips: String?) {}
    override fun getPlayView(): View? = playView

    override fun showPlayLoadingTips(tips: String) {
        dismissPop()
        mUiSwitcher?.showLoadingView(tips)
    }

    override fun showPlayingView() {
        mUiSwitcher?.showPlayingView()
    }

    override fun showPlayBufferTips() {}
    override fun dismissPlayBufferTips() {}
    override fun showPlayCompleteTips(tips: String) {
        dismissPop()
        mUiSwitcher?.showCompleteView(tips)
    }

    override fun showPlayErrorTips(tips: String) {
        dismissPop()
        mUiSwitcher?.showErrorView(tips)
    }

    override fun updateTitleView() {}
    override fun showPlayStateView() {
        mUiSwitcher?.showPlayStateView()
    }

    override fun showPauseStateView() {
        mUiSwitcher?.showPauseStateView()
    }

    override fun showFlowTipsView() {}
    override fun switchVisibleState() {
        mUiSwitcher?.switchVisibleState()
    }

    //TODO 更新进度条
    override fun updatePlayProgressView(isDrag: Boolean, postion: Long) {
        mUiSwitcher?.updatePlayProgressView(isDrag, postion)
    }

    override fun repostControllersDismissTask(enable: Boolean) {
        mUiSwitcher?.repostControllersDismissTask(enable)
    }

    override fun dismissPop() {
        basePlayPop?.dismiss()
    }

    override fun isBaseOnWidth(): Boolean {
        return false
    }

    override fun getSizeInDp(): Float {
        return 360F
    }

    internal inner class SurfaceGustureCallback : IGestureCallBack {
        //seek达到结尾的保护时间 3s
        private val seekProtectTime = 3 * 1000L

        override fun onGestureLeftTB(ratio: Float) { //            ZLog.d(TAG,"onGesture LeftTB ratio=" + ratio);
            ScreenDisplayUtils.setScreenBrightness(activity, ratio)
            mUiSwitcher?.updateBrightness(ratio)
        }

        override fun onGestureRightTB(ratio: Float) { //            ZLog.d(TAG,"onGesture RightTB ratio=" + ratio);
            VolumeUtils.setStreamMusicVolume(context, ratio)
            mUiSwitcher?.updateVolume(ratio)
        }

        override fun onGestureUpdateVideoTime(duration: Long) {
            if (playPresenter!!.isPlaying) {
                playPresenter?.pause()
            }
            val time = limitSeekingTime(duration)
            mUiSwitcher?.updateSeekTimePreView(time)
        }

        override fun onGestureSingleClick() {
            playPresenter!!.onViewClick()
        }

        override fun onGestureDoubleClick() {
            togglePlayState()
        }

        override fun onGestureDown() {}
        /**
         * 限制seek的有效时长
         * @param duration 手势拖动的时长
         * @return 能前后seek得有效时长
         */
        private fun limitSeekingTime(duration: Long): Long {
            val time: Long
            time = if (duration <= 0) { //快退
                val currentPosition = playPresenter!!.currentPosition
                if (currentPosition <= abs(duration)) {
                    -currentPosition
                } else {
                    duration
                }
            } else { //快进
                val currentPosition = playPresenter!!.currentPosition
                val totalDuration = playPresenter!!.duration
                val left = totalDuration - currentPosition - seekProtectTime
                min(duration, left)
            }
            return time
        }
    }
}