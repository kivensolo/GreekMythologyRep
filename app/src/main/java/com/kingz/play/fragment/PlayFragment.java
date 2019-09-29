package com.kingz.play.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.kingz.play.PlayerGestureListener;
import com.kingz.play.gesture.IGestureCallBack;
import com.kingz.play.presenter.PlayPresenter;
import com.kingz.play.view.BasePlayPop;
import com.kingz.play.view.IPlayerView;
import com.kingz.play.view.controller.PlayerUiSwitcher;
import com.kingz.utils.VolumeTools;
import com.module.tools.ScreenTools;

/**
 * author：KingZ
 * date：2019/7/30
 * description：播放器Fragment
 */
public class PlayFragment extends BaseFragment implements IPlayerView{
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
        bundle.putSerializable("MediaParams", mediaParams);
        playFragment.setArguments(bundle);
        return playFragment;
    }

    private void initGestureDetector(){
        gestureDetectorLsr = new PlayerGestureListener(getContext(),
                new FragmentGustureCallback());
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

        playPresenter = new PlayPresenter(mediaPlayer,this);
        playerUiSwitcher = new PlayerUiSwitcher(playPresenter, rootView);
        playerUiSwitcher.setOnClickListener(this);
        playerUiSwitcher.setOnSeekBarChangeListener(playPresenter.seekBarChangeListener);
        playPresenter.onCreateView();
        return rootView;
    }

    public boolean onTouchEvent(MotionEvent event){
        gestureDetector.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(playerUiSwitcher.isSeekingByGesture()){
                long durationOffSet = playerUiSwitcher.switchSeekPreviewState();
                long currentPosition = playPresenter.getCurrentPosition();
                long postion = currentPosition + durationOffSet;
                playPresenter.seekTo(postion);
            }
        }
        return true;
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
            case R.id.fullscreen_icon:
            case R.id.fullscreen_icon_mask:
                switchScreenMode(true);
                break;
            case R.id.tv_quality:
            case R.id.tv_quality_cover:
                //清晰度弹窗
//                basePlayPop = new QualityPop(getContext(), mediaParams, playPresenter.playEventManager);
//                basePlayPop.showAtLocation(getView(), Gravity.CENTER, 0, 0);
//                playerUiSwitcher.switchVisibleState();
                break;
            case R.id.play_pause:
                togglePlayState();
                break;
            case R.id.play_flow_tips:
                playPresenter.play();
                break;
            case R.id.play_next:
                //DO Nothing
                break;
            default:
                playPresenter.onViewClick();
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
        if (mActivity != null && !playerUiSwitcher.isLocked()) {
            final int autoRotation = Settings.System.getInt(mActivity.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION, 0);
            int orientation = isFull ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            mActivity.setRequestedOrientation(orientation);
            playView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //2秒后，横竖屏在由重力感应决定
                    if (isShown() && autoRotation == 1 && !playerUiSwitcher.isLocked()) {
                        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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

    class FragmentGustureCallback implements IGestureCallBack {
        @Override
        public void onGestureLeftTB(float ratio) {
//            ZLog.d(TAG,"onGesture LeftTB ratio=" + ratio);
            ScreenTools.setScreenBrightness(getActivity(),ratio);
        }

        @Override
        public void onGestureRightTB(float ratio) {
//            ZLog.d(TAG,"onGesture RightTB ratio=" + ratio);
            VolumeTools.setStreamMusicVolume(getContext(),ratio);
        }

        @Override
        public void onGestureUpdateVideoTime(int duration) {
            playerUiSwitcher.updateSeekTimePreView(duration);
        }

        @Override
        public void onGestureSingleClick() {
//            使用单击事件的时候，会触发全部view切换状态的情况  用onclick做兼容则不会
//            playPresenter.onViewClick();
        }

        @Override
        public void onGestureDoubleClick() {
            togglePlayState();
        }

        @Override
        public void onGestureDown() {}
    }
}
