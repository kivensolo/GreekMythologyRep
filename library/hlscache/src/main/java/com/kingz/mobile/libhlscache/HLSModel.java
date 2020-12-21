package com.kingz.mobile.libhlscache;

import com.iheartradio.m3u8.Encoding;
import com.iheartradio.m3u8.Format;
import com.iheartradio.m3u8.ParseException;
import com.iheartradio.m3u8.PlaylistException;
import com.iheartradio.m3u8.PlaylistParser;
import com.iheartradio.m3u8.data.Playlist;
import com.iheartradio.m3u8.data.PlaylistData;
import com.kingz.mobile.libhlscache.bean.CacheConfig;
import com.kingz.mobile.libhlscache.bean.CacheVideoConfig;
import com.kingz.mobile.libhlscache.bean.IntRanges;
import com.kingz.mobile.libhlscache.bean.MasterInfo;
import com.kingz.mobile.libhlscache.bean.Progress;
import com.kingz.mobile.libhlscache.bean.VideoInfo;
import com.kingz.mobile.libhlscache.http.AbsHttpRequester;
import com.kingz.mobile.libhlscache.utils.ActionCallBack;
import com.kingz.mobile.libhlscache.utils.IOUtils;
import com.kingz.mobile.libhlscache.utils.IntIntPair;
import com.kingz.mobile.libhlscache.utils.LogUtils;
import com.kingz.mobile.libhlscache.utils.Pair;
import com.kingz.mobile.libhlscache.utils.StringIntPair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HLS 数据模块，线程安全。
 * TODO 抽离文件保存与文件读取逻辑到单独类。
 */
public class HLSModel {
    static final String FILE_NAME_LOCAL = "local.m3u8";
    private static final String FILE_NAME_RAW = "remote.m3u8";
    private static final String FILE_NAME_MASTER = "master.m3u8";

    private Map<String, VideoInfo> videoMap = new HashMap<>();  // 保存正在进行下载的影片
    private Map<String, MasterInfo> masterMap = new HashMap<>();  // 多路流顶层信息集合

    private HLSCache hlsCache;
    private AbsHttpRequester httpRequester;
    private CacheConfig cacheConfig;
    private Downloader downloader;

    private TsDownloadPriorityManager priorityManager;

    void init(HLSCache manager) {
        hlsCache = manager;
        httpRequester = manager.getHttpRequester();
        cacheConfig = manager.getCacheConfig();
        downloader = manager.getDownloader();

        priorityManager = new TsDownloadPriorityManager();

        // 读取磁盘已经下载的视频
        Pair<List<VideoInfo>, List<MasterInfo>> infosPair = readVideoInfosAndMasterInfoFromFile();
        Map<String, VideoInfo> tmpVMap = new HashMap<>();
        for (VideoInfo v : infosPair.first) {
            tmpVMap.put(v.getId(), v);
        }

        synchronized (this) {
            videoMap.putAll(tmpVMap);
        }

        Map<String, MasterInfo> tmpMMap = new HashMap<>();
        for (MasterInfo m : infosPair.second) {
            tmpMMap.put(m.getId(), m);
        }

        synchronized (this) {
            masterMap.putAll(tmpMMap);
        }
    }

    void parseM3u8(final String url, final String id, final CacheVideoConfig config, final ParseM3u8Callback callback) {
        // 文件夹不存在则创建
        try {
            createVideoDirIfNoExists(id);
        } catch (IOException e) {
            if (callback != null) {
                callback.onFail(e);
                return;
            }
        }

        // 下载 remote.m3u8
        httpRequester.asyncDownloadFile(url, getRawM3u8FilePath(id), new ActionCallBack<String>() {
            @Override
            public void call(String filePath) {
                try {
                    Playlist playlist = parsePlaylist(filePath);
                    if (playlist.getMediaPlaylist() != null) {
                        // 单路流解析
                        prepareSingleM3u8(playlist, id, url, config);

                        if (callback != null) {
                            callback.onSuccess(null);
                        }
                    } else if (playlist.getMasterPlaylist() != null) {
                        // 多路流解析
                        final MasterInfo masterInfo = prepareMultiM3u8(filePath, playlist, id, url, config);

                        // 默认缓存第一路流
                        if (!masterInfo.getSubInfos().isEmpty()) {
                            MasterInfo.SubInfo subInfo = masterInfo.getSubInfos().get(0);
                            LogUtils.i("Multi m3u8.");
                            parseM3u8(subInfo.getAbsoluteUrl(), subInfo.getId(), masterInfo.getConfig()
                                    , new ParseM3u8Callback() {
                                        @Override
                                        public void onSuccess(int[] bandWidths) {
                                            if (callback != null) {
                                                int[] realBand = new int[masterInfo.getSubInfos().size()];
                                                for (int i = 0; i < masterInfo.getSubInfos().size(); i++) {
                                                    realBand[i] = masterInfo.getSubInfos().get(i).getBandWidth();
                                                }

                                                callback.onSuccess(realBand);
                                            }
                                        }

                                        @Override
                                        public void onFail(Exception e) {
                                            if (callback != null) {
                                                callback.onFail(e);
                                            }
                                        }
                                    });
                        } else {
                            if (callback != null) {
                                callback.onSuccess(new int[0]);
                            }
                        }
                    }
                } catch (IOException | ParseException | PlaylistException e) {
                    if (callback != null) {
                        callback.onFail(e);
                    }
                }
            }
        }, new ActionCallBack<IOException>() {
            @Override
            public void call(IOException e) {
                if (callback != null) {
                    callback.onFail(e);
                }
            }
        }, null);
    }

