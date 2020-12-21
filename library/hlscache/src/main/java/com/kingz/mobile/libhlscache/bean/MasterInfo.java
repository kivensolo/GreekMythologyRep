package com.kingz.mobile.libhlscache.bean;

import com.iheartradio.m3u8.data.PlaylistData;
import com.kingz.mobile.libhlscache.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 多路流顶层信息
 * Created  2017/11/21.
 */
public class MasterInfo {
    private final String id;
    private final String url;
    private CacheVideoConfig config;
    private List<SubInfo> subInfos = new ArrayList<>();
    private int curPlayIndex = 0;  // 当前正在播放/缓存的流index

    public MasterInfo(String id, String url, CacheVideoConfig config) {
        this.id = id;
        this.url = url;
        this.config = config;
    }

    public void addSubInfos(List<PlaylistData> playlists) {
        for (PlaylistData playlistData : playlists) {
            addSubInfo(playlistData.getUri(), playlistData.getStreamInfo().getBandwidth());
        }
    }

    private void addSubInfo(String url, int bandWidth) {
        MasterInfo.SubInfo subInfo = new MasterInfo.SubInfo();
        subInfo.rawUrl = url;
        subInfo.bandWidth = bandWidth;
        subInfo.id = id + "_____" + bandWidth;
        subInfos.add(subInfo);
    }

    public SubInfo getCurPlaySubInfo() {
        return subInfos.get(curPlayIndex);
    }

    // --------------- Getters And Setters --------------------

    public String getId() {
        return id;
    }

    public List<SubInfo> getSubInfos() {
        return subInfos;
    }

    public CacheVideoConfig getConfig() {
        return config;
    }

    public int getCurPlayIndex() {
        return curPlayIndex;
    }

    public void setCurPlayIndex(int curPlayIndex) {
        this.curPlayIndex = curPlayIndex;
    }

    public class SubInfo {
        int bandWidth;
        String id;
        String rawUrl;

        public String getAbsoluteUrl() {
            return Tools.getAbsoluteUrl(url, rawUrl);
        }

        public String getId() {
            return id;
        }

        public int getBandWidth() {
            return bandWidth;
        }
    }
}
