package com.zeke.play.view.controller;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kingz.utils.ktx.TimeUtilsKt;
import com.module.tools.ViewUtils;
import com.zeke.kangaroo.utils.TimeUtils;
import com.zeke.module_player.R;
import com.zeke.play.constants.PlayMode;

/**
 * author：KingZ
 * date：2019/7/31
 * description：底部工具栏Controller
 * //TODO 直播时不能拖动
 */
public class BottomBarController extends BaseController {
    private TextView liveTipsView;
    private CheckBox playPauseChk;
    private ImageView nextImg;
    private SeekBar seekBar;
    private TextView currentTimeView;
    private TextView durationTimeView;
    private TextView qualityTxt;

    private ImageView fullScreenImg;

    private String playMode = PlayMode.VOD;

    public BottomBarController(View view) {
        rootView = view.findViewById(R.id.player_bar_bottom);
        liveTipsView = rootView.findViewById(R.id.live_flag);
        playPauseChk = rootView.findViewById(R.id.play_pause);
        nextImg = rootView.findViewById(R.id.play_next);
        seekBar = rootView.findViewById(R.id.player_seekbar_progress);
        currentTimeView = rootView.findViewById(R.id.player_txt_current_time);
        durationTimeView = rootView.findViewById(R.id.player_txt_all_time);
        qualityTxt = rootView.findViewById(R.id.tv_quality);
        fullScreenImg = rootView.findViewById(R.id.fullscreen_icon);
    }

    public void setPosition(long position) {
        if(isLiveMode()){
            seekBar.setProgress(1);
        }else{
            seekBar.setProgress((int) position);
            currentTimeView.setText(TimeUtils.generateTime(position));
        }
    }

    public void setDuration(long duration) {
        if(isLiveMode()){
            durationTimeView.setText(TimeUtilsKt.getSystemTime());
            seekBar.setMax(1);
        }else{
            durationTimeView.setText(TimeUtils.generateTime(duration));
            seekBar.setMax((int) duration);
        }
    }

    public void setPlayMode(@PlayMode String mode) {
        playMode = mode;
    }

    public void showPlayState() {
        playPauseChk.setChecked(true);
    }

    public void showPauseState() {
        playPauseChk.setChecked(false);
    }

    public boolean isInPlayState() {
        return !playPauseChk.isChecked();
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener listener) {
        seekBar.setOnSeekBarChangeListener(listener);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        nextImg.setOnClickListener(listener);
        qualityTxt.setOnClickListener(listener);
        fullScreenImg.setOnClickListener(listener);
        liveTipsView.setOnClickListener(listener);
        playPauseChk.setOnClickListener(listener);
    }

    @Override
    public void show() {
//        qualityTxt.setVisibility(View.GONE);
        if(isLiveMode()){
            liveTipsView.setVisibility(View.VISIBLE);
            currentTimeView.setVisibility(View.GONE);
        } else {
            liveTipsView.setVisibility(View.GONE);
            currentTimeView.setVisibility(View.VISIBLE);
        }
        nextImg.setVisibility(View.GONE);
        if(ViewUtils.isLandScape(rootView)){
            fullScreenImg.setVisibility(View.GONE);
        }else{
            fullScreenImg.setVisibility(View.VISIBLE);
        }
        seekBar.setVisibility(View.VISIBLE);
        durationTimeView.setVisibility(View.VISIBLE);
        playPauseChk.setVisibility(View.VISIBLE);
        rootView.setVisibility(View.VISIBLE);
    }

    private boolean isLiveMode(){
        return TextUtils.equals(playMode, PlayMode.LIVE);
    }
}
