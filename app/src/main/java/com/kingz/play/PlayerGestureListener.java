package com.kingz.play;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.kingz.play.gesture.IGestureCallBack;
import com.zeke.kangaroo.utils.ScreenDisplayUtils;
import com.zeke.kangaroo.utils.VolumeUtils;
import com.zeke.kangaroo.utils.ZLog;

import java.util.concurrent.TimeUnit;

/**
 * author：KingZ
 * date：2019/9/27
 * description： 手势监听器
 * 支持：
 *  左&右侧上下滑动
 *  整体水平滑动
 */
public class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener {
    public static final String TAG = "PlayerGestureListener";
    private IGestureCallBack listener;
    private int centerW;
    //播放器View的长宽DP
    private float dpVideoWidth, dpVideoHeight;
    private ScrollMode scrollMode = ScrollMode.NONE;
    private long timeStamp;
    private int mTouchSlop;
    //快进的速率，速度越快，值越大.默认为1
    private int scrollRatio = 1;
    //每一dp，快进的时长,ms
    private int preDpVideoDuration = 0;
    private long totalDuration = 0;     //单次快进快退累计值
    private float brightnessValue = 0; // 亮度值
    private float volumeValue = 0;     // 声音值
    private float density;
    private Context context;

    public PlayerGestureListener(Context context, IGestureCallBack listener) {
        this.listener = listener;
        this.context = context;
        int screenWidth = ScreenDisplayUtils.getScreenWidth(context);
        centerW = screenWidth / 2;
        ZLog.d(TAG,"screen Width="+ screenWidth);
        mTouchSlop =ViewConfiguration.get(context).getScaledTouchSlop();
        density = context.getResources().getDisplayMetrics().density;

        brightnessValue = ScreenDisplayUtils.getScreenBrightnessValue(context) / 255f;
        volumeValue = VolumeUtils.getCurrentVolumePrecent(context);

        //默认的视频宽高dp值
        dpVideoWidth = 1920 / density;
        dpVideoHeight = 1080 / density;
    }

    /**
     * 设置播放器SurfaceView的宽高
     * @param w 宽度  像素值
     * @param h 高度  像素值
     */
    public void setVideoWH(int w, int h) {
        ZLog.d(TAG,"setVideoWH w="+w+" ;h="+h);
        //计算出视频的宽高dp值
        dpVideoWidth = w / density;
        dpVideoHeight = h / density;

        //默认基础总共2分钟(scrollRatio=1的情况下)，代表的意义：从屏幕一边滑动到另一边，总共可以快进2分钟
        preDpVideoDuration = (int) (TimeUnit.MINUTES.toMillis(2) / dpVideoWidth);
        ZLog.d(TAG,"setVideoWH dpVideoWidth="+dpVideoWidth+" ;dpVideoHeight="+dpVideoHeight);
    }

    /**
     * 自定义设置单次手势快进快退最大时间值,默认为2min
     * @param maxDuration ms
     */
    public void setSeekMaxDuration(int maxDuration){
        preDpVideoDuration = (int) (maxDuration / dpVideoWidth);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        scrollMode = ScrollMode.NONE;
        timeStamp = System.currentTimeMillis();
        totalDuration = 0;
        volumeValue = VolumeUtils.getCurrentVolumePrecent(this.context);
        if (listener != null) {
            listener.onGestureDown();
        }
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return super.onSingleTapUp(e);
    }

