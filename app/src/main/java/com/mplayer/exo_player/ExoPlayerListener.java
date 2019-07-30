package com.mplayer.exo_player;

import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.kingz.utils.ZLog;

import java.io.IOException;

/**
 * author：KingZ
 * date：2019/7/22
 * description：exoPlayer的事件监听类
 * 两个最重要的是{@link #onPlayerStateChanged}和{@link #onPlayerError}
 * 具体的参见<a herf="https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/Player.EventListener.html">JavaDoc</>
 */
public class ExoPlayerListener extends Player.DefaultEventListener {
    public static final String TAG = "ExoPlayerListener";

    /**
     * Called when the player starts or stops loading the source.
     */
    @Override
    public void onLoadingChanged(boolean isLoading) {
        super.onLoadingChanged(isLoading);
    }


    /**
     * 当 <a herf="https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/Player.html#getPlayWhenReady--">Player.getPlayWhenReady()</>
     * 或者<a herf="https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/Player.html#getPlaybackState--">Player.getPlaybackState()</>
     * 的返回值改变的时候被调用.
     *
     * playbackState一共有四个状态:
     *  Player.STATE_IDLE : 初始化状态，当播放器stopped或者playback失败的时候，
     *  Player.STATE_BUFFERING: 缓冲状态
     *  Player.STATE_READY: 播放器能够立即从当前位置开始播放
     *  Player.STATE_ENDED：播放器播完了所有媒体资源
     *
     * playWhenReady的flag是为了表示用户的播放意图，
     * 播放器只有在playWhenReady=true和状态为Player.STATE_READY时才能播放。
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        String stateString;
        if (playWhenReady && playbackState == Player.STATE_READY) {
            // Active playback.
            ZLog.d(TAG, "onPlayerStateChanged: actually playing media");
        }else if (playWhenReady) {
            // Not playing because playback ended, the player is buffering, stopped or
            // failed. Check playbackState and player.getPlaybackError for details.
        } else {
            // Paused by app.
        }
        switch (playbackState) {
            case Player.STATE_IDLE:
                stateString = "ExoPlayer.STATE_IDLE      -";
                break;
            case Player.STATE_BUFFERING:
                stateString = "ExoPlayer.STATE_BUFFERING -";
                break;
            case Player.STATE_READY:
                stateString = "ExoPlayer.STATE_READY     -";
                break;
            case Player.STATE_ENDED:
                stateString = "ExoPlayer.STATE_ENDED     -";
                break;
            default:
                stateString = "UNKNOWN_STATE             -";
                break;
        }
        Log.d(TAG, "changed state to " + stateString + " playWhenReady: " + playWhenReady);
        super.onPlayerStateChanged(playWhenReady, playbackState);
    }

    /**
     * Called when an error occurs.
     * 此方法会在播放器状态变为IDLE之前回调。
     */
    @Override
    public void onPlayerError(ExoPlaybackException error) {
        super.onPlayerError(error);
        if (error.type == ExoPlaybackException.TYPE_SOURCE) {
            IOException cause = error.getSourceException();
            ZLog.e("ExoPlayerListener","onPlayerError !  cause:" + cause.getMessage());
            if (cause instanceof HttpDataSource.HttpDataSourceException) {
                // An HTTP error occurred.
                HttpDataSource.HttpDataSourceException httpError = (HttpDataSource.HttpDataSourceException) cause;
                // This is the request for which the error occurred.
                DataSpec requestDataSpec = httpError.dataSpec;
                // It's possible to find out more about the error both by casting and by
                // querying the cause.
                if (httpError instanceof HttpDataSource.InvalidResponseCodeException) {
                    // Cast to InvalidResponseCodeException and retrieve the response code,
                    // message and headers.
                } else {
                    // Try calling httpError.getCause() to retrieve the underlying cause,
                    // although note that it may be null.
                }
            }
        }
    }
}
