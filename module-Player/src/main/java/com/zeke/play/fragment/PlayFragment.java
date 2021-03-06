package com.zeke.play.fragment;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kingz.library.player.IPlayer;
import com.kingz.module.common.base.BaseActivity;
import com.kingz.module.common.base.BaseFragment;
import com.kingz.module.common.base.IPresenter;
import com.kingz.module.common.bean.MediaParams;
import com.module.tools.ScreenTools;
import com.zeke.kangaroo.utils.VolumeUtils;
import com.zeke.module_player.R;
import com.zeke.play.BasePlayPop;
import com.zeke.play.MediaPlayTool;
import com.zeke.play.PlayerGestureListener;
import com.zeke.play.gesture.IGestureCallBack;
import com.zeke.play.presenter.PlayPresenter;
import com.zeke.play.view.IPlayerView;
import com.zeke.play.view.controller.PlayerUiSwitcher;

/**
 * author：KingZ
 * date：2019/7/30
 * description：播放器Fragment
 */
public class PlayFragment extends BaseFragment implements IPlayerView {
    public static final String TAG = "PlayFragment";
    private PlayerUiSwitcher playerUiSwitcher;
    private PlayPresenter playPresenter;
    private SurfaceView playView;
    private MediaParams mediaParams;
    private BasePlayPop basePlayPop;
    private GestureDetector gestureDetector;
    private PlayerGestureListener gestureDetectorLsr;
    private static final long ORIENTATION_CHANGE_DELAY_MS = 2000L;

    public PlayFragment() {
        initGestureDetector();
    }

