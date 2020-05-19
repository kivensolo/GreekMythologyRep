package com.kingz.newfeatures;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Window;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kingz.customdemo.R;

/**
 * 分解动画展示效果
 */

@TargetApi(Build.VERSION_CODES.O)
public class DecomposeAnim extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Explode().setInterpolator(new OvershootInterpolator()).setDuration(2000));
        getWindow().setReturnTransition(new Explode().setDuration(2000));
        setContentView(R.layout.activity_decompose_anim);
    }
}
