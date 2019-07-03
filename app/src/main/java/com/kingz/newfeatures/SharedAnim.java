package com.kingz.newfeatures;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.Window;

import com.kingz.customdemo.R;
import com.kingz.newfeatures.utils.CustomChangeBounds;

/**
 * “共享元素”展示页面
 */
@TargetApi(Build.VERSION_CODES.O)
public class SharedAnim extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Fade().setDuration(2000));
        getWindow().setReturnTransition(new Fade().setDuration(2000));
        getWindow().setSharedElementEnterTransition(new CustomChangeBounds());
        setContentView(R.layout.activity_shared_anim);
    }
}
