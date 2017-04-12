package com.kingz.pages.photo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.kingz.customdemo.R;
import com.utils.BitMapUtils;
import com.utils.ToastTools;

import java.util.Arrays;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/6 23:05
 * description: Listview显示的页面
 */
public class BitmapPhotosActivity extends PhotosActivity{

    public static final String TAG = "BitmapPhotosActivity";

    String[] strs = {"平移图片", "放大/缩小图片", "旋转图片","圆角图片","圆形图片",
            "斜切图片","水印---图片","水印---文字","倒影","Glide加载图片"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        datas = Arrays.asList(strs);
        super.onCreate(savedInstanceState);
        setImageView();
    }


    private void setImageView() {
        srcBitmap = BitMapUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.sunyanzi_1), 680, 420);
        waterMark = BitMapUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.m), 110,132);
        //加载原始图片
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
        String clickedtype = (String) bitmapAdapter.getItem(position);
        Log.i(TAG,"onItemClick： chooseTpye = " + clickedtype);
        switch (position){
            case 0:
                setShowBitMap(srcBitmap);
                break;
            case 1:
                setShowBitMap(BitMapUtils.setZoomImg(srcBitmap,160,90));
                break;
            case 2:
                setShowBitMap(BitMapUtils.setRotateImage(180, srcBitmap));
                break;
            case 3:
                setShowBitMap(BitMapUtils.setRoundCorner(srcBitmap,45));
                break;
            case 4:
                setShowBitMap(BitMapUtils.setBitmapCircle(srcBitmap));
                break;
            case 5:
                setShowBitMap(BitMapUtils.setSkew(srcBitmap,-0.3f,0));
                break;
            case 6:
                setShowBitMap(BitMapUtils.createWaterMarkBitmap(srcBitmap,waterMark,srcBitmap.getWidth() - waterMark.getWidth(),0));
                break;
            case 7:
                setShowBitMap(BitMapUtils.createWaterMarkText(srcBitmap,"测试水印",
                srcBitmap.getWidth() - srcBitmap.getWidth()/2,srcBitmap.getHeight() - srcBitmap.getHeight()/2));
                break;
            case 8:
                setShowBitMap(BitMapUtils.setInvertedBitmap(srcBitmap,srcBitmap.getHeight()/3));
                break;
            case 9:
                Glide.with(this)
                    .load("http://nuuneoi.com/uploads/source/playstore/cover.jpg")
                    .error(R.mipmap.sample_2)
                    .fitCenter() //图片比ImageView大的时候，就会按照比例对图片进行缩放，并将图片居中显示。如果这张图片比ImageView小，那么就会根据比例对图片进行扩大，然后将其居中显示
                    .centerCrop()
                    .into(img1);
                break;
            default:
                ToastTools.getInstance().showMgtvWaringToast(this,"未匹配");
                break;
        }
    }

    private void setShowBitMap(final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                img1.setImageBitmap(bitmap);
            }
        });
    }
}