    /**
     * 删除指定影片的缓存文件。
     *
     * @param id              影片 id
     * @param keepRawM3u8File 是否保留原始 m3u8 文件
     */
    void deleteCacheFiles(String id, boolean keepRawM3u8File) {
        File videoDir = new File(getVideoDirPath(id));
        File[] listFiles = videoDir.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (keepRawM3u8File && file.getAbsolutePath().endsWith(FILE_NAME_RAW)) {
                    // not delete
                } else {
                    boolean success = file.delete();
                    if (!success) {
                        LogUtils.w("Cache file " + file + " delete failed.");
                    }
                }
            }
        }

        if (!keepRawM3u8File && !videoDir.delete()) {
            LogUtils.w("Cache file " + videoDir + " delete failed.");
        }
    }


    String getTsFilePath(String id, int tsIndex) {
        return getVideoDirPath(id) + File.separator + tsIndex + ".ts";
    }

    File serveLocalM3u8FileEnsureExists(String id) {
        if (isMasterId(id)) {
            id = convertMasterIdToCurSubId(id);
        }
        return new File(getLocalM3u8FilePath(id));
    }

    synchronized File serveTsFileEnsureExists(String id, int tsIndex) throws InterruptedException {
        if (isMasterId(id)) {
            // 多路流 id，需做一次 id 转换
            id = convertMasterIdToCurSubId(id);
        }

        VideoInfo v = videoMap.get(id);
        File file = new File(getTsFilePath(id, tsIndex));

        LogUtils.i("fetch " + id + "/" + tsIndex);
        if (v == null) {
            return null;
        } else if (v.isOffline()) {
            return file;
        } else {
            // 在线下载中，maybe 需要进行线程同步。
            v.setLastRequestTsIndex(tsIndex);
            while (!file.exists()) {
                // 添加到请求队列，该分片会具有高优先级。
                priorityManager.addRequestFIFO(id, tsIndex);
                wait();
            }
            return file;
        }
    }

    /**
     * 设置某分片下载完成
     */
    public void setTsDownloadFinish(String id, int tsIndex) {
        boolean downloadFinish;
        synchronized (this) {

            VideoInfo v = videoMap.get(id);
            if (v == null) {
                return;
            }
            v.addDownloadedTs(tsIndex);
            notifyAll();

            if (v.getTotalBytes() == VideoInfo.TOTAL_BYTES_UNDEFINE) {
                v.setTotalBytes(calcTotalBytes(v.getId(), tsIndex, v.getTsCount()));
            }

            // 检查视频是否下载完成，完成回调
            downloadFinish = v.isDownloadFinish();
            if (downloadFinish) {
                // 转换为离线状态
                v.switchToOffline();
            }
        }
        if (downloadFinish) {
            hlsCache.callbackDownloadFinish(id);
        }
    }

    /**
     * 从下载队列中获取下一个待下载的分片。
     *
     * @return something
     */
    synchronized StringIntPair getNextNeedDownloadTs() {
        return priorityManager.getNextNeedDownloadTs(videoMap);
    }

    synchronized void switchBandwidth(final String id, int targetBandwidth) {
        final MasterInfo masterInfo = masterMap.get(id);
        if (masterInfo != null) {
            int newIndex = -1;
            for (int i = 0; i < masterInfo.getSubInfos().size(); i++) {
                if (masterInfo.getSubInfos().get(i).getBandWidth() == targetBandwidth) {
                    newIndex = i;
                    break;
                }
            }
            if (newIndex == masterInfo.getCurPlayIndex()) {
                // 正是当前播放的码率，无需切换。
                return;
            }

            if (newIndex != -1) {
                LogUtils.i("switch bandwidth to " + targetBandwidth + ".");
                VideoInfo oldv = videoMap.get(masterInfo.getCurPlaySubInfo().getId());
                oldv.switchToOffline();

                MasterInfo.SubInfo subInfo = masterInfo.getSubInfos().get(newIndex);
                final int finalNewIndex = newIndex;
                parseM3u8(subInfo.getAbsoluteUrl(), subInfo.getId(), masterInfo.getConfig(), new ParseM3u8Callback() {
                    @Override
                    public void onSuccess(int[] bandWidths) {
                        // TODO: 2017/11/22 回调处理，下同
                        masterInfo.setCurPlayIndex(finalNewIndex);
                    }

                    @Override
                    public void onFail(Exception e) {

                    }
                });
            } else {
                throw new IllegalArgumentException("Invalid target band width " + targetBandwidth + ".");
            }
        } else {
            throw new IllegalArgumentException(id + " isn't a master multi m3u8 id.");
        }
    }


    /**
     * 读取磁盘已经下载的视频与多路流 master 信息
     *
     * @return 视频 videoInfo
     */
    private Pair<List<VideoInfo>, List<MasterInfo>> readVideoInfosAndMasterInfoFromFile() {
        List<VideoInfo> videoInfos = new ArrayList<>();
        List<MasterInfo> masterInfos = new ArrayList<>();

        File file = new File(cacheConfig.getCachePath());
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File listFile : listFiles) {
                // videoInfo ：检查 local m3u8 文件与 ts 分片，若存在 local m3u8 且存在任一 ts 分片文件
                File localM3u8File = new File(listFile.getAbsolutePath() + File.separator + FILE_NAME_LOCAL);
                List<Integer> tses = readDownloadedTsesFromFile(listFile.getName());

                // masterInfo : 检查 master m3u8 文件是否存在。
                File masterM3u8File = new File(listFile.getAbsoluteFile() + File.separator + FILE_NAME_MASTER);

                if (localM3u8File.exists() && !tses.isEmpty()) {
                    // videoInfo 文件
                    try {
                        String id = listFile.getName();
                        Playlist playlist = parsePlaylist(localM3u8File.getAbsolutePath());

                        int downloadedBytes = calcDownloadedBytes(id, tses);
                        int totalBytes = calcTotalBytes(id, tses, playlist.getMediaPlaylist().getTracks().size());

                        videoInfos.add(VideoInfo.createOfflineVideoInfo(id, tses, playlist,
                                downloadedBytes, totalBytes));
                    } catch (IOException | ParseException | PlaylistException e) {
                        e.printStackTrace();
                    }
                } else if (masterM3u8File.exists()) {
                    // masterInfo 文件
                    masterInfos.add(new MasterInfo(listFile.getName(), null, null));
                }
            }
        }
        return Pair.create(videoInfos, masterInfos);
    }

    private MasterInfo prepareMultiM3u8(String filePath, Playlist playlist, String id, String url, CacheVideoConfig config) {
        // 保存所有子路流 m3u8 地址
        List<PlaylistData> playlists = playlist.getMasterPlaylist().getPlaylists();
        MasterInfo masterInfo = new MasterInfo(id, url, config);
        if (playlists != null) {
            masterInfo.addSubInfos(playlists);
        }

        IOUtils.rename(filePath, getMasterM3u8FilePath(id));
        synchronized (this) {
            masterMap.put(id, masterInfo);
        }
        return masterInfo;
    }

    private VideoInfo prepareSingleM3u8(Playlist playlist, String id, String url, CacheVideoConfig config) throws IOException {
        List<Integer> downloadedTsIndexes = Collections.emptyList();
        if (isLocalM3u8FileExists(id)) {
            // 本地代理文件存在，判断流内容是否改变
            if (isLocalM3u8SameWithRawM3u8(id)) {
                // 流内容未改变，读取下载进度
                downloadedTsIndexes = readDownloadedTsesFromFile(id);
            } else {
                // 流内容改变，删除缓存文件
                deleteCacheFiles(id, true);
                createLocalM3u8File(id);
            }
        } else {
            // 没有代理文件，生成代理文件
            createLocalM3u8File(id);
        }

        // 准备完成，解析保存数据。
        int downloadedBytes = calcDownloadedBytes(id, downloadedTsIndexes);
        int totalBytes = calcTotalBytes(id, downloadedTsIndexes, playlist.getMediaPlaylist().getTracks().size());
        VideoInfo v = VideoInfo.createOnlineVideoInfo(id, url, config, playlist, downloadedBytes, totalBytes);

        v.addDownloadedTses(downloadedTsIndexes);
        LogUtils.i("downloadedTsRange: " + v.getDownloadedTsRanges());
        if (v.isDownloadFinish()) {
            // 完成回调
            hlsCache.callbackDownloadFinish(id);
            v.switchToOffline();
        } else {
            downloader.checkAndStart();
        }
        synchronized (this) {
            videoMap.put(id, v);
        }
        return v;
    }

    /**
     * 从文件系统中获取某部影片已经下载的分片 index
     *
     * @param id 影片 id
     * @return 分片 indexes
     */
    private List<Integer> readDownloadedTsesFromFile(String id) {
        List<Integer> res = new ArrayList<>();
        File dir = new File(getVideoDirPath(id));
        String[] tsFileNames = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(".ts");
            }
        });
        if (tsFileNames != null) {
            for (String tsFileName : tsFileNames) {
                int tsIndex = Integer.parseInt(tsFileName.substring(
                        tsFileName.lastIndexOf('/') + 1, tsFileName.indexOf(".ts")));
                res.add(tsIndex);
            }
        }
        return res;
    }

    /**
     * 解析 m3u8 文件
     */
    private Playlist parsePlaylist(String m3u8FilePath) throws IOException, ParseException, PlaylistException {
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(m3u8FilePath));
            PlaylistParser parser = new PlaylistParser(in, Format.EXT_M3U, Encoding.UTF_8);
            return parser.parse();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
    }


    /**
     * 判断指定影片的本地代理 local.m3u8 文件是否存在。
     *
     * @param id 影片 id
     * @return 是否存在
     */
    private boolean isLocalM3u8FileExists(String id) {
        String localM3u8FilePath = getLocalM3u8FilePath(id);
        return IOUtils.exists(localM3u8FilePath);
    }

    /**
     * 比较本地文件 rawM3u8 与 localM3u8 各个分片长度是否相同。
     *
     * @param id 指定视频 id
     * @return true 相同 false 不相同
     */
    private boolean isLocalM3u8SameWithRawM3u8(String id) {
        String rawM3u8FilePath = getRawM3u8FilePath(id);
        String localM3u8FilePath = getLocalM3u8FilePath(id);

        if (IOUtils.exists(localM3u8FilePath)) {
            BufferedReader rawM3u8Reader = null;
            BufferedReader localM3u8Reader = null;
            try {
                rawM3u8Reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(rawM3u8FilePath), Charset.forName("utf-8")));
                localM3u8Reader = new BufferedReader(new InputStreamReader(new FileInputStream(localM3u8FilePath),
                        Charset.forName("utf-8")));

                return isM3u8TsTimesSame(rawM3u8Reader, localM3u8Reader);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (rawM3u8Reader != null) {
                    try {
                        rawM3u8Reader.close();
                    } catch (IOException ignored) {
                    }
                }
                if (localM3u8Reader != null) {
                    try {
                        localM3u8Reader.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        } else {
            return false;
        }
    }

    /**
     * 比较 lhs 与 rhs 除了 EXTINF 的下一行，其他行是否都相同。
     */
    private boolean isM3u8TsTimesSame(BufferedReader lhs, BufferedReader rhs) throws IOException {
        String lLine, rLine;
        boolean lastLineIsEXTINF = false;
        while (true) {
            lLine = lhs.readLine();
            rLine = rhs.readLine();
            if (lLine == null && rLine == null) {
                return true;
            } else if (lLine == null) {
                return false;
            } else if (rLine == null) {
                return false;
            }

            // 除了 EXTINF 的下一行，其他行需要全部相同
            if (!lastLineIsEXTINF) {
                if (!lLine.equals(rLine)) {
                    return false;
                }
            }

            lastLineIsEXTINF = lLine.startsWith("#EXTINF");
        }
    }

    /**
     * 创建本地代理文件。
     *
     * @param id 影片 id
     */
    private void createLocalM3u8File(String id) throws IOException {
        String rawM3u8FilePath = getRawM3u8FilePath(id);
        String localM3u8FilePath = getLocalM3u8FilePath(id);

        BufferedReader in = null;
        BufferedWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(rawM3u8FilePath), Charset.forName("utf-8")));
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(localM3u8FilePath), Charset.forName("utf-8")));

            String line;
            String lastLine = "";
            int i = 0;
            while ((line = in.readLine()) != null) {
                if (lastLine.startsWith("#EXTINF")) {
                    out.write(i + ".ts\n");
                    i++;
                } else {
                    out.write(line + "\n");
                }
                lastLine = line;
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private String getVideoDirPath(String id) {
        return cacheConfig.getCachePath() + File.separator + id;
    }

    private String getRawM3u8FilePath(String id) {
        return getVideoDirPath(id) + File.separator + FILE_NAME_RAW;
    }

    private String getLocalM3u8FilePath(String id) {
        return getVideoDirPath(id) + File.separator + FILE_NAME_LOCAL;
    }

    private String getMasterM3u8FilePath(String id) {
        return getVideoDirPath(id) + File.separator + FILE_NAME_MASTER;
    }

    private synchronized boolean isMasterId(String id) {
        return masterMap.containsKey(id);
    }

    private synchronized String convertMasterIdToCurSubId(String id) {
        String newId;
        MasterInfo mInfo = masterMap.get(id);
        MasterInfo.SubInfo subInfo = mInfo.getCurPlaySubInfo();
        newId = subInfo.getId();
        return newId;
    }

    private void createVideoDirIfNoExists(String id) throws IOException {
        File file = new File(getVideoDirPath(id));
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IOException("Fail to create directory " + file + ".");
            }
        }
    }

    /**
     * 估算已下载大小，取某一分片 * n，不准确。
     *
     * @param id   影片id
     * @param tses 已下载的分片序列
     * @return bytes
     */
    private int calcDownloadedBytes(String id, List<Integer> tses) {
        if (tses == null || tses.isEmpty()) {
            return 0;
        }
        return getTsFileSize(id, tses.get(0)) * tses.size();
    }

    private int calcTotalBytes(String id, List<Integer> downloadedTses, int tsCount) {
        if (downloadedTses == null || downloadedTses.isEmpty()) {
            return VideoInfo.TOTAL_BYTES_UNDEFINE;
        }
        return calcTotalBytes(id, downloadedTses.get(0), tsCount);
    }

    private int calcTotalBytes(String id, int downloadedTs, int tsCount) {
        return getTsFileSize(id, downloadedTs) * tsCount;
    }

    /**
     * 获取某一分片的大小
     *
     * @param id      影片id
     * @param tsIndex 分片 index
     * @return 分片 bytes
     */
    private int getTsFileSize(String id, int tsIndex) {
        return (int) new File(getTsFilePath(id, tsIndex)).length();
    }

    /**
     * 获得某分片绝对下载路径
     */
    public synchronized String getAbsDownloadUrl(String id, int tsIndex) {
        VideoInfo video = videoMap.get(id);
        if (video != null) {
            return video.getAbsoluteDownloadUrl(tsIndex);
        } else {
            return null;
        }
    }

    /**
     * 增加下载进度
     */
    public synchronized void addDownloadedBytes(String id, int bytes) {
        VideoInfo v = videoMap.get(id);
        if (v != null) {
            v.addDownloadedBytes(bytes);
        }
    }

    /**
     * 设置某影片下载失败
     */
    public synchronized void setDownloadError(String id) {
        VideoInfo v = videoMap.get(id);
        if (v != null) {
            v.setError();
        }
    }

    public synchronized void setPause(final String id) {
        VideoInfo v = videoMap.get(id);
        if (v != null) {
            v.setPause();
        }
    }

    public synchronized boolean setDownloading(String id) {
        VideoInfo v = videoMap.get(id);
        if (v != null) {
            v.setDownloading();
            return true;
        } else {
            return false;
        }
    }

    public synchronized void setAllDownloading() {
        for (VideoInfo v : videoMap.values()) {
            v.setDownloading();
        }
    }

    public synchronized Progress getProgress(String id) {
        VideoInfo v = videoMap.get(id);
        if (v == null) {
            return null;
        }
        IntRanges ranges = v.getDownloadedTsRanges();
        float downloadedDuration = 0;
        for (IntIntPair pair : ranges.getRanges()) {
            downloadedDuration += v.tsIndexToStartTime(pair.second) - v.tsIndexToStartTime(pair.first);
        }

        return new Progress(v.getId(), downloadedDuration, v.getDuration(), v.getDownloadedBytes(),
                v.getTotalBytes(), v.getState());
    }

    public synchronized void removeVideo(final String id) {
        VideoInfo v = videoMap.remove(id);
        if (v != null) {
            notifyAll();
        }
    }

    public synchronized Set<String> getAllIds() {
        return videoMap.keySet();
    }

    public interface ParseM3u8Callback {
        void onSuccess(int[] bandWidths);

        void onFail(Exception e);
    }
}
