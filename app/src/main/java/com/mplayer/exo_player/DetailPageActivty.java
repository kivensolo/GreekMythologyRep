package com.mplayer.exo_player;

import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceView;

import com.App;
import com.base.BaseActivity;
import com.kingz.customdemo.R;
import com.kingz.library.player.IMediaPlayer;
import com.kingz.library.player.MediaPlayerFactory;
import com.kingz.library.player.exo.ExoMediaPlayer;
import com.kingz.utils.ZLog;

/**
 * author：KingZ
 * date：2019/4/23
 * description：手机版本的详情页
 * 基于exo播放器组件
 * https://www.jianshu.com/p/6e466e112877
 */
public class DetailPageActivty extends BaseActivity {
    public static final String TAG = "DetailPageActivty";
    private IMediaPlayer player;
    private SurfaceView playerView;
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
        ZLog.d(TAG,"initializePlayer()");
        player = getMediaPlayerCore();
        //TODO 需要挪给Present
        player.setPlayerView(playerView);
//        Uri testPlayUri = Uri.parse("http://113.105.248.47/14/v/i/k/h/vikhmhifgwpksztpfxxcckpfnkxsbu/he.yinyuetai.com/AE3B0166F34C8148E6F94146DBC1BBCE.mp4");
        Uri testPlayUri = Uri.parse("http://183.60.197.33/8/w/w/k/e/wwkeazjkxmrhtvpmdfolzahahbtfua/hc.yinyuetai.com/A3B001588B7A43C2C89C0CD899FEFF76.mp4?sc=1d6a19222c007613&br=778&vid=2729951&aid=4539&area=US&vst=3");
        player.setPlayURI(testPlayUri);
    }

    private IMediaPlayer getMediaPlayerCore() {
        return MediaPlayerFactory.newInstance(App.getAppInstance(), MediaPlayerFactory.FLAG_EXO, null);
    }

    @Override
    protected void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            if(player instanceof ExoMediaPlayer){
                currentWindow = ((ExoMediaPlayer)player).getCurrentWindowIndex();
            }
            player.release();
        }
    }

}
