package com.kingz.play;

import com.App;
import com.kingz.library.player.IMediaPlayer;
import com.kingz.library.player.MediaPlayerFactory;

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
    public IMediaPlayer getMediaPlayerCore() {
        return MediaPlayerFactory.newInstance(App.getAppInstance(),
                MediaPlayerFactory.FLAG_EXO, null);
    }
}
