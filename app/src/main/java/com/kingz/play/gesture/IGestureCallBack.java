package com.kingz.play.gesture;

/**
 * author：KingZ
 * date：2019/9/27
 * description： 根据业务 对外封装暴露的手势回调
 */
public interface IGestureCallBack {
    /***
     * 手指在Layout左半部上下滑动时候调用，一般是亮度手势
     * 从View底部滑动到顶部，代表从0升到1
     * @param ratio：0-1 之间，1代表最亮，0代表最暗
     */
    void onGestureLeftTB(float ratio);

    /***
     * 手指在Layout右半部上下滑动时候调用，一般是音量手势
     * 从View底部滑动到顶部，代表从0升到1
     * @param ratio：0-1 之间，1代表音量最大，0代表音量最低
     */
    void onGestureRightTB(float ratio);

    /**
     * @param duration :快进快退,大于0快进，小于0快退
     */
    void onGestureUpdateVideoTime(long duration);

    /**
     * 单击手势，确认是单击的时候调用
     */
    void onGestureSingleClick();

    /**
     * 双击手势，确认是双击的时候调用，可用于播放器暂停
     */
    void onGestureDoubleClick();

    /**
     * 按下即触发
     */
    void onGestureDown();
}

