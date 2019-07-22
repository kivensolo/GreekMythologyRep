package com.mplayer.exo_player;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.BaseActivity;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.kingz.customdemo.R;
import com.kingz.utils.ZLog;

/**
 * author：KingZ
 * date：2019/4/23
 * description：手机版本的详情页
 * https://www.jianshu.com/p/6e466e112877
 */
public class DetailPageActivty extends BaseActivity {
    public static final String TAG = "DetailPageActivty";
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private ExoPlayerListener _playerLsr;
    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detailpage);
        initializePlayer();
    }

    private void initializePlayer() {
        playerView = findViewById(R.id.detail_page_playerview);
        _playerLsr = new ExoPlayerListener();
        ZLog.d(TAG,"initializePlayer()");
        if(player == null){
        ZLog.d(TAG,"player is null , create it !");
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(this), //默认渲染工厂
                    new DefaultTrackSelector(), // 默认的轨道选择器（DefaultTrackSelector）
                    new DefaultLoadControl());  // 默认的加载控制器（DefaultLoadControl）
        }
        player.addListener(_playerLsr);
        playerView.setPlayer(player);
        MediaSource mediaSource = buildMediaSource(
//                Uri.parse("https://recordcdn.quklive.com/broadcast/activity/1555981304628785/20190423/093356_29.ts"),
                Uri.parse("http://113.105.248.47/14/v/i/k/h/vikhmhifgwpksztpfxxcckpfnkxsbu/he.yinyuetai.com/AE3B0166F34C8148E6F94146DBC1BBCE.mp4"),
                "");
        player.prepare(mediaSource,true,true);
    }

    /**
     * 创建{@link MediaSource}对象
     * DASH (DashMediaSource),
     * SmoothStreaming (SsMediaSource),
     * HLS (HlsMediaSource)，
     * 其他的格式使用 (ExtractorMediaSource).
     * <a href="https://exoplayer.dev/supported-formats.html">EXOPLAYER Support Formats</a>
     *
     * MP4格式应该使用ExtractorMediaSource。
     * @param uri  播放地址的uri
     * @param overrideExtension 扩展参数
     * @return 创建的MediaSource对象
     */
    private MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {
        ZLog.d(TAG,"buildMediaSource with uri :" + uri);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, this.getApplicationInfo().name));

        int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory)
//                        .setPlaylistParser( new FilteringManifestParser<>(new HlsPlaylistParser(),
//                                            (List<RenditionKey>) getOfflineStreamKeys(uri)))
                        .createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                        createMediaSource(uri);
        }
        return null;
    }


    @Override
    protected void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.removeListener(_playerLsr);
            player.release();
            player = null;
        }
    }

}
