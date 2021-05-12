package com.zeke.play.activities;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingz.module.common.CommonApp;
import com.kingz.module.common.bean.MediaParams;
import com.zeke.module_player.R;
import com.zeke.play.PlayerActivity;
import com.zeke.play.VideoInfo;
import com.zeke.play.fragment.PlayFragment;
import com.zeke.play.fragment.VodDetailFragment;
import com.zeke.play.fragment.VodInfoFragment;
import com.zeke.play.presenter.VodInfoPresenter;

import java.util.concurrent.TimeUnit;

/**
 * author：KingZ
 * date：2019/4/23
 * description：手机版本的详情页
 * 基于exo播放器组件
 */
@Route(path = "/module_MPlayer/detailPage")
public class DetailPageActivty extends PlayerActivity {
    public static final String TAG = "DetailPageActivty";

    private PlayFragment playFragment;
    private VodInfoFragment vodInfoFragment;
    private VodInfoPresenter vodInfoPresenter;
    //影片详情介绍的Fragment
    private VodDetailFragment vodDetailFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            initFragment();
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.detail_page;
    }

    /**
     * 初始化横竖屏
     */
    @Override
    public void initRotation() {
        //保存默认竖屏参数
        portraitParams = (LinearLayout.LayoutParams) findViewById(R.id.player_content).getLayoutParams();

        final int autoRotation = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        CommonApp.Companion.getInstance().postDelayToMainLooper(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && autoRotation == 1) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
            }
        }, TimeUnit.SECONDS.toMillis(2));
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // 播放区域
        playFragment = (PlayFragment) fm.findFragmentByTag(TAG_VOD_PLAY);
        if (playFragment == null) {
            // 嫦娥探月
            // http://video.chinanews.com/flv/2019/04/23/400/111773_web.mp4
            MediaParams mediaParams = new MediaParams();
            mediaParams.setVideoUrl("https://vfx.mtime.cn/Video/2020/05/14/mp4/200514070325395613_1080.mp4");
            playFragment = PlayFragment.newInstance(mediaParams);
            fragmentTransaction.add(R.id.player_content, playFragment, TAG_VOD_PLAY);
        }

        // 影片信息区域
        vodInfoFragment = (VodInfoFragment) fm.findFragmentByTag(TAG_VOD_INFO);
        if (vodInfoFragment == null) {
            vodInfoFragment = new VodInfoFragment();
            fragmentTransaction.add(R.id.content_layout, vodInfoFragment, TAG_VOD_INFO);
        } else {
            vodInfoPresenter = new VodInfoPresenter(vodInfoFragment);
        }
        fragmentTransaction.show(vodInfoFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        playFragment.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //目前是横屏的话
            playFragment.onBackPressed();
        } else if (vodDetailFragment != null && vodDetailFragment.isAdded() && vodDetailFragment.isVisible()) {
            // 隐藏影片详情简介fragment
            getSupportFragmentManager().beginTransaction().remove(vodDetailFragment).commit();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 展示&收起 详情页
     * @param isShow    是否显示
     */
    public void showOrDismissVideoDetail(boolean isShow, VideoInfo videoInfo) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        vodDetailFragment = (VodDetailFragment) fm.findFragmentByTag(TAG_VOD_DETAIL);
        if (vodDetailFragment == null) {
            vodDetailFragment = VodDetailFragment.newInstance(videoInfo);
        }
        if (isShow) {
            if (!vodDetailFragment.isAdded()) {
                fragmentTransaction.add(R.id.content_layout, vodDetailFragment, TAG_VOD_DETAIL);
            }
            fragmentTransaction.show(vodDetailFragment);
        } else {
            fragmentTransaction.remove(vodDetailFragment);
        }
        fragmentTransaction.commit();
    }


}
