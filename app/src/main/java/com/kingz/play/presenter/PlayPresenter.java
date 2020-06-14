package com.kingz.play.presenter;

import android.media.TimedText;
import android.net.Uri;
import android.util.Log;
import android.widget.SeekBar;

import com.kingz.library.player.IPlayer;
import com.kingz.library.player.IPlayerEventsCallBack;
import com.kingz.library.player.exo.ExoPlayer;
import com.kingz.play.view.IPlayerView;

import static com.google.android.exoplayer2.Player.REPEAT_MODE_ALL;

/**
 * author：KingZ
 * date：2019/7/30
 * description：
 * <p>
 * 视频地址：http://www.zhiboo.net/
 * TODO 画面模式调整，
 */
public class PlayPresenter extends AbsBasePresenter implements IPlayerEventsCallBack {
    private IPlayerView playerView;
    private IPlayer mPlayer;
    public PlaySeekBarChangeListener seekBarChangeListener;
    private boolean isDraggingSeekBar; //是否正在拖动进度条


    public PlayPresenter(IPlayer player, IPlayerView playerView) {
        this.mPlayer = player;
        this.playerView = playerView;
        seekBarChangeListener = new PlaySeekBarChangeListener();
    }

    @Override
    public void onCreateView() {
        mPlayer.setPlayerView(playerView.getPlayView());
        mPlayer.setPlayerEventCallBack(this);
        //TODO 进行播放数据模型建立
        playTest();
    }

    private void playTest() {
        Uri testPlayUri = Uri.parse("https://vfx.mtime.cn/Video/2020/05/14/mp4/200514070325395613_1080.mp4");
        //嫦娥探月
//        Uri testPlayUri = Uri.parse("http://video.chinanews.com/flv/2019/04/23/400/111773_web.mp4");
//        Uri testPlayUri = Uri.parse("http://cctvtxyh5c.liveplay.myqcloud.com/wstv/dongfang_2/index.m3u8");
        mPlayer.setPlayURI(testPlayUri);
        if (mPlayer instanceof ExoPlayer) {
            ((ExoPlayer) mPlayer).setRepeatMode(REPEAT_MODE_ALL);
        }
        play();
    }

    /**
     * 切换播放状态
     */
    public void togglePlay(){
        if(mPlayer.isPlaying()){
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

    public boolean isPlayeing(){
        return mPlayer.isPlaying();
    }

    private void release() {
        mPlayer.release();
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
    public void onStop() {
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
    public void onPrepared() {
        Log.d(TAG, "onPrepared()");
        playerView.showPlayingView();
    }

    @Override
    public void onPlay() {

    }

    @Override
    public boolean onError(int what, int extra) {
        return false;
    }

    @Override
    public void onBufferStart() {

    }

    @Override
    public void onBufferEnd() {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onCompletion() {

    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public void onPlayerTimingUpdate() {
//        long position = mPlayer.getCurrentPosition();
//        long duration = mPlayer.getDuration();
        if (!isDraggingSeekBar) {
            //不是拖动中才自动更新进度
            playerView.updatePlayProgressView(false, -1);
        }

    }

    @Override
    public boolean onInfo(int what, int extra) {
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
