package com.kingz.play;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kingz.play.fragment.PlayFragment;
import com.kingz.play.fragment.VodDetailFragment;
import com.kingz.play.presenter.PlayPresenter;

/**
 * Copyright (c) 2015, 北京视达科科技有限责任公司 All rights reserved.
 * author：KingZ
 * date：2019/7/30
 * description：
 */

public class VodActivity extends PlayerActivity {

    private PlayFragment playFragment;
    private VodDetailFragment vodDetailFragment;
    private PlayPresenter playPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment();
    }

    private void initFragment() {
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        playFragment = (PlayFragment) fm.findFragmentByTag(TAG_VOD_PLAY);
//        if (playFragment == null) {
//            playFragment = PlayFragment.newInstance(mediaParams);
//            fragmentTransaction.add(R.id.player_content, playFragment, TAG_VOD_PLAY).show(playFragment);
//        }
//        playPresenter = new PlayPresenter(playFragment, mediaParams, playRequestManager, playEventManager);
//        playFragment.setPresenter(playPresenter);
//
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
//        fragmentTransaction.commit();
    }


}
