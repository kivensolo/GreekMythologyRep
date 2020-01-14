package com.kingz.pages.photo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kingz.customdemo.R;
import com.kingz.graphics.GlideApp;
import com.zeke.kangaroo.utils.BitMapUtils;
import com.zeke.kangaroo.utils.ScreenShotUtils;
import com.zeke.kangaroo.utils.UIUtils;
import com.zeke.kangaroo.utils.ZLog;

import java.util.Arrays;

/**
 * author: King.Z
 * date:  2016/8/6 23:05
 * description:
 * //TODO 解决listView的复用问题
 */
public class BitmapPhotosActivity extends PhotosActivity {
    public static final String TAG = "BitmapPhotosActivity";
    public static int WIDTH;
    public static int HEIGHT;
    public static int current_pos = -1;
    protected Bitmap srcBitmap;
    protected Bitmap waterMark;

    String[] fakeData = new String[]{"原始图片", "放大/缩小图片",
            "旋转图片---水平", "旋转图片---Y轴", "圆角图片_手动裁剪",
            "斜切图片", "水印---图片", "水印---文字",
            "倒影", "截屏", "高斯模糊", "GIF"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WIDTH = UIUtils.dip2px(this, 300);
        HEIGHT = UIUtils.dip2px(this, 160);
        ZLog.d(TAG,"WIDTH="+WIDTH + ";HEIGHT="+HEIGHT);
        srcBitmap = BitMapUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.glide_cover), WIDTH, HEIGHT);
        waterMark = BitMapUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.raspberry_pi_logo),
                UIUtils.dip2px(this, 35),
                UIUtils.dip2px(this, 40));
        datas = Arrays.asList(fakeData);
        super.onCreate(savedInstanceState);
        setImageView();
    }


    private void setImageView() {
        picView.setImageBitmap(srcBitmap);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        if(current_pos == position){
            return;
        }else{
            current_pos = position;
        }
        String name = datas.get(position);
        if (TextUtils.equals(name, "原始图片")) {
            setShowBitMap(srcBitmap);
        } else if (TextUtils.equals(name, "放大/缩小图片")) {
            setShowBitMap(BitMapUtils.setZoomImg(srcBitmap, 160, 90));
        } else if (TextUtils.equals(name, "旋转图片---水平")) {
            setShowBitMap(BitMapUtils.setRotateImage(180, srcBitmap));
        } else if (TextUtils.equals(name, "旋转图片---Y轴")) {
            setShowBitMap(BitMapUtils.setRotateImage_XYZ(0f, 40f, 0f, srcBitmap));
        } else if (TextUtils.equals(name, "圆角图片_手动裁剪")) {
            setShowBitMap(BitMapUtils.setRoundCorner(srcBitmap, 45));
        } else if (TextUtils.equals(name, "斜切图片")) {
            setShowBitMap(BitMapUtils.setSkew(srcBitmap, -0.3f, 0));
        } else if (TextUtils.equals(name, "水印---图片")) {
            int mark_x = srcBitmap.getWidth() - waterMark.getWidth();
            setShowBitMap(BitMapUtils.createWaterMarkBitmap(srcBitmap, waterMark,
                    mark_x, 0));
        } else if (TextUtils.equals(name, "水印---文字")) {
            int mark_x = srcBitmap.getWidth() - srcBitmap.getWidth() / 2;
            int mark_y = srcBitmap.getHeight() - srcBitmap.getHeight() / 4;
            setShowBitMap(BitMapUtils.createWaterMarkText(srcBitmap, "测试水印",
                    mark_x, mark_y));
        } else if (TextUtils.equals(name, "投影")) {
            setShowBitMap(BitMapUtils.setInvertedBitmap(srcBitmap, srcBitmap.getHeight() / 3));
        } else if (TextUtils.equals(name, "截屏")) {
            setShowBitMap(ScreenShotUtils.screenShot(getWindow().getDecorView()));
        } else if (TextUtils.equals(name, "高斯模糊")) {
//            setShowBitMap(BitMapUtils.guassBlur(srcBitmap, this, 15.5f));
        } else if (TextUtils.equals(name, "GIF")) {
            showGif("http://p1.pstatp.com/large/166200019850062839d3");
        }
    }

    private void setShowBitMap(Bitmap bitmap) {
        if (bitmap == null) {
            ZLog.e("setShowBitMap bitmap == null");
            return;
        }
        picView.setBackgroundColor(Color.TRANSPARENT);
        RequestOptions ros = new RequestOptions()
                .override(WIDTH, HEIGHT)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.alert_dialog_icon); //设置“加载失败”状态时显示的图片

        GlideApp.with(this)
                .load(bitmap)
                .apply(ros)
                .into(picView);
    }

    private void showGif(String url) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.android) //设置“加载中”状态时显示的图片
                .override(200, 200) // TODO 设置图形大小后  感觉是设置的imageview
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.alert_dialog_icon); //设置“加载失败”状态时显示的图片

        GlideApp.with(this)
                .load(url)
                .apply(requestOptions)
                .into(picView);
    }
}

