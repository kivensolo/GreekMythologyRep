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

}
