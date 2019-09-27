package com.kingz.play.view.controller;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.kingz.customdemo.R;
import com.kingz.play.presenter.PlayPresenter;

/**
 * author：KingZ
 * date：2019/7/31
 * description：播放视图controller的控制类
 */
public class PlayerUiSwitcher {
    private static final String TAG = PlayerUiSwitcher.class.getName();

//    private IMediaPlayer _mp;

    private PlayPresenter _presenter;
    private View rootView;
    private TopBarController topBarController;
    private BottomBarController bottomBarController;
    private LockPanelController lockPanelController;
    private CoverPanelController coverPanelController;
    private View bufferLoadView;
    private static final int SCREEN_LANSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    private static final int SCREEN_UNSPECIFIED = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    private static final int CONTROLLER_DELAY_GONE_MS = 5000;

    /**
     * 显示和关闭 各个状态栏的Runnable
     */
    private Runnable delayDismissRunnable = new Runnable() {
        @Override
        public void run() {
            showControllerBar(false,
                    topBarController,
                    lockPanelController,
                    bottomBarController);
        }
    };

    public PlayerUiSwitcher(PlayPresenter playPresenter, View view) {
        _presenter = playPresenter;
        rootView = view;
        bufferLoadView = rootView.findViewById(R.id.play_load_layout);
        topBarController = new TopBarController(view);
        bottomBarController = new BottomBarController(view);
        lockPanelController = new LockPanelController(view);
        coverPanelController = new CoverPanelController(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        rootView.setOnClickListener(listener);
        topBarController.setOnClickListener(listener);
        bottomBarController.setOnClickListener(listener);
        coverPanelController.setOnClickListener(listener);
        lockPanelController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) rootView.getContext()).setRequestedOrientation(!isLocked() ? SCREEN_LANSCAPE : SCREEN_UNSPECIFIED);
                lockPanelController.switchLockState();
                showControllerBar(!isLocked(), topBarController, bottomBarController);
            }
        });
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener seekBarChangeListener) {
        bottomBarController.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    /**
     * 切换全部蒙层的显示效果
     */
    public void switchVisibleState() {
        Log.d(TAG,"switchVisibleState()");
        if (isLocked()) {
            showControllerBar(!lockPanelController.isShown(), lockPanelController);
        } else {
            showControllerBar(!bottomBarController.isShown(), topBarController, lockPanelController, bottomBarController);
        }
        repostControllersDismissTask(true);
    }


    /**
     * 刷新显示 主要用来在转屏的时候刷新显示
     */
    public void refreshViewState() {
        if (coverPanelController.isShown()) {
            coverPanelController.show();
        }
        if (!isLocked()) {
            showControllerBar(true, topBarController, lockPanelController, bottomBarController);
        }
        repostControllersDismissTask(true);
    }

    /**
     * 是否开启定时消失指示栏
     */
    public void repostControllersDismissTask(boolean enable) {
        rootView.removeCallbacks(delayDismissRunnable);
        if (enable) {
            rootView.postDelayed(delayDismissRunnable, CONTROLLER_DELAY_GONE_MS);
        }
    }

    public boolean isLocked() {
        return lockPanelController.isLocked();
    }

    /**
     * 是否在播放
     */
    public boolean isInPlayState() {
        return bottomBarController.isInPlayState();
    }

    /**
     * 显示加载圈
     */
    public void showLoadingView(String tips) {
        updateTitle();
        coverPanelController.showLoading(tips);
    }

    public void updateTitle() {
        topBarController.setTitle("测试影片");
    }


    /**
     * 实现播放完成的view
     */
    public void showCompleteView(String tips) {
        if(isLocked()){
            lockPanelController.switchLockState();
        }
        coverPanelController.showComplete(tips);
    }

    /**
     * 播放错误的展示
     */
    public void showErrorView(String tips) {
        if(isLocked()){
            lockPanelController.switchLockState();
        }
        coverPanelController.showError(tips);
    }

    /**
     * 播放状态
     */
    public void showPlayStateView() {

        bottomBarController.showPlayState();
    }

    /**
     * 暂停状态
     */
    public void showPauseStateView() {
        bottomBarController.showPauseState();
    }

    /**
     * 播放状态的视图
     */
    public void showPlayingView() {
        Log.d(TAG,"showPlayingView()");
        bufferLoadView.setVisibility(View.GONE);
        coverPanelController.close();
        if (!isLocked()) {
            topBarController.show();
            bottomBarController.show();
        } else {
            topBarController.close();
            bottomBarController.close();
        }
        bottomBarController.setPosition(_presenter.getCurrentPosition());
        bottomBarController.setDuration(_presenter.getDuration());
        lockPanelController.show();
        repostControllersDismissTask(true);
    }


    /**
     * 更新视频播放的进度显示
     */
    public void updatePlayProgressView(boolean isDragging,int postion) {
        bottomBarController.setPosition(isDragging ? postion : _presenter.getCurrentPosition());
        bottomBarController.setDuration(_presenter.getDuration());
    }

    /**
     * 显示和关闭 各个状态栏
     */
    private void showControllerBar(boolean release, Displayable... controller) {
        for (Displayable v : controller) {
            if (release) {
                v.show();
            } else if (v.isShown()) {
                v.close();
            }
        }
    }
}
