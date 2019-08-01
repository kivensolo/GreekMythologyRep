package com.kingz.play.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.base.BaseActivity;
import com.base.BaseFragment;
import com.base.IPresenter;
import com.kingz.customdemo.R;
import com.kingz.library.player.IMediaPlayer;
import com.kingz.play.MediaParams;
import com.kingz.play.MediaPlayTool;
import com.kingz.play.presenter.PlayPresenter;
import com.kingz.play.view.BasePlayPop;
import com.kingz.play.view.IPlayerView;
import com.kingz.play.view.controller.PlayerUiSwitcher;

/**
 * author：KingZ
 * date：2019/7/30
 * description：播放器Fragment
 */
public class PlayFragment extends BaseFragment implements IPlayerView{
    private PlayerUiSwitcher playerUiSwitcher;
    private PlayPresenter playPresenter;
    private SurfaceView playView;
    private MediaParams mediaParams;
    private BasePlayPop basePlayPop;
    private static final long ORIENTATION_CHANGE_DELAY_MS = 2000L;

    public static PlayFragment newInstance(MediaParams mediaParams) {
        PlayFragment playFragment = new PlayFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MediaParams", mediaParams);
        playFragment.setArguments(bundle);
        return playFragment;
    }

    @Override
    public void setPresenter(IPresenter presenter) {
        playPresenter = (PlayPresenter) presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mediaParams = (MediaParams) getArguments().getSerializable("MediaParams");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.player_view_controller_basic_new, container, false);
        playView = rootView.findViewById(R.id.play_view);
        playView.setOnClickListener(this);

        IMediaPlayer mediaPlayer = MediaPlayTool.getInstance().getMediaPlayerCore();

        playerUiSwitcher = new PlayerUiSwitcher(mediaPlayer,rootView);
        playerUiSwitcher.setOnClickListener(this);

        playPresenter = new PlayPresenter(mediaPlayer,this); // p和view层关联
        playPresenter.onCreateView();
        playerUiSwitcher.setOnSeekBarChangeListener(playPresenter.seekBarChangeListener);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        repostControllersDismissTask(true);
        switch (v.getId()) {
            case R.id.back_tv:
            case R.id.cover_back:
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
                break;
            case R.id.img_fullscreen:
            case R.id.img_fullscreen_cover:
                //TODO 横屏后播放
                switchOrientation(true);
                break;
            case R.id.tv_quality:
            case R.id.tv_quality_cover:
                //清晰度弹窗
//                basePlayPop = new QualityPop(getContext(), mediaParams, playPresenter.playEventManager);
//                basePlayPop.showAtLocation(getView(), Gravity.CENTER, 0, 0);
//                playerUiSwitcher.switchVisibleState();
                break;
            case R.id.play_pause:
                if (playerUiSwitcher.isInPlayState()) {
                    playPresenter.pause();
                } else {
                    playPresenter.play();
                }
                break;
            case R.id.play_flow_tips:
                playPresenter.play();
                break;
            case R.id.play_next:
                //DO Nothing
                break;
            default:
                playPresenter.onViewClick(v);
        }

        if (basePlayPop != null) {
            basePlayPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    playerUiSwitcher.switchVisibleState();
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        playPresenter.onPause();
    }

    @Override
    public void onDestroy() {
        if (basePlayPop != null) {
            basePlayPop.dismiss();
        }
        playerUiSwitcher.repostControllersDismissTask(false);
        playPresenter.onDestroyView();
        super.onDestroy();
    }

    @Override
    public boolean onBackPressed() {
        return switchOrientation(false);
    }

    public boolean switchOrientation(boolean isLand) {
        if (mActivity != null && !playerUiSwitcher.isLocked()) {
            final int autoRotation = Settings.System.getInt(mActivity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
            mActivity.setRequestedOrientation(isLand ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //2秒，横竖屏默认
            playView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isShown() && autoRotation == 1 && !playerUiSwitcher.isLocked()) {
                        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    }
                }
            }, ORIENTATION_CHANGE_DELAY_MS);
            return false;
        }
        return true;
    }

    @Override
    public void showLoading() {}

    @Override
    public void hideLoading() {}

    @Override
    public void showError(View.OnClickListener listener) {}

    @Override
    public void showEmpty(View.OnClickListener listener) {}

    @Override
    public boolean isShown() {
        return getActivity() != null && ((BaseActivity) getActivity()).isActivityShow() && isVisible();
    }

    @Override
    public void showMessage(String tips) {

    }

    @Override
    public View getPlayView() {
        return playView;
    }

    @Override
    public void showPlayLoadingTips(String tips) {
        dismissPop();
        playerUiSwitcher.showLoadingView(tips);
    }

    @Override
    public void showPlayingView() {
        playerUiSwitcher.showPlayingView();
    }

    @Override
    public void showPlayCompleteTips(String tips) {
        dismissPop();
        playerUiSwitcher.showCompleteView(tips);
    }

    @Override
    public void showPlayErrorTips(String tips) {
        dismissPop();
        playerUiSwitcher.showErrorView(tips);
    }

    @Override
    public void updateTitleView() {

    }


    @Override
    public void showPlayStateView() {
        playerUiSwitcher.showPlayStateView();
    }

    @Override
    public void showPauseStateView() {
        playerUiSwitcher.showPauseStateView();
    }

    @Override
    public void showFlowTipsView() {

    }

    @Override
    public void switchVisibleState() {
        playerUiSwitcher.switchVisibleState();
    }

    //TODO 更新进度条
    @Override
    public void updatePlayProgressView(boolean isDrag,int postion) {
        playerUiSwitcher.updatePlayProgressView(isDrag,postion);
    }

    @Override
    public void repostControllersDismissTask(boolean enable) {
        playerUiSwitcher.repostControllersDismissTask(enable);
    }

    @Override
    public void dismissPop() {
        if (basePlayPop != null) {
            basePlayPop.dismiss();
        }
    }

    public PlayerUiSwitcher getControllerViewManager(){
        return playerUiSwitcher;
    }
}
