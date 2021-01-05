package com.kingz.mobile.libhlscache;

import com.kingz.mobile.libhlscache.bean.CacheConfig;
import com.kingz.mobile.libhlscache.bean.CacheVideoConfig;
import com.kingz.mobile.libhlscache.bean.IDownloadCallBack;
import com.kingz.mobile.libhlscache.bean.Progress;
import com.kingz.mobile.libhlscache.bean.VideoInfo;
import com.kingz.mobile.libhlscache.http.AbsHttpRequester;
import com.kingz.mobile.libhlscache.http.HttpUrlConRequester;
import com.kingz.mobile.libhlscache.utils.LogUtils;
import com.kingz.mobile.libhlscache.utils.NanoHTTPResponseUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fi.iki.elonen.NanoHTTPD;

/**
 * 对外：通信接口，处理播放器的 HTTP 请求与 API 调用。
 * 对内：内部对象创建依赖管理。
 */
public class HLSCache {
    private List<IDownloadCallBack> listeners = new ArrayList<>();

    // 内部模块
    private NanoHTTPD nanoHTTPD;

    private final AbsHttpRequester httpRequester;
    private final Downloader downloader;
    private final HLSModel hlsModel;
    private final CacheConfig cacheConfig;

    /**
     * 创建缓存对象，通常一个 app 仅创建一个实例对象. 创建缓存模块实例时不会对上次未下载完成的视频进行继续下载，
     * 可调用 {@link #proxyHLSVideo(String, String, CacheVideoConfig, ProxyHLSVideoCallback)}
     * 继续进行下载。
     *
     * @param cachePath 缓存文件保存路径。注意：请勿放在应用 cache 路径下，会导致空间不足时被 android 系统自动删除。
     */
    public HLSCache(String cachePath) {
        cacheConfig = new CacheConfig(cachePath);
        httpRequester = new HttpUrlConRequester();
        downloader = new Downloader();
        hlsModel = new HLSModel();

        downloader.init(this);
        hlsModel.init(this);

        initNanoHTTPD();
    }

    /**
     * 添加下载过程中成功、失败监听。调用 {@link #removeResultListener(IDownloadCallBack)} 删除监听。
     * 注意：只添加监听不调用 {@link #removeResultListener(IDownloadCallBack)} 将造成 Context 上下文泄漏
     */
    public void addResultListener(IDownloadCallBack onResultListener) {
        listeners.add(onResultListener);
    }

    /**
     * 删除监听
     */
    public void removeResultListener(IDownloadCallBack onResultListener) {
        listeners.remove(onResultListener);
    }

    /**
     * 暂停下载指定影片，仅在 {@link #proxyHLSVideo(String, String, CacheVideoConfig, ProxyHLSVideoCallback)}
     * 回调成功后有效.
     *
     * @param id 影片唯一id
     */
    public void pause(String id) {
        hlsModel.setPause(id);
    }

    /**
     * 指定某部影片从错误/暂停中恢复，继续下载.
     *
     * @param id 影片 id
     * @return true 恢复成功， false 恢复失败
     */
    public boolean resumeDownload(String id) {
        if (hlsModel.setDownloading(id)) {
            downloader.checkAndStart();
            return true;
        }
        return false;
    }

    /**
     * 所有影片从错误/暂停中恢复，继续下载。
     */
    public void resumeDownloadAll() {
        hlsModel.setAllDownloading();
        downloader.checkAndStart();
    }

    /**
     * 使能控制，控制是否启动代理和下载，默认会开启。通常用于停止下载和本地代理.
     *
     * @param enableProxy    是否启动代理。
     * @param enableDownload 是否启动下载。
     */
    public void setEnable(boolean enableProxy, boolean enableDownload) {
        downloader.setEnable(enableDownload);
        if (enableProxy) {
            checkAndStartHTTPD();
        } else {
            nanoHTTPD.stop();
        }
    }

    /**
     * 获取某部影片的下载进度与下载状态。
     *
     * @param id 影片 id
     * @return 进度与状态，若无该影片则返回 null
     */
    public Progress getProgress(String id) {
        return hlsModel.getProgress(id);
    }

    /**
     * 对影片进行本地缓存代理和下载, 对已下载的视频进行继续下载. 下载完成的视频不会重新重新下载.
     * 注意：在回调返回之前，调用{@link #pause(String)} {@link #deleteCache(String)}
     * {@link #deleteCacheAll()} {@link #getProgress(String)} 等方法，将无效或无法获得正确结果。
     *
     * @param m3u8Url  m3u8 地址
     * @param id       影片唯一 id
     * @param config   影片缓存配置
     * @param callback 代理结果回调，子线程
     */
    public void proxyHLSVideo(String m3u8Url, final String id, CacheVideoConfig config, final ProxyHLSVideoCallback callback) {

        // 启动代理服务器
        checkAndStartHTTPD();

        // 处理 M3u8 文件
        hlsModel.parseM3u8(m3u8Url, id, config, new HLSModel.ParseM3u8Callback() {
            @Override
            public void onSuccess(int[] bandWidths) {
                if (callback != null) {
                    callback.onSuccess(getLocalProxyUrl(id), bandWidths);
                }
            }

            @Override
            public void onFail(Exception e) {
                if (callback != null) {
                    callback.onFail(e);
                }
            }
        });
    }

