package com.kingz.mobile.libhlscache;

import com.kingz.mobile.libhlscache.bean.IntRanges;
import com.kingz.mobile.libhlscache.bean.VideoInfo;
import com.kingz.mobile.libhlscache.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The priority download manager of TS fragment.
 */
class TsDownloadPriorityManager {
    private Random random = new Random(System.currentTimeMillis());
    private List<Pair<String,Integer>> requestFIFO = new ArrayList<>(); // Pair<video id,ts index>，请求队列，优先控制下载请求队列里的分片。

    void addRequestFIFO(String videoId, int tsIndex) {
        Pair<String,Integer> sip = Pair.create(videoId, tsIndex);
        if (!requestFIFO.contains(sip)) {
            requestFIFO.add(0, sip);
        }
    }

    Pair<String,Integer> getNextNeedDownloadTs(Map<String, VideoInfo> videoInfoMap) {

        // 1. 请求队列有内容则优先下载请求队列。
        if (!requestFIFO.isEmpty()) {
            return requestFIFO.remove(requestFIFO.size() - 1);
        }


        // 找出 STATE_DOWNLOADING 的影片
        List<VideoInfo> downingVideos = new ArrayList<>();
        for (VideoInfo v : videoInfoMap.values()) {
            if (v.getState() == VideoInfo.STATE_ONLINE_DOWNLOADING) {
                downingVideos.add(v);
            }
        }

        // 2. 缓冲时长设置较短的且目前缓冲时间不足的优先下载。
        for (VideoInfo videoInfo : downingVideos) {
            if (videoInfo.getConfig().getForwardCacheTime() != Integer.MAX_VALUE) {
                // 读取已缓存距离进行比较。
                int lastRequestTsIndex = videoInfo.getLastRequestTsIndex();
                Pair<Integer,Integer> containsPair = videoInfo.getDownloadedTsRanges().contains(lastRequestTsIndex);

                if (containsPair != null) {
                    float finishBufferTime = videoInfo.tsIndexToStartTime(lastRequestTsIndex) +
                            videoInfo.getConfig().getForwardCacheTime();
                    if (videoInfo.tsIndexToStartTime(containsPair.second) < finishBufferTime) {
                        return Pair.create(videoInfo.getId(), containsPair.second);
                    }
                } else {
                    return Pair.create(videoInfo.getId(), lastRequestTsIndex);
                }
            }
        }

        // 3. 随机选取缓冲时长设置为 max 的文件进行下载。
        // 取出所有预先下载时长为 MAX_VALUE 的影片
        List<VideoInfo> maxVideoInfos = new ArrayList<>();
        for (VideoInfo videoInfo : downingVideos) {
            if (videoInfo.getConfig().getForwardCacheTime() == Integer.MAX_VALUE) {
                maxVideoInfos.add(videoInfo);
            }
        }
        // TODO: 2017/11/21 优化：优先取上次请求分片之后的分片进行下载
        // 随机选取进行下载
        if (!maxVideoInfos.isEmpty()) {
            int rand = random.nextInt(maxVideoInfos.size());
            VideoInfo videoInfo = maxVideoInfos.get(rand);
            IntRanges downloadedTsRanges = videoInfo.getDownloadedTsRanges();
            int tsIndex = 0;
            if (!downloadedTsRanges.getRanges().isEmpty()) {
                tsIndex = downloadedTsRanges.getRanges().get(0).second;
            }
            return Pair.create(videoInfo.getId(), tsIndex);
        }

        return null;
    }
}
