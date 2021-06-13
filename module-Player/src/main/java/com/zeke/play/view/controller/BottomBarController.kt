package com.zeke.play.view.controller

import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.kingz.utils.ktx.getSystemTime
import com.module.tools.ViewUtils
import com.zeke.kangaroo.utils.TimeUtils
import com.zeke.module_player.R
import com.zeke.play.constants.PlayMode

/**
 * author：KingZ
 * date：2019/7/31
 * description：底部工具栏Controller
 * //TODO 直播时不能拖动
 *
 * 当前底部控制器，是包含了多种UI排列的效果，全部组件参见
 * player_view_controller_basic_new.xml
 */
class BottomBarController(view: View) : BaseController() {
    private var liveTipsView: TextView? = null
    private var playPauseChk: CheckBox? = null
    private var nextImg: ImageView? = null
    private val seekBar: SeekBar
    private val currentTimeView: TextView
    private val durationTimeView: TextView
    private var qualityTxt: TextView? = null
    private var fullScreenImg: ImageView? = null
    private var playMode = PlayMode.VOD
    fun setPosition(position: Long) {
        if (isLiveMode) {
            seekBar.progress = 1
        } else {
            seekBar.progress = position.toInt()
            currentTimeView.text = TimeUtils.generateTime(position)
        }
    }

    fun setDuration(duration: Long) {
        if (isLiveMode) {
            durationTimeView.text = getSystemTime()
            seekBar.max = 1
        } else {
            durationTimeView.text = TimeUtils.generateTime(duration)
            seekBar.max = duration.toInt()
        }
    }

    fun onProgressUpdate(position: Long,duration: Long){
        setPosition(position)
        setDuration(duration)
        //TODO  增加功能：隐藏时，是否在播放器底部显示进度条
    }

    fun setPlayMode(@PlayMode mode: String) {
        playMode = mode
    }

    fun showPlayState() {
        playPauseChk?.isChecked = true
    }

    fun showPauseState() {
        playPauseChk?.isChecked = false
    }

    @Deprecated("废弃")
    val isInPlayState: Boolean
        get() = playPauseChk?.isChecked?:false

    fun setOnSeekBarChangeListener(listener: SeekBar.OnSeekBarChangeListener?) {
        seekBar.setOnSeekBarChangeListener(listener)
    }

    fun setOnClickListener(listener: View.OnClickListener?) {
        nextImg?.setOnClickListener(listener)
        qualityTxt?.setOnClickListener(listener)
        fullScreenImg?.setOnClickListener(listener)
        liveTipsView?.setOnClickListener(listener)
        playPauseChk?.setOnClickListener(listener)
    }

    override fun show() { //        qualityTxt.setVisibility(View.GONE);
        if (isLiveMode) {
            liveTipsView?.visibility = View.VISIBLE
            currentTimeView.visibility = View.GONE
        } else {
            liveTipsView?.visibility = View.GONE
            currentTimeView.visibility = View.VISIBLE
        }
        nextImg?.visibility = View.GONE
        if (ViewUtils.isLandScape(rootView)) {
            fullScreenImg?.visibility = View.GONE
        } else {
            fullScreenImg?.visibility = View.VISIBLE
        }
        seekBar.visibility = View.VISIBLE
        durationTimeView.visibility = View.VISIBLE
        playPauseChk?.visibility = View.VISIBLE
        rootView.visibility = View.VISIBLE
    }

    private val isLiveMode: Boolean
        private get() = TextUtils.equals(playMode, PlayMode.LIVE)

    init {
        rootView = view.findViewById(R.id.player_bar_bottom)
        liveTipsView = rootView.findViewById(R.id.live_flag)
        playPauseChk = rootView.findViewById(R.id.play_pause)
        nextImg = rootView.findViewById(R.id.play_next)
        seekBar = rootView.findViewById(R.id.player_seekbar_progress)
        currentTimeView = rootView.findViewById(R.id.player_txt_current_time)
        durationTimeView = rootView.findViewById(R.id.player_txt_all_time)
        qualityTxt = rootView.findViewById(R.id.tv_quality)
        fullScreenImg = rootView.findViewById(R.id.fullscreen_icon)

    }
}