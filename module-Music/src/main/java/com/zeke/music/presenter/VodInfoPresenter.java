package com.zeke.music.presenter;

import android.media.TimedText;

import com.kingz.library.player.IPlayer;
import com.kingz.library.player.IPlayerEventsListener;
import com.zeke.music.fragments.VodInfoFragment;
import com.zeke.play.presenter.AbsBasePresenter;

import org.jetbrains.annotations.NotNull;

/**
 * author：KingZ
 * date：2019/8/7
 * description：影片信息Presenter  用于处理影片信息获取业务
 */

public class VodInfoPresenter extends AbsBasePresenter implements IPlayerEventsListener {
    private VodInfoFragment vodInfoFragment;

    public VodInfoPresenter(VodInfoFragment vodInfoFragment) {
        this.vodInfoFragment = vodInfoFragment;
    }

    @Override
    public void onCreate() {
        //
    }

    @Override
    public void onCreateView() {
        vodInfoFragment.showLoading();
    }

    @Override
    public void onPrepared(IPlayer player) {

    }

    @Override
    public boolean onError(IPlayer player,int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(IPlayer player) {

    }

    @Override
    public void onSeekComplete(IPlayer player) {

    }

    @Override
    public void onPlayerTimingUpdate() {

    }

    @Override
    public boolean onInfo(IPlayer player, int what, int extra) {
        return false;
    }

    @Override
    public void onTimedText(TimedText text) {

    }

    @Override
    public void onViewCreated() {

    }

    @Override
    public void onViewChanged(int format, int width, int height) {

    }

    @Override
    public void onViewDestroyed() {

    }

    @Override
    public void onPrepareTimeout(@NotNull IPlayer xmp) {

    }

    @Override
    public void onBuffering(@NotNull IPlayer xmp, boolean buffering, float percentage) {

    }

    @Override
    public void onBufferTimeout(@NotNull IPlayer xmp) {

    }

    @Override
    public void onSeekComplete(@NotNull IPlayer xmp, long pos) {

    }

    @Override
    public void onVideoSizeChanged(@NotNull IPlayer player, int mVideoWidth, int mVideoHeight) {

    }

    @Override
    public void onVideoFirstFrameShow(@NotNull IPlayer player) {

    }
}
