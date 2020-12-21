package com.zeke.hlscache;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.kingz.mobile.libhlscache.HLSCache;
import com.kingz.mobile.libhlscache.bean.CacheVideoConfig;
import com.kingz.mobile.libhlscache.bean.OnResultListener;
import com.kingz.mobile.libhlscache.bean.Progress;

import java.io.File;
import java.util.Arrays;

/**
 * 多码率流无缝切换码率
 */
public class MultiActivity extends AppCompatActivity {
    private static final String URL = "http://192.168.90.61:5021/nn_vod.m3u8?id=f06795c5c1983cda476118877e64f25b&url_c1=636570685f3230313730342f7374617469632f766964656f2f303030303033303032303030303030303130303030303030313350524f4d49303030303037383537322f706c61796c6973742e6d33753800&nn_ak=013c0f8d928dc85c4e0faf645ef3c11fdb&npips=192.168.90.61:5201&ncmsid=10021&ngs=5a13c96b000bca9850cacaadde18a251&nft=m3u8&nn_user_id=&ndi=&ndv=&nfm=&nst=iptv&nn_core_test=senzhang&ndt=stb&nca=%26nn_cp%3dMGTV";
    private VideoView mVideoView;
    private TextView mTextView;
    private Handler mHandler = new Handler();
    private HLSCache mHLSCache;
    private static final String VIDEO_ID = "11111";
    private int[] mBandWidth;
    private int mBandWidthIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);

        mVideoView = findViewById(R.id.video_view);
        mTextView = findViewById(R.id.tv);
    }

    private void initHLSCache() {
        String path = getExternalCacheDir() + File.separator + "cache";

        mHLSCache = new HLSCache(path);

        mHLSCache.addResultListener(new OnResultListener() {
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

    private void noProxy() {
        mVideoView.setVideoURI(Uri.parse(URL));
        mVideoView.start();
    }

    private void proxy() {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Progress progress = mHLSCache.getProgress(VIDEO_ID);
                if (progress != null) {
                    mTextView.setText(progress.getDownloadedDuration() + "/" + progress.getDuration());
                }

                mHandler.postDelayed(this, 500);
            }
        });


        CacheVideoConfig config = new CacheVideoConfig(Integer.MAX_VALUE);
        mHLSCache.proxyHLSVideo(URL,
                VIDEO_ID, config, new HLSCache.ProxyHLSVideoCallback() {
                    @Override
                    public void onSuccess(final String localUrl, final int[] bandWidths) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("libhlscache", "run() called:" + localUrl + " band: " + Arrays.toString(bandWidths));
                                mVideoView.setVideoURI(Uri.parse(localUrl));
                                mVideoView.start();
                                mBandWidth = bandWidths;
                                mBandWidthIndex = 0;
                            }
                        });
                    }

                    @Override
                    public void onFail(Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public void onInitClicked(View view) {
        initHLSCache();
    }

    public void onStartClicked(View view) {
        proxy();
//        noProxy();
    }

    public void onOfflineClicked(View view) {
        String url = mHLSCache.proxyHLSVideo(VIDEO_ID);
        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.start();
    }

    /**
     * 切换码率，会延迟一会儿（播放器缓存了一部分原码率视频）
     */
    public void onSwitchClicked(View view) {
        mBandWidthIndex++;
        if (mBandWidthIndex >= mBandWidth.length) {
            mBandWidthIndex = 0;
        }
        mHLSCache.switchBandwidth(VIDEO_ID, mBandWidth[mBandWidthIndex]);
    }
}
