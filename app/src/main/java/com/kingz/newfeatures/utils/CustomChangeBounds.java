package com.kingz.newfeatures.utils;

import android.animation.Animator;
import android.transition.ChangeBounds;
import android.transition.TransitionValues;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

/**
 * Created by zezheng.li on 2019/6/21
 * Describe:
 */
public class CustomChangeBounds extends ChangeBounds {

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        Animator animator = super.createAnimator(sceneRoot,startValues,endValues);
        animator.setDuration(2000);
        animator.setInterpolator(new OvershootInterpolator(){
            float tension = 1.5f;
            @Override
            public float getInterpolation(float t) {
                t -= 1.0f;
                return t * t * ((tension + 1) * t + tension) + 1.0f;
            }
        });
        return animator;
    }

    /**
     * 插值器
     * AccelerateDecelerateInterpolator 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
     *
     * AccelerateInterpolator  在动画开始的地方速率改变比较慢，然后开始加速
     *
     * AnticipateInterpolator 开始的时候向后然后向前甩
     *
     * AnticipateOvershootInterpolator 开始的时候向后然后向前甩一定值后返回最后的值
     *
     * BounceInterpolator   动画结束的时候弹起
     *
     * CycleInterpolator 动画循环播放特定的次数，速率改变沿着正弦曲线
     *
     * DecelerateInterpolator 在动画开始的地方快然后慢
     *
     * LinearInterpolator   以常量速率改变
     *
     * OvershootInterpolator    向前甩一定值后再回到原来位置

     */
}
