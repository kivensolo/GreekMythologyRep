package com.module.tools;

import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/**
 * author：KingZ
 * date：2019/3/24
 * description： 手势速度工具类
 */
public class VelocityUtils {

    public static float getscrollerVelocity_X(VelocityTracker tracker){
        tracker.computeCurrentVelocity(1000);
        return tracker.getXVelocity();
    }

    public static float getscrollerVelocity_Y(VelocityTracker tracker){
        tracker.computeCurrentVelocity(1000);
        return tracker.getYVelocity();
    }

    public static void recycleVelocityTracker(VelocityTracker tracker){
        if (tracker != null) {
            tracker.clear();
            tracker.recycle();
        }
    }

    public static int getScaledMinFlingVelocityOfViewConfig(ViewConfiguration vc){
        return vc.getScaledMinimumFlingVelocity();
    }

    public static int getScaledMaxFlingVelocityOfViewConfig(ViewConfiguration vc){
        return vc.getScaledMaximumFlingVelocity();
    }



}
