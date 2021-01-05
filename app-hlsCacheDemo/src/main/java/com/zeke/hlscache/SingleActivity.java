package com.zeke.hlscache;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.kingz.mobile.libhlscache.HLSCache;
import com.kingz.mobile.libhlscache.bean.CacheVideoConfig;
import com.kingz.mobile.libhlscache.bean.IDownloadCallBack;
import com.kingz.mobile.libhlscache.bean.Progress;
import com.kingz.mobile.libhlscache.bean.VideoInfo;

import java.io.File;

/**
 * 单码率流边下边播，纯下载则为只下载，不播放。
 */
public class SingleActivity extends AppCompatActivity {
    private VideoView mVideoView;
    private TextView mTextView;
    private Handler mHandler = new Handler();
    private HLSCache mHLSCache;
    private static final String VIDEO_ID = "1";
    private static final String URL = "http://120.205.22.106:5000/nn_vod/nn_x64/aWQ9YmE2YjM3OTg4NjA1NTFkMGJhZjQ2MDkyZmIwNDliZmEmdXJsX2MxPTZiNmY3YTZlNjE2YjJmNzg2OTZlNmM2MTZlNmQ3NTJmNzM2ODY5NmE2OTY1NjI2NTY5NmM3NTZlNzQ2MTZlMmY3MzY4Njk3NTZhNjk2NTYyNjU2OTZjNzU2ZTc0NjE2ZTM1MmU3NDczMDAmbm5fYWs9MDE3MmViMmJkYWE1OWY4NzUzZjI2YmRhNDYwZTZkODM4OCZudHRsPTMmbnBpcHM9MTkyLjE2OC4xNS4xMDI6NTEwMCZuY21zaWQ9MTEwMDgmbmdzPTViNDQxNDk2MzUwODNjYjBkOWE5MGI4MzYyOTA5MzQzJm5uZD1jbi56Z3lkLmFsbCZuc2Q9Y24uemd5ZC5hbGwmbmZ0PXRzJm5uX3VzZXJfaWQ9Jm5kdD1waG9uZSZuZGk9YTc2MmZjNzM0MmJlNDA0MCZuZHY9NC40LjAuYW5kcm9pZF9Lb3puYWtfcmVsZWFzZSZuc3Q9aXB0diZuYWw9MDE5NjE0NDQ1YjA2MDdlMGEwZmI3ZjhjYmQ3ZWQ1NGNhN2FhYzdlNjMwYjcyMw,,/ba6b3798860551d0baf46092fb049bfa.m3u8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        mVideoView = findViewById(R.id.video_view);
        mTextView = findViewById(R.id.tv);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController);
    }

    private void initHLSCache() {
        String path = getExternalCacheDir() + File.separator + "cache";

        mHLSCache = new HLSCache(path);

        mHLSCache.addResultListener(new IDownloadCallBack() {
            @Override
            public void onDownloadError(String id, Exception e) {
                Log.d("libhlscache", "onDownloadError: ");
            }

            @Override
            public void onDownloadFinish(String id) {
                Log.d("libhlscache", "onDownloadFinish: ");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacksAndMessages(null);
        if (mHLSCache != null) {
            mHLSCache.setEnable(false, false);
        }
    }

    /**
     * 原始网络请求，不走缓存
     */
    private void noProxy() {
        mVideoView.setVideoURI(Uri.parse(URL));
        mVideoView.start();
    }

    /**
     * 从缓存模块中取串播放
     */
    private void proxy() {
        if (mHLSCache == null) {
            Toast.makeText(this, "请先初始化", Toast.LENGTH_SHORT).show();
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Progress progress = mHLSCache.getProgress(VIDEO_ID);
                if (progress != null) {
                    mTextView.setText(
                            "缓存状态：" + toStateString(progress) + "\n" +
                                    "缓存时间：" + progress.getDownloadedDuration() + "/" + progress.getDuration() + "\n" +
                                    "缓存大小(M)：" + (progress.getDownloadedBytes() / 1024 / 1024) + "/" + (progress.getTotalBytes() / 1024 / 1024)
                    );
                }

                mHandler.postDelayed(this, 500);
            }
        });


        CacheVideoConfig config = new CacheVideoConfig(Integer.MAX_VALUE);
        mHLSCache.proxyHLSVideo(URL,
                VIDEO_ID, config, new HLSCache.ProxyHLSVideoCallback() {
                    @Override
                    public void onSuccess(final String localUrl, int[] bandWidths) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("libhlscache", "run() called:" + localUrl);
                                mVideoView.setVideoURI(Uri.parse(localUrl));
                                mVideoView.start();
                            }
                        });
                    }

                    @Override
                    public void onFail(Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private String toStateString(Progress progress) {
        switch (progress.getState()) {
            case VideoInfo.STATE_ONLINE_DOWNLOADING:
                return "下载中";
            case VideoInfo.STATE_OFFLINE:
                if (progress.isDownloadFinish()) {
                    return "下载完成";
                } else {
                    return "离线视频";
                }
            case VideoInfo.STATE_ONLINE_PAUSE:
                return "暂停";
            case VideoInfo.STATE_ONLINE_ERROR:
                return "下载错误";
            default:
                return null;
        }
    }

    public void onInitClicked(View view) {
        initHLSCache();
    }

    public void onStartClicked(View view) {
        proxy();
//        noProxy();
    }

    public void onOfflineClicked(View view) {
        if (mHLSCache == null) {
            Toast.makeText(this, "请先初始化", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = mHLSCache.proxyHLSVideo(VIDEO_ID);
        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.start();
    }
}
