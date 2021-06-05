package com.zeke.play.view;

import android.view.View;

import com.kingz.module.common.base.IAppView;

/**
 * date：2019/7/30
 * description：播放器顶层View接口
 * TODO 把IPLayerView和IAppView隔离开
 */

public interface IPlayerView extends IAppView,View.OnClickListener {
    /**
     * 获取播放视图
     */
    View getPlayView();

    void showPlayLoadingTips(String tips);

    /**
     * 展示正在播放时候的UI
     */
    void showPlayingView();

    /**
     * 网络卡顿时候的提示
     */
    void showPlayBufferTips();

    /**
     * 取消网络卡顿时候的提示
     */
    void dismissPlayBufferTips();

    /**
     * 播放完成之后的显示
     */
    void showPlayCompleteTips(String tips);

    /**
     * 播放出错时候的显示
     */
    void showPlayErrorTips(String tips);


    /**
     * 更新标题
     */
    void updateTitleView();

    /**
     * 播放状态的展示
     */
    void showPlayStateView();

    /**
     * 暂停状态的展示
     */
    void showPauseStateView();

    /**
     * 流量提示的展示
     */
    void showFlowTipsView();

    /**
     * 切换蒙层显示状态
     */
    void switchVisibleState();

    /**
     * 更新视频播放的进度显示
     * @param isDrag    是否拖动中
     * @param postion   当前进度位置  isDrag为false的时候，参数无效
     */
    void updatePlayProgressView(boolean isDrag,int postion);

    /**
     * 开启关闭定时消失蒙层
     */
    void repostControllersDismissTask(boolean enable);

    /**
     * 在蒙层显示的时候 都需要收起pop
     */
    void dismissPop();
}
