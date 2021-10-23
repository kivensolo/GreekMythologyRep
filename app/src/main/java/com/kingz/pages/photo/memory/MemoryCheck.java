package com.kingz.pages.photo.memory;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kingz.customdemo.R;
import com.kingz.module.common.base.BaseActivity;
import com.zeke.kangaroo.utils.BitMapUtils;
import com.zeke.kangaroo.zlog.ZLog;

import java.util.Locale;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2017/8/20 14:23 <br>
 * description: BitMap内存加载测试 <b */

public class MemoryCheck extends BaseActivity {

    private ImageView imgView;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_memory);

        imgView = (ImageView) findViewById(R.id.image_show_id);
        btn = (Button) findViewById(R.id.change_btn_blowimg);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bitmap bitmap = BitMapUtils.decodeResource(getResources(), R.drawable.img_1080_2, 1920 / 2, 1080 / 2);
                ZLog.d("MemoryCheck",String.format(Locale.getDefault(),"新bitmap使用: %2f M",(float)bitmap.getAllocationByteCount() / 1024f / 1024f));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imgView.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
}
