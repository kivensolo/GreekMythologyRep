package com.kingz.newfeatures;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Window;
import android.view.animation.AnticipateInterpolator;

import com.kingz.customdemo.R;

/**
 * Activity 滑动切换效果页面
 */
@TargetApi(Build.VERSION_CODES.O)
public class SlideAnim extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Slide().setDuration(2000));
        getWindow().setReturnTransition(new Slide().setInterpolator(new AnticipateInterpolator()).setDuration(2000));
        setContentView(R.layout.activity_slide_anim);
    }
}
