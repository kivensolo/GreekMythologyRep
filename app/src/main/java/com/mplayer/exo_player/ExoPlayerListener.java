package com.mplayer.exo_player;

import android.util.Log;

import com.google.android.exoplayer2.Player;
import com.kingz.utils.ZLog;

/**
 * author：KingZ
 * date：2019/7/22
 * description：exoPlayer的事件监听类
 */
public class ExoPlayerListener extends Player.DefaultEventListener {
    public static final String TAG = "ExoPlayerListener";
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        String stateString;
        // actually playing media
        if (playWhenReady && playbackState == Player.STATE_READY) {
            ZLog.d(TAG, "onPlayerStateChanged: actually playing media");
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
}
