package com.kingz.mobile.libhlscache.bean;

/**
 * 进度，用于对外接口。
 * Created  2017/11/16.
 */
public class Progress {
    private String id;
    private float downloadedDuration;
    private float duration;
    private int downloadedBytes;
    private int totalBytes;
    private int state;

    public Progress(String id, float downloadedDuration, float duration, int downloadedBytes, int totalBytes,
                    int state) {
        this.id = id;
        this.downloadedDuration = downloadedDuration;
        this.duration = duration;
        this.downloadedBytes = downloadedBytes;
        this.totalBytes = totalBytes;
        this.state = state;
    }

    /**
     * @return 视频 id , 为外部传入的 id 值.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 视频已经下载的时长.
     *
     * @return 时长，单位 s
     */
    public float getDownloadedDuration() {
        return downloadedDuration;
    }

    /**
     * 视频总时长.
     *
     * @return 总时长，单位 s
     */
    public float getDuration() {
        return duration;
    }

    /**
     * 已经下载的字节数，不精确.
     *
     * @return 字节数，单位 byte
     */
    public int getDownloadedBytes() {
        return downloadedBytes;
    }

    /**
     * 视频总字节数，不精确.
     *
     * @return 字节数，单位 byte
     */
    public int getTotalBytes() {
        return totalBytes;
    }

    /**
     * @return 是否下载完成
     */
    public boolean isDownloadFinish() {
        return Math.abs(duration - downloadedDuration) < 0.01;
    }

    /**
     * 获取下载状态
     *
     * @return {@link VideoInfo#STATE_ONLINE_DOWNLOADING} 下载中
     * {@link VideoInfo#STATE_ONLINE_ERROR} 下载失败
     * {@link VideoInfo#STATE_ONLINE_PAUSE} 暂停下载
     * {@link VideoInfo#STATE_OFFLINE} 未激活视频，有两种情况：1. 下载完成(通过 {@link #isDownloadFinish()})
     * 判断. 2. 上次退出时存留的视频.
     */
    public int getState() {
        return state;
    }
}
