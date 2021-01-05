package com.kingz.mobile.libhlscache;

import com.kingz.mobile.libhlscache.http.AbsHttpRequester;
import com.kingz.mobile.libhlscache.utils.IOUtils;
import com.kingz.mobile.libhlscache.utils.LogUtils;
import com.kingz.mobile.libhlscache.utils.Pair;

import java.io.IOException;

/**
 * TS fragment Downloader
 */
class Downloader {
    private Thread downloadThread;
    private volatile boolean enable = true;

    private HLSCache hlsCache;
    private AbsHttpRequester httpRequester;
    private HLSModel hlsModel;

    void init(HLSCache hlsManager) {
        hlsCache = hlsManager;
        httpRequester = hlsManager.getHttpRequester();
        hlsModel = hlsManager.getHLSModel();
    }

    synchronized void checkAndStart() {
        if ((downloadThread == null || !downloadThread.isAlive()) && enable) {
            downloadThread =
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                final Pair<String, Integer> ts = hlsModel.getNextNeedDownloadTs(); // id, tsIndex
                                if (ts == null || !enable) {
                                    LogUtils.i("stop download");
                                    break;
                                }

                                try {
                                    String tsFilePath = hlsModel.getTsFilePath(ts.first, ts.second);
                                    if (!IOUtils.exists(tsFilePath)) {
                                        LogUtils.i("downloading " + ts.first + ":" + ts.second + " ts");

                                        String url = hlsModel.getAbsDownloadUrl(ts.first, ts.second);
                                        if (url != null) {
                                            httpRequester.downloadFile(url,
                                                    tsFilePath, true, new AbsHttpRequester.ProgressCallback() {
                                                        @Override
                                                        public void onRead(int bytes) {
                                                            hlsModel.addDownloadedBytes(ts.first, bytes);
                                                        }
                                                    });
                                            hlsModel.setTsDownloadFinish(ts.first, ts.second);
                                            LogUtils.i("downloaded  " + ts.first + ":" + ts.second + " ts");
                                        }
                                    } else {
                                        LogUtils.i("skip " + ts.first + ":" + ts.second + " ts");
                                    }
                                } catch (IOException e) {
                                    hlsModel.setDownloadError(ts.first);
                                    hlsCache.callbackDownloadError(ts.first, e);
                                }
                            }
                        }
                    });
            downloadThread.start();
        }
    }

    /**
     * 设置是否允许下载。
     */
    void setEnable(boolean enable) {
        this.enable = enable;
    }
}
