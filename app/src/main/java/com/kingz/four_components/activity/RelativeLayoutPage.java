package com.kingz.four_components.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import com.kingz.customdemo.R;

/**
 * Created by KingZ on 2016/1/6.
 * Discription:
 */
@SuppressLint("Registered")
public class RelativeLayoutPage extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relativelayout_page);
    }
}
