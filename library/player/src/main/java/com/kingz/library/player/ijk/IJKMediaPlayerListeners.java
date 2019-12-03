package com.kingz.library.player.ijk;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * author：KingZ
 * date：2019/7/30
 * description：IJK 播放器状态监听接口 <br>
 */
public interface IJKMediaPlayerListeners extends
        IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnInfoListener,
        IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnTimedTextListener,
        IMediaPlayer.OnSeekCompleteListener,
        IMediaPlayer.OnBufferingUpdateListener {
}
