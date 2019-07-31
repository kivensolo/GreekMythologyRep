package com.kingz.play.view.controller;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.kingz.library.player.IMediaPlayer;

/**
 * author：KingZ
 * date：2019/7/31
 * description：播放视图controller的控制类
 */
public class PlayerUiSwitcher {
    private static final String TAG = PlayerUiSwitcher.class.getName();

    private IMediaPlayer _mp;
    private View rootView;
    private TopBarController topBarController;
    private BottomBarController bottomBarController;
    private LockPanelController lockPanelController;
    private CoverPanelController coverPanelController;
    private static final int SCREEN_LANSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    private static final int SCREEN_UNSPECIFIED = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

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

    public PlayerUiSwitcher(IMediaPlayer mp, View view) {
        _mp = mp;
        rootView = view;
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
        if (isLocked()) {
            showControllerBar(!lockPanelController.isShown(), lockPanelController);
        } else {
            showControllerBar(!bottomBarController.isShown(), topBarController, lockPanelController, bottomBarController);
        }
        dismissControlbar(true);
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
        dismissControlbar(true);
    }

    /**
     * 是否开启定时消失指示栏
     */
    public void dismissControlbar(boolean enable) {
        rootView.removeCallbacks(delayDismissRunnable);
        if (enable) {
            rootView.postDelayed(delayDismissRunnable, 5000);
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
        coverPanelController.close();
        if (!isLocked()) {
            topBarController.show();
            bottomBarController.show();
        } else {
            topBarController.close();
            bottomBarController.close();
        }
        //设置左上角文字  TODO 进度条显示
        bottomBarController.setDuration(0L);
        bottomBarController.setPosition(150000L);
        lockPanelController.show();
        dismissControlbar(true);
    }


    /**
     * 更新视频播放的进度显示
     */
    public void updatePlayProgressView(boolean isDrag) {
        if (!isDrag) {
            bottomBarController.setDuration(0L);
            bottomBarController.setPosition(150000L);
        }
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
