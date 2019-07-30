package com.kingz.library.player;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

/**
 * author：KingZ
 * date：2019/7/30
 * description：播放器对象顶层接口
 */
public interface IMediaPlayer {
    String TAG = IMediaPlayer.class.getName();
    int MEDIA_ERROR_SEEK_STATUS = 0x1001;
    int MEDIA_ERROR_PLAY_STATUS = 0x1002;
    int MEDIA_ERROR_PAUSE_STATUS = 0x1003;
    int MEDIA_ERROR_CUSTOM_ERROR = 0x1011;

    int MEDIA_INFO_BUFFERING_START = 701;
    int MEDIA_INFO_BUFFERING_END = 702;
    //视频第一帧显示出来
    int MEDIA_INFO_VIDEO_RENDERING_START = 3;

    // -------------------- 播放操作api ----------
    void play();
    void pause();
    void release();
    void seekTo(long msec);

    // -------------------- 设置api ---------------

    void setPlayerView(View playView);
    void setPlayURI(Uri uri);
    void setPlayerEventCallBack(IMediaPlayerCallBack callBack);

    /**
     * 设置播放速率
     * @param speed 速率 支持0.5-2.0
     */
    void setSpeed(float speed);

    /**
     * 设置播放器的缓冲区大小
     *
     * @param bufferSize 缓冲区大小  单位kb
     */
    void setBufferSize(int bufferSize);


    // -------------------- 读取api ----------
    boolean isPrepared();
    boolean isPlaying();
    boolean isBuffering();
    boolean isPaused();
    Uri getCurrentURI();

    /**
     * Gets the duration of the file.
     * @return 流总长度 ms
     */
    long getDuration();

    /**
     * Gets the current playback position.
     * @return the current position in milliseconds
     */
    long getCurrentPosition();

    /**
     * @return 缓冲的进度  ms
     */
    long getBufferedPosition();

    /**
     * 获取当前耗费的流量
     * @return 单位byte
     */
    long getTcpSpeed();

    /**
     * 获取当前帧的图
     * @return 位图数据
     */
    Bitmap getCurrentThumb();


}
