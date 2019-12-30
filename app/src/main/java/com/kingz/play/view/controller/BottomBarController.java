package com.kingz.play.view.controller;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.module.tools.ViewUtils;
import com.zeke.kangaroo.utils.TimeUtils;

/**
 * author：KingZ
 * date：2019/7/31
 * description：底部工具栏Controller
 */
public class BottomBarController extends BaseController{
    private TextView liveTv;
    private CheckBox playPauseChk;
    private ImageView nextImg;
    private SeekBar seekBar;
    private TextView currentTxt;
    private TextView durationTxt;
    private TextView qualityTxt;

    private ImageView fullScreenImg;

    public BottomBarController(View view) {
        rootView = view.findViewById(R.id.player_bar_bottom);
        liveTv = rootView.findViewById(R.id.live_flag);
        playPauseChk = rootView.findViewById(R.id.play_pause);
        nextImg = rootView.findViewById(R.id.play_next);
        seekBar = rootView.findViewById(R.id.player_seekbar_progress);
        currentTxt = rootView.findViewById(R.id.player_txt_current_time);
        durationTxt = rootView.findViewById(R.id.player_txt_all_time);
        qualityTxt = rootView.findViewById(R.id.tv_quality);
        fullScreenImg = rootView.findViewById(R.id.fullscreen_icon);
    }

    public void setPosition(long position) {
        seekBar.setProgress((int) position);
        currentTxt.setText(TimeUtils.generateTime(position));
    }

    public void setDuration(long duration) {
        durationTxt.setText(TimeUtils.generateTime(duration));
        seekBar.setMax((int) duration);
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
        liveTv.setOnClickListener(listener);
        playPauseChk.setOnClickListener(listener);
    }

    @Override
    public void show() {
//        qualityTxt.setVisibility(View.GONE);
        liveTv.setVisibility(View.GONE);
        nextImg.setVisibility(View.GONE);
        if(ViewUtils.isLandScape(rootView)){
            fullScreenImg.setVisibility(View.GONE);
        }else{
            fullScreenImg.setVisibility(View.VISIBLE);
        }

        seekBar.setVisibility(View.VISIBLE);
        durationTxt.setVisibility(View.VISIBLE);
        currentTxt.setVisibility(View.VISIBLE);
        playPauseChk.setVisibility(View.VISIBLE);
        rootView.setVisibility(View.VISIBLE);
    }
}
