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
    private static final String TAG = PlayerUiSwitcher.class.getSimpleName();

//    private IMediaPlayer _mp;

    private PlayPresenter _presenter;
    private View rootView;
    private TopBarController topBarController;
    private BottomBarController bottomBarController;
    private LockPanelController lockPanelController;
    private CoverPanelController coverPanelController;
    private GestureViewController gestureViewController;
    private SettingPanelController settingPanelController;

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
        gestureViewController = new GestureViewController(view);
        settingPanelController = new SettingPanelController(view);
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
        Log.d(TAG, "switchVisibleState()");
        if (isLocked()) {
            showControllerBar(!lockPanelController.isShown(), lockPanelController);
        } else {
            showControllerBar(!bottomBarController.isShown(), topBarController, lockPanelController, bottomBarController);
        }
        repostControllersDismissTask(true);
    }

    /**
     * 获取seeking总时长
     */
    public long getSeekingDurationByGesture() {
        return gestureViewController.getDurationOffset();
    }

    public void setGestureViewVisible(boolean display) {
        showControllerBar(display, gestureViewController);
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
            showVideoTitle();
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
     * 手势交互界面是否Visible
     */
    public boolean isGestureViewVisible() {
        return gestureViewController.isShown();
    }

    /**
     * 是否通过手势在Seeking
     */
    public boolean isSeekingByGesture() {
        return gestureViewController.isInSeekingControl();
    }

    /**
     * 是否在播放
     */
    public boolean isInPlayState() {
        //TODO 优化  不能按照播放按钮是否是checked来判断，要根据播放器
        return bottomBarController.isInPlayState();
    }

    /**
     * 显示加载圈
     */
    public void showLoadingView(String tips) {
        showVideoTitle();
        coverPanelController.showLoading(tips);
    }

    public void showVideoTitle() {
        topBarController.show();
    }

    public void setVideoTitle(String name) {
        topBarController.setTitle(name);
    }

    /**
     * 实现播放完成的view
     */
    public void showCompleteView(String tips) {
        if (isLocked()) {
            lockPanelController.switchLockState();
        }
        coverPanelController.showComplete(tips);
    }

    /**
     * 播放错误的展示
     */
    public void showErrorView(String tips) {
        if (isLocked()) {
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
        Log.d(TAG, "showPlayingView()");
        bufferLoadView.setVisibility(View.GONE);
        showControllerBar(false, coverPanelController, gestureViewController);
        showControllerBar(!isLocked(), topBarController, bottomBarController);
        bottomBarController.setPosition(_presenter.getCurrentPosition());
        bottomBarController.setDuration(_presenter.getDuration());
        lockPanelController.show();
        repostControllersDismissTask(true);
    }

    public void makeSettingViewVisible(boolean visible) {
        Log.d(TAG, "makeSettingViewVisible()" + visible);
        if (visible) {
            bufferLoadView.setVisibility(View.GONE);
            showControllerBar(false, topBarController,
                    lockPanelController,
                    bottomBarController,
                    gestureViewController);
            showControllerBar(true, settingPanelController);
            repostControllersDismissTask(false);
        } else {
            showControllerBar(false, settingPanelController);
        }

    }

    public boolean isSettingViewShow() {
        return settingPanelController.isShown();
    }

    /**
     * 更新视频播放的进度显示
     */
    public void updatePlayProgressView(boolean isDragging, int postion) {
        bottomBarController.setPosition(isDragging ? postion : _presenter.getCurrentPosition());
        bottomBarController.setDuration(_presenter.getDuration());
    }

    /**
     * 更新seek的预览效果
     *
     * @param duration 松手时需要seek的时长.
     */
    public void updateSeekTimePreView(long duration) {
        if (!gestureViewController.isShown()) {
            gestureViewController.show();
        }
        gestureViewController.setDuration(duration);
    }

    /**
     * 更新亮度
     *
     * @param ratio
     */
    public void updateBrightness(float ratio) {
        if (!gestureViewController.isShown()) {
            gestureViewController.show();
        }
        gestureViewController.setBrightness(ratio);
    }

    /**
     * 更新音量
     *
     * @param ratio
     */
    public void updateVolume(float ratio) {
        if (!gestureViewController.isShown()) {
            gestureViewController.show();
        }
        gestureViewController.setVolume(ratio);
    }


    /**
     * 显示和关闭 各个状态栏
     */
    private void showControllerBar(boolean display, Displayable... controller) {
        for (Displayable v : controller) {
            if (display) {
                v.show();
            } else if (v.isShown()) {
                v.close();
            }
        }
    }
}
