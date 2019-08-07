package com.kingz.play.presenter;

import android.media.TimedText;

import com.kingz.library.player.IMediaPlayer;
import com.kingz.library.player.IMediaPlayerCallBack;
import com.kingz.play.fragment.VodInfoFragment;

/**
 * author：KingZ
 * date：2019/8/7
 * description：影片信息Presenter  用于处理影片信息获取业务
 */

public class VodInfoPresenter extends AbsBasePresenter implements IMediaPlayerCallBack {
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
    public void onPrepared(IMediaPlayer player) {

    }

    @Override
    public void onPlay(IMediaPlayer player) {

    }

    @Override
    public boolean onError(IMediaPlayer player, int what, int extra) {
        return false;
    }

    @Override
    public void onBufferStart(IMediaPlayer player) {

    }

    @Override
    public void onBufferEnd(IMediaPlayer player) {

    }

    @Override
    public void onBufferingUpdate(IMediaPlayer player, int percent) {

    }

    @Override
    public void onCompletion(IMediaPlayer player) {

    }

    @Override
    public void onSeekComplete(IMediaPlayer player) {

    }

    @Override
    public void onPlayerTimingUpdate() {

    }

    @Override
    public boolean onInfo(IMediaPlayer player, int what, int extra) {
        return false;
    }

    @Override
    public void onTimedText(IMediaPlayer player, TimedText text) {

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
}
