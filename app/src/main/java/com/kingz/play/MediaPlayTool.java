package com.kingz.play;

import com.kingz.library.player.IPlayer;
import com.kingz.library.player.MediaPlayerFactory;
import com.zeke.ktx.App;

/**
 * description：单例播放器  为后面的悬浮播放准备 也可以避免多个播放器同时在播放的问题
 */
public class MediaPlayTool {
    private static MediaPlayTool ourInstance;


    public static synchronized MediaPlayTool getInstance() {
        if (ourInstance == null) {
            ourInstance = new MediaPlayTool();
        }
        return ourInstance;
    }

    private MediaPlayTool() {
    }

    /**
     * 获取 IMediaPlayer 实例
     */
    public IPlayer getMediaPlayerCore() {
        return MediaPlayerFactory.newInstance(App.instance.getApplicationContext(),
                MediaPlayerFactory.FLAG_EXO, null);
    }
}