    @Override
    public void onShowPress(MotionEvent e) {
        super.onShowPress(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        long time = System.currentTimeMillis();
        distanceX = -distanceX;
        distanceY += distanceY;
        float dpX = distanceX / density;    // 横向滑动的dp值
        float dpY = distanceY / density;    // 垂直方向滑动的dp值
//        updateScrollRatio(dpX, time - timeStamp);
        timeStamp = time;
        float xDiff = e2.getX() - e1.getX();
        float yDiff = e2.getY() - e1.getY();
        if (scrollMode == ScrollMode.NONE) {
            //横向滑动
            if (Math.abs(xDiff) > mTouchSlop) {
                scrollMode = ScrollMode.HORIZONTAL_S;
                updateVideoTime((int) (preDpVideoDuration * xDiff));
            }
            //纵向滑动
            else if (Math.abs(yDiff) > mTouchSlop) {
                if (e1.getX() < centerW) {
                    scrollMode = ScrollMode.LEFT_TB;
                } else {
                    scrollMode = ScrollMode.RIGHT_TB;
                }
            }
        }
        //快进快退
        else if (scrollMode == ScrollMode.HORIZONTAL_S) {
            updateVideoTime((int) (preDpVideoDuration * scrollRatio * dpX));
        } else if (scrollMode == ScrollMode.LEFT_TB) {
            updateVideoLeftTB(dpY / dpVideoHeight); // y方向滑动距离比例即为亮度比例
        } else if (scrollMode == ScrollMode.RIGHT_TB) {
            updateVideoRightTB(dpY / dpVideoHeight);
        }
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (listener != null) {
            listener.onGestureDoubleClick();
        }
        //双击事件
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return super.onDoubleTapEvent(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (listener != null) {
            listener.onGestureSingleClick();
        }
        //单击事件，在双击事件发生时不会产生这个事件，所以用这个回调作为播放器单击事件
        return super.onSingleTapConfirmed(e);
    }

    /***
     * 根据滑动速度更新速度比率值，这样可以在滑动速率较快时，
     * 快进的速度也会变快，可以根据需要调整。
     * 根据经验，正常速度一般在10~40dp/s
     * @param dpX：  横向滑动距离 dp
     * @param duration：时间间隔 ms
     */
    private void updateScrollRatio(float dpX, long duration) {
        int ratio = (int) ((Math.abs(dpX) / duration) * 1000);
        if (ratio < 20) {
            scrollRatio = 1;
        } else if (ratio < 40) {
            scrollRatio = 3;
        } else if (ratio < 70) {
            scrollRatio = 7;
        } else if (ratio < 100) {
            scrollRatio = 13;
        } else if (ratio < 300) {
            scrollRatio = 18;
        } else if (ratio < 500) {
            scrollRatio = 24;
        } else if (ratio < 800) {
            scrollRatio = 31;
        } else if (ratio < 1000) {
            scrollRatio = 40;
        } else {
            scrollRatio = 60;
        }
    }

    //累积快进进度,totalDuration：当前快进的总值，负值代表是要快退
    private void updateVideoTime(int duration) {
        totalDuration += duration;
        if (listener != null) {
            listener.onGestureUpdateVideoTime(totalDuration);
        }
    }

    /**
     * 更新亮度值
     * @param ratio 单次滑动改边的亮度比率 0.0~1.0f
     */
    private void updateVideoLeftTB(float ratio) {
        brightnessValue += ratio;
        brightnessValue = Math.min(brightnessValue, 1.0f);
        if(brightnessValue < 1.0f){
            brightnessValue = Math.max(brightnessValue, 0f);
        }
        if (listener != null) {
            listener.onGestureLeftTB(brightnessValue);
        }
    }

    private void updateVideoRightTB(float ratio) {
        volumeValue += ratio;
        volumeValue = Math.min(volumeValue, 1.0f);
        if(volumeValue < 1.0f){
            volumeValue = Math.max(volumeValue, 0f);
        }
        if (listener != null) {
            listener.onGestureRightTB(volumeValue);
        }
    }

    private enum ScrollMode {
        NONE,               //初始值
        LEFT_TB,            //左边上下滑动(调节亮度)
        RIGHT_TB,           //右边上下滑动(调节声音)
        HORIZONTAL_S,       //横向滑动(快进快退)
        SINGLE_CLICK,       //单击
        DOUBLE_CLICK        //双击
    }

}
