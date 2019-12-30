/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kingz.view.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.kingz.customdemo.R;

/**
 * 各种速度差值器效果
 */
public class InterpolatorAnimation extends Activity implements AdapterView.OnItemSelectedListener {

    private static final String[] INTERPOLATORS = {
            "Accelerate", "Decelerate", "Accelerate/Decelerate",
            "Anticipate", "Overshoot", "Anticipate/Overshoot",
            "Bounce","CycleInterpolator","Decelerate/Accelerate",
            "TowCurves"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_interpolator);

        Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, INTERPOLATORS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        final View target = findViewById(R.id.target);
        final View targetParent = (View) target.getParent();

        Animation a = new TranslateAnimation(0.0f,
                targetParent.getWidth() - target.getWidth() - targetParent.getPaddingLeft() -
                targetParent.getPaddingRight(), 0.0f, 0.0f);
        a.setDuration(2000);
        a.setStartOffset(1000);
        a.setRepeatMode(Animation.RESTART);
        a.setRepeatCount(Animation.INFINITE);

        switch (position) {
            case 0:
                a.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.accelerate_interpolator));
                break;
            case 1:
                a.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.decelerate_interpolator));
                break;
            case 2:
                a.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.accelerate_decelerate_interpolator));
                break;
            case 3:
                a.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.anticipate_interpolator));
                break;
            case 4:
                a.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.overshoot_interpolator));
                break;
            case 5:
                a.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.anticipate_overshoot_interpolator));
                break;
            case 6:
                a.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.bounce_interpolator));
                break;
            case 7:
                a.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.cycle_interpolator));
                break;
            case 8:
                a.setInterpolator(new DeclerateAccelerateInterpolator());
                break;
            case 9:
                a.setInterpolator(new TwoCurvesInterpolator());
                break;
        }

        target.startAnimation(a);
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }
}