    /**
     * 切换码率，sdk 会立即切换返回给播放器的码率视频流，播放器把原始码率缓存的数据播放放完成后画面会切换到
     * 新码率.
     *
     * @param id              影片唯一id
     * @param targetBandwidth 目标带宽，在{@link #proxyHLSVideo(String, String, CacheVideoConfig, ProxyHLSVideoCallback)}
     *                        回调中可获取支持带宽
     */
    public void switchBandwidth(String id, int targetBandwidth) {
        hlsModel.switchBandwidth(id, targetBandwidth);
    }

    /**
     * 仅对本地缓存的影片进行代理播放，不进行下载。
     *
     * @param id 影片 id
     * @return 存在该影片文件则返回代理地址，否则返回 null
     */
    public String proxyHLSVideo(String id) {
        if (hlsModel.getProgress(id) != null) {
            checkAndStartHTTPD();
            return getLocalProxyUrl(id);
        } else {
            return null;
        }
    }

    /**
     * 删除指定缓存与文件
     *
     * @param id 用于区分影片的id
     */
    public void deleteCache(String id) {
        hlsModel.removeVideo(id);
        hlsModel.deleteCacheFiles(id, false);
    }

    /**
     * 删除所有缓存
     */
    public void deleteCacheAll() {
        for (String id : hlsModel.getAllIds()) {
            deleteCache(id);
        }
    }

    /**
     * 获取所有缓存的影片及其对应下载状态，下载状态参考{@link VideoInfo#STATE_ONLINE_DOWNLOADING} ...
     *
     * @return map(id, state) {@link VideoInfo#STATE_ONLINE_DOWNLOADING} ...
     */
    public Set<String> getAllIds() {
        return hlsModel.getAllIds();
    }

    void callbackDownloadError(final String id, final Exception e) {
        LogUtils.i(e.toString());
        for (IDownloadCallBack listener : listeners) {
            listener.onDownloadError(id, e);
        }
    }

    void callbackDownloadFinish(final String id) {
        LogUtils.i(id + " download finish.");
        for (IDownloadCallBack listener : listeners) {
            listener.onDownloadFinish(id);
        }
    }

    private void initNanoHTTPD() {
        nanoHTTPD = new NanoHTTPD("127.0.0.1", 0) {
            //        nanoHTTPD = new NanoHTTPD("0.0.0.0", 0) {
            @Override
            public Response serve(IHTTPSession session) {
                String uri = session.getUri();
                LogUtils.i("request:" + uri);

                if (uri.endsWith(".m3u8") || uri.endsWith(".ts")) {
                    String[] split = uri.split("/");
                    if (split.length != 3) {
                        return NanoHTTPResponseUtils.newNotFoundResponse();
                    } else {
                        String id = split[1];
                        if (uri.endsWith(".m3u8")) {
                            return serveM3u8HttpRequest(id);
                        } else {
                            try {
                                int tsIndex = Integer.parseInt(split[2].substring(0, split[2].length() - 3));
                                return serveTsHttpRequest(id, tsIndex);
                            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                                return NanoHTTPResponseUtils.newNotFoundResponse();
                            }

                        }
                    }

                } else {
                    return NanoHTTPResponseUtils.newNotFoundResponse();
                }
            }
        };
    }

    /**
     * 启动 nanohttpd
     */
    private void checkAndStartHTTPD() {
        if (!nanoHTTPD.isAlive()) {
            try {
                nanoHTTPD.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 用于处理播放器的 http 请求。
     *
     * @param id      缓存唯一id
     * @param tsIndex 分片序号
     * @return http response
     */
    private NanoHTTPD.Response serveTsHttpRequest(String id, int tsIndex) {
        try {
            File file = hlsModel.serveTsFileEnsureExists(id, tsIndex);
            return NanoHTTPResponseUtils.newFixedFileResponse(file,
                    NanoHTTPD.getMimeTypeForFile(".ts"), true);
        } catch (FileNotFoundException | InterruptedException e) {
            return NanoHTTPResponseUtils.newNotFoundResponse();
        }
    }

    private NanoHTTPD.Response serveM3u8HttpRequest(String id) {
        File file = hlsModel.serveLocalM3u8FileEnsureExists(id);
        try {
            return NanoHTTPResponseUtils.newFixedFileResponse(file,
                    NanoHTTPD.getMimeTypeForFile(".m3u8"));
        } catch (FileNotFoundException e) {
            return NanoHTTPResponseUtils.newNotFoundResponse();
        }
    }

    private String getLocalProxyUrl(String id) {
        return "http://" + nanoHTTPD.getHostname() + ":" + nanoHTTPD.getListeningPort() + "/" +
                id + "/" + HLSModel.FILE_NAME_LOCAL;
    }

    public HLSModel getHLSModel() {
        return hlsModel;
    }

    Downloader getDownloader() {
        return downloader;
    }

    AbsHttpRequester getHttpRequester() {
        return httpRequester;
    }

    CacheConfig getCacheConfig() {
        return cacheConfig;
    }

    public interface ProxyHLSVideoCallback {
        void onSuccess(String localUrl, int[] bandWidths);

        void onFail(Exception e);
    }
}
