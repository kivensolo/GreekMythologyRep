package com.kingz.play.view;

import android.view.View;
import com.base.IAppView;

/**
 * date：2019/7/30
 * description：播放器顶层View接口
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
     * 试看完成的提示
     */
    void showPreviewCompleteTips(String tips);

    /**
     * 需要购买时候的提示
     */
    void showVipBuyTips(String tips);

    /**
     * 更新标题
     */
    void updateTitleView();

    /**
     * 需要登陆时候的提示
     */
    void showLoginTips(String tips);

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
     * 展示当前剧集的多屏按钮
     */
    void showDLNA(String tags);

    /**
     * 切换蒙层显示状态
     */
    void switchVisibleState();

    /**
     * 更新视频播放的进度显示
     */
    void updatePlayProgressView(boolean isDrag);

    /**
     * 开启关闭定时消失蒙层
     */
    void repostControllersDismissTask(boolean enable);

    /**
     * 在蒙层显示的时候 都需要收起pop
     */
    void dismissPop();
}
