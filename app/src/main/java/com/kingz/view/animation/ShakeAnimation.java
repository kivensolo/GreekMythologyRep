package com.kingz.view.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.kingz.customdemo.R;

/**
 * Shake 抖动动画
 * 可用于密码错误等情况的抖动提示
 */
public class ShakeAnimation extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_shake);
    }

    public void login(View v) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        ((EditText)findViewById(R.id.user_name_edit_text)).setText(R.string.user_error_tips);
        findViewById(R.id.user_name_edit_text).startAnimation(shake);
    }

}