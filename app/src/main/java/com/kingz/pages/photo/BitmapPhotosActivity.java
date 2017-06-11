package com.kingz.pages.photo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.kingz.customdemo.R;
import com.utils.BitMapUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/6 23:05
 * description: Listview显示的页面
 */
public class BitmapPhotosActivity extends PhotosActivity{

    public static final String TAG = "BitmapPhotosActivity";


    String[] strs = {"平移图片", "放大/缩小图片", "旋转图片---水平","旋转图片---Y轴","圆角图片","圆形图片",
            "斜切图片","水印---图片","水印---文字","倒影","Glide加载图片"};
    private Map<String,Bitmap> photoMap = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        datas = new ArrayList<>();
        datas.add("平移图片");
        datas.add("放大/缩小图片");
        datas.add("旋转图片---水平");
        datas.add("旋转图片---Y轴");
        datas.add("圆角图片");
        datas.add("圆形图片");
        datas.add("斜切图片");
        datas.add("水印---图片");
        datas.add("水印---文字");
        datas.add("倒影");
        datas.add("高斯模糊");
        super.onCreate(savedInstanceState);
        setImageView();
        photoMap.put("原始图片",srcBitmap);
        photoMap.put("放大/缩小图片",BitMapUtils.setZoomImg(srcBitmap,160,90));
        photoMap.put("旋转图片---水平",BitMapUtils.setRotateImage(180, srcBitmap));
        photoMap.put("旋转图片---Y轴",BitMapUtils.setRotateImage_XYZ(0f,20f,0f,srcBitmap));
        photoMap.put("圆角图片",BitMapUtils.setRoundCorner(srcBitmap,45));
        photoMap.put("圆形图片",BitMapUtils.setBitmapCircle(srcBitmap));
        photoMap.put("斜切图片",BitMapUtils.setSkew(srcBitmap,-0.3f,0));
        photoMap.put("水印---图片",BitMapUtils.createWaterMarkBitmap(srcBitmap,waterMark,srcBitmap.getWidth() - waterMark.getWidth(),0));
        photoMap.put("水印---文字",BitMapUtils.createWaterMarkText(srcBitmap,"测试水印",
                                                                    srcBitmap.getWidth() - srcBitmap.getWidth()/2,
                                                                    srcBitmap.getHeight() - srcBitmap.getHeight()/2));
        photoMap.put("倒影",BitMapUtils.setInvertedBitmap(srcBitmap,srcBitmap.getHeight()/3));
        //photoMap.put("高斯模糊",BitMapUtils.guassBlur(srcBitmap,this,15.5f));
    }


    private void setImageView() {
        srcBitmap = BitMapUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.sunyanzi_1), 680, 420);
        waterMark = BitMapUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.m), 110,132);
        img1.setImageBitmap(srcBitmap);
    }

    @Override
    public void showLoadingDialog() {
        super.showLoadingDialog();
    }

    @Override
    protected void Listener() {
        super.Listener();
    }

    @Override
    protected void initIntent() {
        super.initIntent();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view,position,id);
        String value = datas.get(position);
        setShowBitMap(photoMap.get(value));
        //Glide.with(this)
        //    .load("http://nuuneoi.com/uploads/source/playstore/cover.jpg")
        //    .error(R.mipmap.sample_2)
        //    .fitCenter() //图片比ImageView大的时候，就会按照比例对图片进行缩放，并将图片居中显示。如果这张图片比ImageView小，那么就会根据比例对图片进行扩大，然后将其居中显示
        //    .centerCrop()
        //    .into(img1);
    }

    private void setShowBitMap(final Bitmap bitmap) {
        if(bitmap == null){
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                img1.setBackgroundColor(Color.TRANSPARENT);
                img1.setImageBitmap(bitmap);
            }
        });
    }
}

