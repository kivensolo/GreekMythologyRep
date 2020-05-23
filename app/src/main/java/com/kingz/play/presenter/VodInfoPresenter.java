package com.kingz.play.presenter;

import android.media.TimedText;

import com.kingz.library.player.IPlayerEventsCallBack;
import com.kingz.play.fragment.VodInfoFragment;

/**
 * author：KingZ
 * date：2019/8/7
 * description：影片信息Presenter  用于处理影片信息获取业务
 */

public class VodInfoPresenter extends AbsBasePresenter implements IPlayerEventsCallBack {
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
    public void onPrepared() {

    }

    @Override
    public void onPlay() {

    }

    @Override
    public boolean onError(int what, int extra) {
        return false;
    }

    @Override
    public void onBufferStart() {

    }

    @Override
    public void onBufferEnd() {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onCompletion() {

    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public void onPlayerTimingUpdate() {

    }

    @Override
    public boolean onInfo(int what, int extra) {
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
}
