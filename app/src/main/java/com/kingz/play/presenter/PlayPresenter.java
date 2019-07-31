package com.kingz.play.presenter;

import android.media.TimedText;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.kingz.library.player.IMediaPlayer;
import com.kingz.library.player.exo.ExoMediaPlayer;
import com.kingz.play.view.IPlayerView;

import static com.google.android.exoplayer2.Player.REPEAT_MODE_ALL;

/**
 * author：KingZ
 * date：2019/7/30
 * description：
 * FIXME 这个presenter实现SeekBar的监听可否优化
 */
public class PlayPresenter implements IPlayPresenter,
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {
    private static final String TAG = PlayPresenter.class.getName();
    private IPlayerView playerView;
    private IMediaPlayer player;

    public PlayPresenter(IMediaPlayer player,IPlayerView playerView) {
        this.player = player;
        this.playerView = playerView;
    }

    @Override
    public void onCreateView() {
        player.setPlayerView(playerView.getPlayView());
        player.setPlayerEventCallBack(this);
//        Uri testPlayUri = Uri.parse("http://113.105.248.47/14/v/i/k/h/vikhmhifgwpksztpfxxcckpfnkxsbu/he.yinyuetai.com/AE3B0166F34C8148E6F94146DBC1BBCE.mp4");
        Uri testPlayUri = Uri.parse("http://183.60.197.33/8/w/w/k/e/wwkeazjkxmrhtvpmdfolzahahbtfua/hc.yinyuetai.com/A3B001588B7A43C2C89C0CD899FEFF76.mp4?sc=1d6a19222c007613&br=778&vid=2729951&aid=4539&area=US&vst=3");
        player.setPlayURI(testPlayUri);
        if(player instanceof ExoMediaPlayer){
            ((ExoMediaPlayer)player).setRepeatMode(REPEAT_MODE_ALL);
        }
        play();
    }

    public void play() {
        player.play();
        playerView.showPlayStateView();
    }

    public void pause() {
        player.pause();
        playerView.showPauseStateView();
    }

    public void release() {
        player.release();
    }

    private void seekTo(long progress) {
        if (progress < 0 || progress > player.getDuration()) {
            return;
        }
        player.seekTo(progress);
    }

    @Override
    public void onCreate() {

    }


    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {
        //TODO 进行其他业务处理 如播放记录的记录
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
        playerView.switchVisibleState();
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

    @Override
    public void onPrepared(IMediaPlayer player) {
        Log.d(TAG,"onPrepared()");
        playerView.showPlayingView();
    }

    @Override
    public void onPlay(IMediaPlayer player) {

    }

    @Override
    public boolean onError(IMediaPlayer player, int what, int extra) {
        return false;
    }

    @Override
    public void onBufferStart(IMediaPlayer player) {

    }

    @Override
    public void onBufferEnd(IMediaPlayer player) {

    }

    @Override
    public void onBufferingUpdate(IMediaPlayer player, int percent) {

    }

    @Override
    public void onCompletion(IMediaPlayer player) {

    }

    @Override
    public void onSeekComplete(IMediaPlayer player) {

    }

    @Override
    public void onProgressInSecond() {

    }

    @Override
    public boolean onInfo(IMediaPlayer player, int what, int extra) {
        return false;
    }

    @Override
    public void onTimedText(IMediaPlayer player, TimedText text) {

    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void onViewChanged(int format, int width, int height) {

    }

    @Override
    public void onViewDestroyed() {

    }
}
