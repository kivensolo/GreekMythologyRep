package com.kingz.library.player;

import android.media.TimedText;

/**
 * author：KingZ
 * date：2019/7/30
 * description： 播放器事件回调接口
 */

public interface IPlayerEventsCallBack {

    /**
     * 开始播放，调用play之后就会回调这方法
     * 可以再这个回调里面更新外部UI  是否可以去掉？？
     */
    void onPlay();

    void onPrepared();

    boolean onError(int what, int extra);

    void onBufferStart();

    void onBufferEnd();

    void onBufferingUpdate(int percent);

    void onCompletion();

    void onSeekComplete();

    /**
     * 播放器定时刷新回调  主要用于更新UI
     */
    void onPlayerTimingUpdate();

    boolean onInfo(int what, int extra);

    /**
     * @param text   字幕内容
     */
    void onTimedText(TimedText text);

    /**
     * 承载播放的view创建
     */
    void onViewCreated();

    /**
     * 承载播放的view变化
     */
    void onViewChanged(int format, int width, int height);

    /**
     * 承载播放的view销毁
     */
    void onViewDestroyed();

}
