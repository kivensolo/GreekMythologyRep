package com.kingz.library.player.internal;

import android.media.MediaPlayer;

/**
 * author: King.Z <br>
 * date:  2016/9/8 16:38 <br>
 * description: Android原生播放器状态监听接口 <br>
 * 相关样例接口描述文档参见:
 * <a herf="https://developer.android.com/reference/android/media/MediaPlayer.OnBufferingUpdateListener.html?hl=en"></a>
 */
public interface AndroidMediaPlayerListeners extends
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnTimedTextListener,
        MediaPlayer.OnSeekCompleteListener{
}
