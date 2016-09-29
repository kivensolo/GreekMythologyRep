package com.view.animation;

import android.view.animation.Interpolator;

/**
 * author: King.Z <br>
 * date:  2016/9/29 16:50 <br>
 * description: 二次曲线的差值器 <br>
 */
public class TwoCurvesInterpolator implements Interpolator {
    public TwoCurvesInterpolator() {
    }

    @Override
    public float getInterpolation(float input) {
        float result;
        if(input <= 0.5){
           result= (float) (-8*Math.pow(input,2)+4*input);
        }else{
           result =(float) (-8*Math.pow(input-0.5,2)+4*(input-0.5)) + 0.5f;
        }
        return result;
    }
}
