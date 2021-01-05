package com.kingz.mobile.libhlscache.bean;

import androidx.annotation.IntDef;

import com.iheartradio.m3u8.data.Playlist;
import com.iheartradio.m3u8.data.TrackData;
import com.kingz.mobile.libhlscache.utils.Pair;
import com.kingz.mobile.libhlscache.utils.Tools;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * 影片信息。
 * Created  2017/11/20.
 */
public class VideoInfo {
    private String id;
    // 分片分界时间，最后一个为影片时长。单位 s
    private float[] tsDivideTimes;
    // 已下载的 ts 分片段，由多个 [start, end) 组成
    private IntRanges downloadedTsRanges = new IntRanges();

    // online 表示 url 有效。
    // 下载中
    public static final int STATE_ONLINE_DOWNLOADING = 0;
    // 下载失败
    public static final int STATE_ONLINE_ERROR = 1;
    // 暂停下载
    public static final int STATE_ONLINE_PAUSE = 2;
    // 未激活视频，有两种情况：
    // 1. 下载完成(通过 {@link #isDownloadFinish()})判断.
    // 2. 上次退出时存留的视频.
    public static final int STATE_OFFLINE = 3; // 完成或者没有设置 url

    @IntDef({STATE_ONLINE_DOWNLOADING, STATE_ONLINE_ERROR, STATE_ONLINE_PAUSE, STATE_OFFLINE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DownLoadState {}

    private int state;

    private String url;
    private List<String> tsDownloadRawUrls;
    private CacheVideoConfig config;

    private int lastRequestTsIndex;  // 上次进行 http 请求的 ts 分片。

    public static final int TOTAL_BYTES_UNDEFINE = -1;
    private int totalBytes = TOTAL_BYTES_UNDEFINE;       // 总大小，某个分片下载完成后进行估算，不准确。
    private int downloadedBytes;  // 已下载的大小，估算，不准确。可用于计算下载速度。

    private VideoInfo() {
    }

    public static VideoInfo createOnlineVideoInfo(
            String id, String url, CacheVideoConfig config, Playlist playlist, int downloadedBytes,
            int totalBytes) {
        VideoInfo v = new VideoInfo();
        v.id = id;
        v.config = config;
        v.url = url;
        v.downloadedBytes = downloadedBytes;
        v.totalBytes = totalBytes;

        // 保存每个分片的下载地址
        v.tsDownloadRawUrls = new ArrayList<>();
        for (TrackData trackData : playlist.getMediaPlaylist().getTracks()) {
            v.tsDownloadRawUrls.add(trackData.getUri());
        }

        // 计算每个分片的开始时间，持续时间，影片总时长
        v.tsDivideTimes = calcTsDivideTimes(playlist);
        v.state = STATE_ONLINE_DOWNLOADING;
        return v;
    }

    public static VideoInfo createOfflineVideoInfo(
            String id, List<Integer> downloadedTses, Playlist playlist, int downloadedBytes,
            int totalBytes) {
        VideoInfo v = new VideoInfo();
        v.id = id;
        v.downloadedBytes = downloadedBytes;
        v.totalBytes = totalBytes;
        v.tsDivideTimes = calcTsDivideTimes(playlist);

        for (Integer downloadedTs : downloadedTses) {
            v.downloadedTsRanges.addInt(downloadedTs);
        }
        v.state = STATE_OFFLINE;
        return v;
    }

    private static float[] calcTsDivideTimes(Playlist playlist) {
        float[] tsDivideTimes = new float[playlist.getMediaPlaylist().getTracks().size() + 1];
        float duration = 0;
        for (int i = 0; i < playlist.getMediaPlaylist().getTracks().size(); i++) {
            tsDivideTimes[i] = duration;
            TrackData trackData = playlist.getMediaPlaylist().getTracks().get(i);
            duration += trackData.getTrackInfo().duration;
        }
        tsDivideTimes[tsDivideTimes.length - 1] = duration;
        return tsDivideTimes;
    }

    public void switchToOffline() {
        state = STATE_OFFLINE;
    }

    public void setPause() {
        state = STATE_ONLINE_PAUSE;
    }

    public void setError() {
        state = STATE_ONLINE_ERROR;
    }

    public void setDownloading() {
        state = STATE_ONLINE_DOWNLOADING;
    }

    public boolean isDownloadFinish() {
        List<Pair<Integer,Integer>> ranges = downloadedTsRanges.getRanges();
        if (ranges.size() == 1) {
            Pair<Integer,Integer> pair = ranges.get(0);
            if (pair.first == 0 && pair.second == tsDivideTimes.length - 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isOffline() {
        return state == STATE_OFFLINE;
    }

    public float getDuration() {
        return tsDivideTimes[tsDivideTimes.length - 1];
    }

    public void addDownloadedTs(int tsIndex) {
        downloadedTsRanges.addInt(tsIndex);
    }

    public void addDownloadedTses(List<Integer> tsIndexes) {
        for (Integer tsIndex : tsIndexes) {
            downloadedTsRanges.addInt(tsIndex);
        }
    }

    public Pair<Integer,Integer> hasDownloadedTs(int tsIndex) {
        return downloadedTsRanges.contains(tsIndex);
    }

    public float tsIndexToStartTime(int i) {
        return tsDivideTimes[i];
    }

    public String getAbsoluteDownloadUrl(int i) {
        return Tools.getAbsoluteUrl(url, tsDownloadRawUrls.get(i));
    }

    public void addDownloadedBytes(int newBytes) {
        downloadedBytes += newBytes;
    }

    public int getTsCount() {
        return tsDownloadRawUrls.size();
    }

    public String getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public void setLastRequestTsIndex(int lastRequestTsIndex) {
        this.lastRequestTsIndex = lastRequestTsIndex;
    }

    public IntRanges getDownloadedTsRanges() {
        return downloadedTsRanges;
    }

    public CacheVideoConfig getConfig() {
        return config;
    }

    public int getLastRequestTsIndex() {
        return lastRequestTsIndex;
    }

    public int getDownloadedBytes() {
        return downloadedBytes;
    }

    public void setDownloadedBytes(int downloadedBytes) {
        this.downloadedBytes = downloadedBytes;
    }

    public int getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(int totalBytes) {
        this.totalBytes = totalBytes;
    }

}
