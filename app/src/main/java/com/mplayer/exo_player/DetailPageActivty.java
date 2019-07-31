package com.mplayer.exo_player;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.kingz.customdemo.R;
import com.kingz.play.MediaParams;
import com.kingz.play.PlayerActivity;
import com.kingz.play.fragment.PlayFragment;
import com.kingz.play.presenter.PlayPresenter;

/**
 * author：KingZ
 * date：2019/4/23
 * description：手机版本的详情页
 * 基于exo播放器组件
 */
public class DetailPageActivty extends PlayerActivity {
    public static final String TAG = "DetailPageActivty";

    private PlayFragment playFragment;
    private PlayPresenter playPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment();
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        playFragment = (PlayFragment) fm.findFragmentByTag(TAG_VOD_DETAIL);
        if (playFragment == null) {
            playFragment = PlayFragment.newInstance(new MediaParams());
            fragmentTransaction
                    .add(R.id.player_content, playFragment, TAG_VOD_DETAIL)
                    .show(playFragment);
        }
//        playPresenter = new PlayPresenter(playFragment); //p和view层关联
//        playFragment.setPresenter(playPresenter);

        // 影片详情显示
//        vodInfoFragment = (VodInfoFragment) fm.findFragmentByTag(TAG_VOD_INFO);
//        if (vodInfoFragment == null) {
//            vodInfoFragment = new VodInfoFragment();
//            vodInfoPresenter = new VodInfoPresenter(vodInfoFragment, mediaParams, playRequestManager, playEventManager);   //这样写的原因是要确保presenter在fragment的onCreate之前执行
//            fragmentTransaction.add(R.id.content_layout, vodInfoFragment, TAG_VOD_INFO);
//        } else {
//            vodInfoPresenter = new VodInfoPresenter(vodInfoFragment, mediaParams, playRequestManager, playEventManager);
//        }
//        vodInfoFragment.setPresenter(vodInfoPresenter);
//
//        fragmentTransaction.show(vodInfoFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //目前是横屏的话
            playFragment.onBackPressed();
        }
//        else if (vodDetailFragment != null && vodDetailFragment.isAdded() && vodDetailFragment.isVisible()) {
//            getSupportFragmentManager().beginTransaction().remove(vodDetailFragment).commit();
//        }
        else {
            super.onBackPressed();
        }
    }

}
