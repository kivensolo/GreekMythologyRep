package com.module.tools.Interpolator;

import android.view.animation.Interpolator;

/**
 * @author zeke.wang
 * @date 2020/7/23
 * @maintainer zeke.wang
 * @desc:
 */
public class GaiEaseCubicInterpolator implements Interpolator {
    private float[] result;

    public GaiEaseCubicInterpolator(float[] result) {
        this.result = result;
    }

    public float getInterpolation(float input) {
        int index1 = (int)(input / 0.01F);
        int index2 = index1 + 1;
        return this.result[index1] + (this.result[index2] - this.result[index1]) * (input - (float)index1 * 0.01F) / 0.01F;
    }
}
