package com.mplayer.exo_player;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.BaseActivity;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.kingz.customdemo.R;
import com.kingz.utils.ZLog;

/**
 * author：KingZ
 * date：2019/4/23
 * description：手机版本的详情页
 */

public class DetailPageActivty extends BaseActivity {

    public static final String TAG = "DetailPageActivty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detailpage);
        createPlayer();
    }

    private void createPlayer() {
        ZLog.d(TAG,"createPlayer()");
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        //输出绑定view
//        player.setVideoSurface(surface);
//        player.setVideoSurfaceView(view);
//        player.setVideoTextureView(textureView);

        PlayerView playerView = new PlayerView(this);
        playerView.setPlayer(player);

        MediaSource mediaSource = buildMediaSource(Uri.parse("https://recordcdn.quklive.com/broadcast/activity/1555981304628785/20190423/093356_29.ts"
                ),"");
        player.prepare(mediaSource);
    }

    //准备数据源
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
                return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        }
        return null;
    }


}
