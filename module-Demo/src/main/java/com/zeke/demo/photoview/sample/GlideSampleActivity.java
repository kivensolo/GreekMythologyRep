package com.zeke.demo.photoview.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.module.views.photoview.PhotoView;
import com.zeke.demo.R;

public class GlideSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        final PhotoView photoView = findViewById(R.id.iv_photo);

        Glide.with(this)
                .load("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1415616805,3449257715&fm=26&gp=0.jpg")
                .into(photoView);
    }
}
