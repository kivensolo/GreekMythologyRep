package com.kingz.play.fragment;

import android.view.View;

import com.base.BaseFragment;
import com.base.IPresenter;
import com.kingz.play.view.IPlayerView;

/**
 * author：KingZ
 * date：2019/7/30
 * description：播放器Fragment
 */
public class PlayFragment extends BaseFragment implements IPlayerView, View.OnClickListener {

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(View.OnClickListener listener) {

    }

    @Override
    public void showEmpty(View.OnClickListener listener) {

    }

    @Override
    public boolean isShown() {
        return false;
    }

    @Override
    public void showMessage(String tips) {

    }

    @Override
    public void setPresenter(IPresenter presenter) {

    }

    @Override
    public View getPlayView() {
        return null;
    }

    @Override
    public void showPlayLoadingTips(String tips) {

    }

    @Override
    public void showPlayingView() {

    }

    @Override
    public void showPlayBufferTips() {

    }

    @Override
    public void dismissPlayBufferTips() {

    }

    @Override
    public void showPlayCompleteTips(String tips) {

    }

    @Override
    public void showPlayErrorTips(String tips) {

    }

    @Override
    public void showPreviewCompleteTips(String tips) {

    }

    @Override
    public void showVipBuyTips(String tips) {

    }

    @Override
    public void updateTitleView() {

    }

    @Override
    public void showLoginTips(String tips) {

    }

    @Override
    public void showPlayStateView() {

    }

    @Override
    public void showPauseStateView() {

    }

    @Override
    public void showFlowTipsView() {

    }

    @Override
    public void showDLNA(String tags) {

    }

    @Override
    public void switchVisibleState() {

    }

    @Override
    public void updatePlayProgressView(boolean isDrag) {

    }

    @Override
    public void enableDelayDismiss(boolean isDelayDismiss) {

    }

    @Override
    public void dismissPop() {

    }
}
