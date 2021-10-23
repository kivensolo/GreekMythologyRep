package com.zeke.play.presenter;

import android.media.TimedText;
import android.net.Uri;
import android.util.Log;
import android.widget.SeekBar;

import com.google.android.exoplayer2.Player;
import com.kingz.library.player.IPlayer;
import com.kingz.library.player.IPlayerEventsListener;
import com.kingz.library.player.exo.ExoPlayer;
import com.kingz.module.common.bean.MediaParams;
import com.zeke.kangaroo.zlog.ZLog;
import com.zeke.play.view.IPlayerView;

import org.jetbrains.annotations.NotNull;

/**
 * author：KingZ
 * date：2019/7/30
 * description：
 * <p>
 * 视频地址：http://www.zhiboo.net/
 * TODO 画面模式调整，
 */
public class PlayPresenter extends AbsBasePresenter implements IPlayerEventsListener {
    private IPlayerView playerView;
    private IPlayer mPlayer;
    private MediaParams mPlayParams;
    public PlaySeekBarChangeListener seekBarChangeListener;
    private boolean isDraggingSeekBar; //是否正在拖动进度条

    public static final int ERROR_PLAY_PARAMS = 0x0001;

    public PlayPresenter(IPlayer player, IPlayerView playerView) {
        ZLog.d("init play presenter. " + player.getClass().getSimpleName());
        this.mPlayer = player;
        this.playerView = playerView;
        seekBarChangeListener = new PlaySeekBarChangeListener();
    }

    public void changePlayerKernel(IPlayer player){
        //TODO 判断是否相同
        this.mPlayer = player;
    }

    public void setPlayParams(MediaParams params){
        mPlayParams = params;
    }

    @Override
    public void onCreateView() {
        mPlayer.initRenderView(playerView.getPlayView());
        mPlayer.setPlayerEventListener(this);
    }

    /**
     * 解码以下视频花屏
     * http://116.77.67.2:5000/nn_live/nn_x64/aWQ9WERTU1NFSEQmdXJsX2MxPTc1NmU2NDY1NjY2OTZlNjU2NDAwJm5kbmk9MTE2Ljc3LjY3LjI6NTAwMCZubl9haz0wMTE4MDYzMmZiYTBlYzRiNmE0ODA4N2U3NWUyNThlYjA5Jm5waXBzPTE3Mi4yNy4xNy42Njo1MTAwJm5jbXNpZD0xMDAzMDIwMSZuZ3M9NjE2/OTFkODQwMDA1ZDc4MWU2N2E5MjA1YTgzYWUxMWYmbm5kPXRlc3QmbnNkPXR3bmV0Lmd1YW5nZG9uZy5zaGVuc2hhbiZuZnQ9dHMybTN1OCZubl91c2VyX2lkPWxpdWppbnRhbyZuZHQ9c3RiJm5kaT04VDIwMTRBMjk3RkFBNzc1Jm5kdj0xLjAuMC5TVEIuRlRUSC5U/T1VDSC5PVFRfU0tXMDEuUmVsZWFzZSZuc3Q9aXB0diZuY2E9JTI2bmNvaSUzZFNhbmRTdG9uZV9TM19saXZlMTAlMjZubl9jcCUzZHR3JTI2bmN2MmklM2RjZG52Ml9saXZlXzEwOSZuYWw9MDE4NTFkNjk2MTA2MDc5OGY3ZjMwMzAzNmY5OWFlNjM0NmM3NTk3MGZk/ZmEyYg,,/XDSSSEHD.m3u8
     */
    public void startPlay() {
        if(mPlayParams == null){
            ZLog.e("No play source.Please confirm play params!!!");
            onError(mPlayer, ERROR_PLAY_PARAMS, -1);
            return;
        }
        Uri playUri = Uri.parse(mPlayParams.getVideoUrl());
        ZLog.d("playUrl = " + playUri.toString());
        mPlayer.setDataSource(playUri);
        if (mPlayer instanceof ExoPlayer) {
            ((ExoPlayer) mPlayer).setRepeatMode(Player.REPEAT_MODE_ALL);
        }
        play();
    }

    /**
     * 切换播放状态
     */
    public void togglePlay(){
        if(isPlaying()){
            pause();
        }else{
            play();
        }
    }

    public void play() {
        mPlayer.play();
        playerView.showPlayStateView();
    }

    public void pause() {
        mPlayer.pause();
        playerView.showPauseStateView();
    }

    public boolean isPlaying(){
        return mPlayer.isPlaying();
    }

    private void release() {
        mPlayer.destory();
    }

    public void reset(){
        mPlayer.pause();
        mPlayer.destory();
    }

    public void seekTo(long progress) {
        if (progress < 0 || progress > mPlayer.getDuration()) {
            return;
        }
        mPlayer.seekTo(progress);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onPause() {  //进行其他业务处理 如播放记录的记录
    }

    @Override
    public void onStop() {//为啥onStop是Pause???
        mPlayer.pause();
    }

    @Override
    public void onDestroyView() {
        release();
    }

    @Override
    public void onDestroy() {
    }

    public void onViewClick() {
        playerView.switchVisibleState();
    }

    @Override
    public void onPrepared(IPlayer player) {
        Log.d(TAG, "onPrepared()");
        playerView.showPlayingView();
    }

    @Override
    public boolean onError(IPlayer player, int what, int extra) {
        ZLog.d(TAG,"onError: what="+what+",extra="+extra);
        return false;
    }

    @Override
    public void onCompletion(IPlayer player) {

    }

    @Override
    public void onSeekComplete(IPlayer player) {

    }

    @Override
    public void onPlayerTimingUpdate(IPlayer player,long position) {
//        ZLog.d("onPlayerTimingUpdate position=" + position);
        playerView.updatePlayProgressView(isDraggingSeekBar, position);
    }

    @Override
    public boolean onInfo(IPlayer player,int what, int extra) {
        Log.d(TAG, "onInfo what=" + what + "; extra=" + extra);
        return false;
    }

    @Override
    public void onTimedText(TimedText text) {

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

    public long getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    public long getDuration() {
        return mPlayer.getDuration();
    }

    public String getPlayMode(){
        return mPlayParams.getVideoType();
    }

    @Override
    public void onPrepareTimeout(@NotNull IPlayer xmp) {

    }

    @Override
    public void onBuffering(@NotNull IPlayer xmp, boolean buffering, float percentage) {

    }

    @Override
    public void onBufferTimeout(@NotNull IPlayer xmp) {

    }

    @Override
    public void onSeekComplete(@NotNull IPlayer xmp, long pos) {

    }

    @Override
    public void onVideoSizeChanged(@NotNull IPlayer player, int mVideoWidth, int mVideoHeight) {

    }

    @Override
    public void onVideoFirstFrameShow(@NotNull IPlayer player) {

    }

    class PlaySeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                playerView.updatePlayProgressView(true, progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            playerView.repostControllersDismissTask(false);
            isDraggingSeekBar = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            playerView.updatePlayProgressView(true, progress);
            seekTo(progress);
            isDraggingSeekBar = false;
            playerView.repostControllersDismissTask(true);
        }
    }
}
