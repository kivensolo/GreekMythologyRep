package com.zeke.play.wigets

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.kingz.module.common.base.IPresenter
import com.kingz.module.common.bean.MediaParams
import com.zeke.kangaroo.utils.VolumeUtils
import com.zeke.kangaroo.utils.ZLog
import com.zeke.module_player.R
import com.zeke.play.BasePlayPop
import com.zeke.play.MediaPlayTool
import com.zeke.play.PlayerGestureListener
import com.zeke.play.gesture.IGestureCallBack
import com.zeke.play.presenter.PlayPresenter
import com.zeke.play.view.IPlayerView
import com.zeke.play.view.controller.PlayerUiSwitcher
import me.jessyan.autosize.utils.ScreenUtils
import kotlin.math.abs
import kotlin.math.min

/**
 * author：ZekeWang
 * date：2021/6/5
 * description：
 * 具备播放的播放器UI组件抽象层
 */
abstract class BasePlayView @JvmOverloads constructor(
    mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0    // 注意这个attr的默认值,有的控件不一定是0
) : FrameLayout(mContext, attrs, defStyleAttr), IPlayerView {

    //视图切换控制器
    var mUiSwitcher: PlayerUiSwitcher? = null
        private set
    private var playPresenter: PlayPresenter? = null
    private var renderView: SurfaceView? = null
    private var mediaParams: MediaParams? = null
    private val basePlayPop: BasePlayPop? = null
    private var gestureDetector: GestureDetector? = null
    private var gestureDetectorLsr: PlayerGestureListener? = null

    init {
        ZLog.d("kingz init()")
        initGestureDetector()

        View.inflate(context, getLayoutId(), this)
        renderView = findViewById(R.id.surface_container)
//        TODO 外部设置播放器内核
        val mediaPlayer = MediaPlayTool.getInstance().mediaPlayerCore
        playPresenter = PlayPresenter(mediaPlayer, this)
        playPresenter?.onCreateView()

        mUiSwitcher = PlayerUiSwitcher(playPresenter, this).apply {
            setOnClickListener(this@BasePlayView)
            setOnSeekBarChangeListener(playPresenter?.seekBarChangeListener)
        }
    }

    //子类可复写播放控件试图，但是试图id需保持一致
    abstract fun getLayoutId(): Int

    private fun initGestureDetector() {
        gestureDetectorLsr = PlayerGestureListener(context, SurfaceGustureCallback())
        val screenSize = ScreenUtils.getScreenSize(context)
        gestureDetectorLsr?.setVideoWH(screenSize[0], screenSize[1])
        gestureDetector = GestureDetector(context, gestureDetectorLsr)
    }


    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        //当child被添加的时候
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setOnTouchListener{_, event ->
            if (mUiSwitcher?.isLocked == false) {
                //分发触摸事件给手势检测器
                gestureDetector?.onTouchEvent(event)
            }
            if (event.action == MotionEvent.ACTION_UP) {
                changeUIComponentsVisible()
            }
            true
        }
        ZLog.d("kingz onAttachedToWindow()")
    }

    /**
     * UI组件显示/隐藏切换
     */
    private fun changeUIComponentsVisible() {
        mUiSwitcher?.apply {
            if(isUiComponentsVisible){
                if(isSeekingByGesture){
                    val durationOffSet = seekingDurationByGesture
                    val currentPosition = playPresenter!!.currentPosition
                    val postion = currentPosition + durationOffSet
                    playPresenter?.apply {
                        this.seekTo(postion)
                        this.play()
                    }
                }
                setCompoentsVisible(false)
            }

            if(isLocked){
                switchVisibleState()
            }
        }
    }

    override fun onDetachedFromWindow() {
        ZLog.d("kingz onDetachedFromWindow()")
        basePlayPop?.dismiss()
        mUiSwitcher?.repostControllersDismissTask(false)
        playPresenter?.onDestroyView()
        super.onDetachedFromWindow()
    }

    // <editor-fold defaultstate="collapsed" desc="对外公开API">
    open fun setDataSource(params: MediaParams) {
        mediaParams = params
        //数据传递至presenter
        ZLog.d("kingz setDataSource  playPresenter=$playPresenter")
        playPresenter?.setPlayParams(mediaParams)
        mUiSwitcher?.setVideoTitle(mediaParams?.videoName)
    }

    open fun setScreenMode() {

    }

    open fun open() {
        playPresenter?.startPlay()
    }

    open fun play(){
        playPresenter?.play()
    }

    open fun stop() {
        playPresenter?.onStop()
    }
    // </editor-fold>

    /**
     * 切换播放器状态
     */
    private fun togglePlayState() {
        playPresenter?.togglePlay()
    }


    internal inner class SurfaceGustureCallback : IGestureCallBack {
        //seek达到结尾的保护时间 3s
        private val seekProtectTime = 3 * 1000L

        override fun onGestureLeftTB(ratio: Float) { //            ZLog.d(TAG,"onGesture LeftTB ratio=" + ratio);
//            ScreenDisplayUtils.setScreenBrightness(activity, ratio)
//            val localWindow: Window = activity.getWindow()
//            val localLayoutParams = localWindow.attributes
//            localLayoutParams.screenBrightness = ratio
//            localWindow.attributes = localLayoutParams
            mUiSwitcher?.updateBrightness(ratio)
        }

        override fun onGestureRightTB(ratio: Float) { //            ZLog.d(TAG,"onGesture RightTB ratio=" + ratio);
            VolumeUtils.setStreamMusicVolume(context, ratio)
            mUiSwitcher?.updateVolume(ratio)
        }

        override fun onGestureUpdateVideoTime(duration: Long) {
            if (playPresenter!!.isPlayeing) {
                playPresenter?.pause()
            }
            val time = limitSeekingTime(duration)
            mUiSwitcher?.updateSeekTimePreView(time)
        }

        override fun onGestureSingleClick() {
            playPresenter!!.onViewClick()
        }

        override fun onGestureDoubleClick() {
            mUiSwitcher?.setCenterPauseIconVisible(playPresenter!!.isPlayeing)
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

    override fun dismissPop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun switchVisibleState() {
        mUiSwitcher?.switchVisibleState()
    }

    override fun showPauseStateView() {
        mUiSwitcher?.showPauseStateView()
    }

    override fun showFlowTipsView() {
    }

    override fun showPlayLoadingTips(tips: String?) {
        dismissPop()
        mUiSwitcher?.showLoadingView(tips)
    }

    override fun showEmpty(listener: OnClickListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showPlayStateView() {
        mUiSwitcher?.showPlayStateView()
    }

    override fun showMessage(tips: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 切换屏幕模式
     * @param isFull 是否全屏
     * @return 是否成功切换
     */
    private fun switchScreenMode(isFull: Boolean): Boolean {
        //有activity的情况
//        if (mActivity != null && !mUiSwitcher!!.isLocked) {
//            val autoRotation = Settings.System.getInt(mActivity!!.contentResolver,
//                Settings.System.ACCELEROMETER_ROTATION, 0
//            )
//            val orientation = if (isFull) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//            else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//            mActivity?.requestedOrientation = orientation
//            playView?.postDelayed({
//                //2秒后，横竖屏在由重力感应决定
//                if (isShown && autoRotation == 1 && !mUiSwitcher!!.isLocked) {
//                    mActivity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
//                }
//            }, PlayFragment.ORIENTATION_CHANGE_DELAY_MS)
//            mUiSwitcher?.refreshViewState()
//            return true
//        }
        // TODO 是否做大小窗切换
        return false
    }

    override fun showLoading() {}

    override fun showPlayBufferTips() {
    }

    override fun hideLoading() {}

    override fun updateTitleView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showPlayingView() {
        mUiSwitcher?.showPlayingView()
    }

    override fun showPlayErrorTips(tips: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(v: View) {
        repostControllersDismissTask(true)
        when (v.id) {
            R.id.back_tv,
            R.id.cover_back -> {
                //TODO back logic
//                backPress()
            }
            R.id.fullscreen_icon,
            R.id.fullscreen_icon_mask -> {
                switchScreenMode(true)
            }
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
            R.id.play_next -> {
            }
            R.id.player_setting -> {
                mUiSwitcher?.makeSettingViewVisible(true)
            }
            R.id.play_pause_center->{
                mUiSwitcher?.setCenterPauseIconVisible(false)
                playPresenter?.play()
            }
        }
        basePlayPop?.setOnDismissListener { switchVisibleState() }

    }

    override fun getPlayView(): View? = renderView

    override fun updatePlayProgressView(isDrag: Boolean, postion: Int) {
        mUiSwitcher?.updatePlayProgressView(isDrag, postion)
    }

    override fun repostControllersDismissTask(enable: Boolean) {
        mUiSwitcher?.repostControllersDismissTask(enable)
    }

    override fun showError(listener: OnClickListener?) {}

    override fun setPresenter(presenter: IPresenter) {
        playPresenter = presenter as PlayPresenter
    }

    override fun showPlayCompleteTips(tips: String?) {
        dismissPop()
        mUiSwitcher?.showCompleteView(tips)
    }

    override fun dismissPlayBufferTips() {}

    override val isViewVisible: Boolean = isVisible
}