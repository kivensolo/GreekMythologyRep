package com.kingz.library.player;

import android.media.TimedText;

/**
 * author：KingZ
 * date：2019/7/30
 * description：
 */

public interface IMediaPlayerCallBack {

    /**
     * 视频准备好播放
     *
     * @param player 当前的播放器对象
     */
    void onPrepared(IMediaPlayer player);

    /**
     * 开始播放，调用play之后就会回调这方法
     * 可以再这个回调里面更新外部UI
     *
     * @param player 当前的播放器对象
     */
    void onPlay(IMediaPlayer player);

    /**
     * 播放出错
     *
     * @param player 当前的播放器对象
     * @param what   错误码
     * @param extra  附加信息
     */
    boolean onError(IMediaPlayer player, int what, int extra);

    /**
     * 缓冲开始
     *
     * @param player 当前的播放器对象
     */
    void onBufferStart(IMediaPlayer player);

    /**
     * 缓冲结束
     *
     * @param player 当前的播放器对象
     */
    void onBufferEnd(IMediaPlayer player);

    /**
     * 缓存更新
     *
     * @param player  当前的播放器对象
     * @param percent 缓存的进度
     */
    void onBufferingUpdate(IMediaPlayer player, int percent);

    /**
     * 播放完成
     *
     * @param player 当前的播放器对象
     */
    void onCompletion(IMediaPlayer player);

    /**
     * seek完成
     *
     * @param player 当前的播放器对象
     */
    void onSeekComplete(IMediaPlayer player);

    /**
     * 每秒一次的回调 让外部更新UI
     */
    void onProgressInSecond();

    /**
     * 信息回调  比如第一帧显示等等
     *
     * @param player 当前的播放器对象
     * @param what   错误码
     * @param extra  附加信息
     * @return
     */
    boolean onInfo(IMediaPlayer player, int what, int extra);

    /**
     * @param player 当前的播放器对象
     * @param text   字幕内容
     */
    void onTimedText(IMediaPlayer player, TimedText text);

    /**
     * 承载播放的view创建的回调
     */
    void onViewCreated();

    /**
     * 承载播放的view变化的回调
     */
    void onViewChanged(int format, int width, int height);

    /**
     * 承载播放的view销毁的回调
     */
    void onViewDestroyed();

}
