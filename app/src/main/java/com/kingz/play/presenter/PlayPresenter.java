package com.kingz.play.presenter;

import android.media.TimedText;
import android.net.Uri;
import android.util.Log;
import android.widget.SeekBar;

import com.kingz.library.player.IMediaPlayer;
import com.kingz.library.player.IMediaPlayerCallBack;
import com.kingz.library.player.exo.ExoMediaPlayer;
import com.kingz.play.view.IPlayerView;

import static com.google.android.exoplayer2.Player.REPEAT_MODE_ALL;

/**
 * author：KingZ
 * date：2019/7/30
 * description：
 * <p>
 * 视频地址：http://www.zhiboo.net/
 * TODO 画面模式调整，
 * TODO 全屏的播控画面亮度
 * TODO 全屏的播控声音
 */
public class PlayPresenter extends AbsBasePresenter implements IMediaPlayerCallBack {
    private IPlayerView playerView;
    private IMediaPlayer mPlayer;
    public PlaySeekBarChangeListener seekBarChangeListener;
    private boolean isDraggingSeekBar; //是否正在拖动进度条


    public PlayPresenter(IMediaPlayer player, IPlayerView playerView) {
        this.mPlayer = player;
        this.playerView = playerView;
        seekBarChangeListener = new PlaySeekBarChangeListener();
    }

    @Override
    public void onCreateView() {
        mPlayer.setPlayerView(playerView.getPlayView());
        mPlayer.setPlayerEventCallBack(this);
        playTest();
    }

    private void playTest() {
        Uri testPlayUri = Uri.parse("http://video.chinanews.com/flv/2019/04/23/400/111773_web.mp4");
        //Uri testPlayUri = Uri.parse("http://183.222.102.65/cache/hc.yinyuetai.com/uploads/videos/common/C1C6015E95755D79B2A706CC75BB1809.mp4?sc=022fceb4b8b24fb7&br=780&vid=3045490&aid=42545&area=US&vst=4&ich_args2=394-31214602005373_4274ad8eeb58c86e3763a95d241faabd_10307403_9c896228d2cbf1d69f3a518939a83798_6af147aa7f96aa8723f9c43d081fec81");
//        Uri testPlayUri = Uri.parse("http://cctvtxyh5c.liveplay.myqcloud.com/wstv/dongfang_2/index.m3u8");
        mPlayer.setPlayURI(testPlayUri);
        if (mPlayer instanceof ExoMediaPlayer) {
            ((ExoMediaPlayer) mPlayer).setRepeatMode(REPEAT_MODE_ALL);
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

    private void pause() {
        mPlayer.pause();
        playerView.showPauseStateView();
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
    public void onPause() {
        //TODO 进行其他业务处理 如播放记录的记录
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
    public void onPrepared(IMediaPlayer player) {
        Log.d(TAG, "onPrepared()");
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
    public void onPlayerTimingUpdate() {
//        long position = mPlayer.getCurrentPosition();
//        long duration = mPlayer.getDuration();
        if (!isDraggingSeekBar) {
            //不是拖动中才自动更新进度
            playerView.updatePlayProgressView(false, -1);
        }

    }

    @Override
    public boolean onInfo(IMediaPlayer player, int what, int extra) {
        Log.d(TAG, "onInfo what=" + what + "; extra=" + extra);
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
