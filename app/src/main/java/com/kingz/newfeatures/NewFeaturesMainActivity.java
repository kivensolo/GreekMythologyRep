package com.kingz.newfeatures;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingz.customdemo.R;
import com.kingz.module.common.router.RouterConfig;

@Route(path = RouterConfig.PAGE_NEW_FEATURES)
public class NewFeaturesMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setReturnTransition(new Explode().setDuration(2000));
        }
        setContentView(R.layout.newfeatures_main);
        init();
    }

    private void init() {
        findViewById(R.id.decompose_anim).setOnClickListener(this);
        findViewById(R.id.slide_anim).setOnClickListener(this);
        findViewById(R.id.fade_anim).setOnClickListener(this);
        findViewById(R.id.shared_anim).setOnClickListener(this);
        findViewById(R.id.perssion_btn).setOnClickListener(this);
        findViewById(R.id.pip_btn).setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            Toast.makeText(this,"要求API高于21 (5.0)",Toast.LENGTH_LONG).show();
            return;
        }
        switch (view.getId()) {
            case R.id.decompose_anim:
                startActivity(new Intent(this, DecomposeAnim.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            case R.id.slide_anim:
                startActivity(new Intent(this, SlideAnim.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            case R.id.fade_anim:
                startActivity(new Intent(this, FadeAnim.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
            case R.id.shared_anim:
                startActivity(new Intent(this, SharedAnim.class), ActivityOptions.makeSceneTransitionAnimation(this, view, "shared_btn_one").toBundle());
                break;
            case R.id.perssion_btn:
                startActivity(new Intent(this, PermissionActivity.class));
                break;
            case R.id.pip_btn:
                startActivity(new Intent(this, PinpActivity.class));
                break;
        }
    }
}