    public static PlayFragment newInstance(MediaParams mediaParams) {
        PlayFragment playFragment = new PlayFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MediaParams.PARAMS_KEY, mediaParams);
        playFragment.setArguments(bundle);
        return playFragment;
    }

    private void initGestureDetector(){
        gestureDetectorLsr = new PlayerGestureListener(getContext(),
                new SurfaceGustureCallback());
        gestureDetectorLsr.setVideoWH(1920 ,1080);
        gestureDetector = new GestureDetector(getContext(), gestureDetectorLsr);
    }

    @Override
    public void setPresenter(IPresenter presenter) {
        playPresenter = (PlayPresenter) presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mediaParams = getArguments().getParcelable(MediaParams.PARAMS_KEY);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.player_view_controller_basic_new;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated() {
        playView = getRootView().findViewById(R.id.play_view);
        //        playView.setOnClickListener(this);
        playView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!playerUiSwitcher.isLocked()) {
                    gestureDetector.onTouchEvent(event);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(playerUiSwitcher.isGestureViewVisible()){
                        if(playerUiSwitcher.isSeekingByGesture()){
                            long durationOffSet = playerUiSwitcher.getSeekingDurationByGesture();
                            long currentPosition = playPresenter.getCurrentPosition();
                            long postion = currentPosition + durationOffSet;
                            playPresenter.seekTo(postion);
                            playPresenter.play();
                        }
                        playerUiSwitcher.setGestureViewVisible(false);
                    }
                    if(playerUiSwitcher.isLocked()){
                        playerUiSwitcher.switchVisibleState();
                    }
                }
                return true;
            }
        });

        IPlayer mediaPlayer = MediaPlayTool.getInstance().getMediaPlayerCore();

        playPresenter = new PlayPresenter(mediaPlayer,this);
        playPresenter.setPlayParams(mediaParams);
        // TODO 根据类型初始化不同的UISwitcher
        playerUiSwitcher = new PlayerUiSwitcher(playPresenter, getRootView());
        playerUiSwitcher.setOnClickListener(this);
        playerUiSwitcher.setVideoTitle(mediaParams.getVideoName());
        playerUiSwitcher.setOnSeekBarChangeListener(playPresenter.seekBarChangeListener);
        playPresenter.onCreateView();
    }


    @Override
    public void onClick(View v) {
        repostControllersDismissTask(true);
        int id = v.getId();
        if (id == R.id.back_tv
                || id == R.id.cover_back) {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        } else if (id == R.id.fullscreen_icon
                || id == R.id.fullscreen_icon_mask) {
            switchScreenMode(true);
        } else if (id == R.id.tv_quality || id == R.id.tv_quality_cover) {
            //清晰度弹窗
//                basePlayPop = new QualityPop(getContext(), mediaParams, playPresenter.playEventManager);
//                basePlayPop.showAtLocation(getView(), Gravity.CENTER, 0, 0);
//                playerUiSwitcher.switchVisibleState();
        } else if (id == R.id.play_pause) {
            togglePlayState();
        } else if (id == R.id.play_flow_tips) {
            playPresenter.play();
        } else if (id == R.id.play_next) {
            //DO Nothing
        } else if (id == R.id.player_setting) {
            playerUiSwitcher.makeSettingViewVisible(true);
        }

        if (basePlayPop != null) {
            basePlayPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    switchVisibleState();
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
        if(playerUiSwitcher.isSettingViewShow()){
            playerUiSwitcher.makeSettingViewVisible(false);
            return true;
        }
        switchScreenMode(false);
        return false;
    }


    /**
     * 切换播放器状态
     */
    private void togglePlayState() {
        playPresenter.togglePlay();
    }

    /**
     * 切换屏幕模式
     * @param isFull 是否全屏
     * @return 是否成功切换
     */
    public boolean switchScreenMode(boolean isFull) {
        if (getMActivity() != null && !playerUiSwitcher.isLocked()) {
            final int autoRotation = Settings.System.getInt(getMActivity().getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION, 0);
            int orientation = isFull ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            getMActivity().setRequestedOrientation(orientation);
            playView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //2秒后，横竖屏在由重力感应决定
                    if (isShown() && autoRotation == 1 && !playerUiSwitcher.isLocked()) {
                        getMActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    }
                }
            }, ORIENTATION_CHANGE_DELAY_MS);
            playerUiSwitcher.refreshViewState();
            return true;
        }
        return false;
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
    public void showPlayBufferTips() {

    }

    @Override
    public void dismissPlayBufferTips() {

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

    class SurfaceGustureCallback implements IGestureCallBack {
        //seek达到结尾得保护时间 3s
        private long seekProtectTime = 3 * 1000L;
        @Override
        public void onGestureLeftTB(float ratio) {
//            ZLog.d(TAG,"onGesture LeftTB ratio=" + ratio);
            ScreenTools.setScreenBrightness(getActivity(),ratio);
            playerUiSwitcher.updateBrightness(ratio);
        }

        @Override
        public void onGestureRightTB(float ratio) {
//            ZLog.d(TAG,"onGesture RightTB ratio=" + ratio);
            VolumeUtils.setStreamMusicVolume(getContext(),ratio);
            playerUiSwitcher.updateVolume(ratio);
        }

        @Override
        public void onGestureUpdateVideoTime(long duration) {
            if(playPresenter.isPlayeing()){
                playPresenter.pause();
            }
            long time = limitSeekingTime(duration);
            playerUiSwitcher.updateSeekTimePreView(time);
        }

        @Override
        public void onGestureSingleClick() {
            playPresenter.onViewClick();
        }

        @Override
        public void onGestureDoubleClick() {
            togglePlayState();
        }

        @Override
        public void onGestureDown() {}

        /**
         * 限制seek的有效时长
         * @param duration 手势拖动的时长
         * @return 能前后seek得有效时长
         */
        private long limitSeekingTime(long duration) {
            long time;
            if(duration <= 0){ //快退
                long currentPosition = playPresenter.getCurrentPosition();
                if(currentPosition <= Math.abs(duration)){
                    time = -currentPosition;
                }else{
                    time = duration;
                }
            }else{ //快进
                long currentPosition = playPresenter.getCurrentPosition();
                long totalDuration = playPresenter.getDuration();
                long left = totalDuration - currentPosition - seekProtectTime;
                time = Math.min(duration, left);
            }
            return time;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
