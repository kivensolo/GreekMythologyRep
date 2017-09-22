package com.kingz.view.animation;

import android.view.animation.Interpolator;

/**
 * author: King.Z <br>
 * date:  2016/9/29 15:47 <br>
 * description: 自定义差值器 <br>
 */
public class DeclerateAccelerateInterpolator implements Interpolator{

    public DeclerateAccelerateInterpolator() {
    }

    @Override
    public float getInterpolation(float input) {
        float result;
        if(input <= 0.5){
           result= (float)Math.sin((input*Math.PI))/2;
        }else{
           result =(float)Math.cos((input+0.5)*Math.PI)/2+1.0f;
        }
        return result;
    }
}
