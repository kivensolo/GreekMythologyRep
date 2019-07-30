package com.kingz.play.presenter;

import android.view.View;
import android.widget.SeekBar;

import com.kingz.play.view.IPlayerView;

/**
 * author：KingZ
 * date：2019/7/30
 * description：
 */
public class PlayPresenter
        implements IPlayPresenter, View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {
    private IPlayerView playerView;


    public void play() {
        //TODO 进行播放器的播放
        playerView.showPlayStateView();
    }

    public void pause() {
        //TODO 进行播放器的暂停
        playerView.showPauseStateView();
    }

    public void release() {
        //TODO 进行播放器的release
    }

    private void seekTo(long progress) {
        //TODO 进行播放器的 seekTo

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onCreateView() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
