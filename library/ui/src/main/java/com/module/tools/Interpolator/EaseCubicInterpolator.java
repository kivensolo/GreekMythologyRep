package com.module.tools.Interpolator;

import android.graphics.PointF;
import android.view.animation.Interpolator;

/**
* Android内置插值器
* AccelerateDecelerateInterpolator 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
* AccelerateInterpolator  在动画开始的地方速率改变比较慢，然后开始加速
* AnticipateInterpolator 开始的时候向后然后向前甩
* AnticipateOvershootInterpolator 开始的时候向后然后向前甩一定值后返回最后的值
* BounceInterpolator   动画结束的时候弹起
* CycleInterpolator 动画循环播放特定的次数，速率改变沿着正弦曲线
* DecelerateInterpolator 在动画开始的地方快然后慢
* LinearInterpolator   以常量速率改变
* OvershootInterpolator    向前甩一定值后再回到原来位置
* PathInterpolator
* LutInterpolator
*/

/**
 * @author zeke.wang
 * @date 2020/7/23
 * @desc: 三阶贝塞尔差值器
 * 在线工具："http://cubic-bezier.com"
 */
public class EaseCubicInterpolator implements Interpolator {
    private static final String TAG = "EaseCubicInterpolator";
    private static final int ACCURACY = 4096;
    private final PointF mControlPoint1 = new PointF();
    private final PointF mControlPoint2 = new PointF();
    private int mLastI = 0;

    public EaseCubicInterpolator(float x1, float y1, float x2, float y2) {
        this.mControlPoint1.x = x1;
        this.mControlPoint1.y = y1;
        this.mControlPoint2.x = x2;
        this.mControlPoint2.y = y2;
    }

    /**
     * 求三次贝塞尔曲线(四个控制点)一个点某个维度的值.<br>
     * 参考资料: <em> http://devmag.org.za/2011/04/05/bzier-curves-a-tutorial/ </em>
     * @param t        取值[0, 1]
     * @param value0    value0
     * @param value1    value1
     * @param value2    value2
     * @param value3    value3
     * @return
     */
    private static double cubicCurves(double t, double value0, double value1, double value2, double value3) {
        double u = 1.0D - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;
        double value = uuu * value0;
        value += 3.0D * uu * t * value1;
        value += 3.0D * u * tt * value2;
        value += ttt * value3;
        return value;
    }

    public float getInterpolation(float input) {
        float t = input;
        // 近似求解t的值[0,1]
        for(int i = mLastI; i < ACCURACY; ++i) {
            t = 1.0F * (float)i / ACCURACY;
            double x = cubicCurves(t, 0.0D, mControlPoint1.x, mControlPoint2.x, 1);
            if (x >= input) {
                mLastI = i;
                break;
            }
        }

        double value = cubicCurves(t, 0.0D, mControlPoint1.y, mControlPoint2.y, 1);
        if (value > 0.999D) {
            value = 1;
            mLastI = 0;
        }

        return (float)value;
    }
}