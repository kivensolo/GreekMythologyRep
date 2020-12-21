package com.kingz.mobile.libhlscache;

import com.kingz.mobile.libhlscache.http.AbsHttpRequester;
import com.kingz.mobile.libhlscache.utils.IOUtils;
import com.kingz.mobile.libhlscache.utils.LogUtils;
import com.kingz.mobile.libhlscache.utils.StringIntPair;

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
                                final StringIntPair ts = hlsModel.getNextNeedDownloadTs(); // id, tsIndex
                                if (ts == null || !enable) {
                                    LogUtils.i("stop download");
                                    break;
                                }

                                try {
                                    String tsFilePath = hlsModel.getTsFilePath(ts.str, ts.i);
                                    if (!IOUtils.exists(tsFilePath)) {
                                        LogUtils.i("downloading " + ts.str + ":" + ts.i + " ts");

                                        String url = hlsModel.getAbsDownloadUrl(ts.str, ts.i);
                                        if (url != null) {
                                            httpRequester.downloadFile(url,
                                                    tsFilePath, true, new AbsHttpRequester.ProgressCallback() {
                                                        @Override
                                                        public void onRead(int bytes) {
                                                            hlsModel.addDownloadedBytes(ts.str, bytes);
                                                        }
                                                    });
                                            hlsModel.setTsDownloadFinish(ts.str, ts.i);
                                            LogUtils.i("downloaded  " + ts.str + ":" + ts.i + " ts");
                                        }
                                    } else {
                                        LogUtils.i("skip " + ts.str + ":" + ts.i + " ts");
                                    }
                                } catch (IOException e) {
                                    hlsModel.setDownloadError(ts.str);
                                    hlsCache.callbackDownloadError(ts.str, e);
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
