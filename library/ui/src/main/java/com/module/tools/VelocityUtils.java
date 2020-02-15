package com.module.tools;

import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/**
 * author：KingZ
 * date：2019/3/24
 * description： 手势速度工具类
 */
public class VelocityUtils {

    /**
     * 根据每秒滑动的像素值，获取X方向上的手势滑动速度
     * @param tracker {@link VelocityTracker} 速度追踪器对象
     * @return X速度值
     */
    public static float getscrollerVelocity_X(VelocityTracker tracker){
        tracker.computeCurrentVelocity(1000);
        return tracker.getXVelocity();
    }

    /**
     * 根据每秒滑动的像素值，获取Y方向上的手势滑动速度
     * @param tracker {@link VelocityTracker} 速度追踪器对象
     * @return Y速度值
     */
    public static float getscrollerVelocity_Y(VelocityTracker tracker){
        tracker.computeCurrentVelocity(1000);
        return tracker.getYVelocity();
    }

    /**
     * 回收速度追踪器对象
     * @param tracker {@link VelocityTracker} 速度追踪器
     */
    public static void recycleVelocityTracker(VelocityTracker tracker){
        if (tracker != null) {
            tracker.clear();
            tracker.recycle();
        }
    }

    /**
     * 根据{@link ViewConfiguration}获取Fling的最小速度值(以每秒像素值为单位)
     * @param vc ViewConfiguration对象
     * @return 最小速度值
     */
    public static int getScaledMinFlingVelocity(ViewConfiguration vc){
        return vc.getScaledMinimumFlingVelocity();
    }

    /**
     * 根据{@link ViewConfiguration}获取Fling的最大速度值(以每秒像素值为单位)
     * @param vc ViewConfiguration对象
     * @return 最大速度值
     */
    public static int getScaledMaxFlingVelocity(ViewConfiguration vc){
        return vc.getScaledMaximumFlingVelocity();
    }



}
