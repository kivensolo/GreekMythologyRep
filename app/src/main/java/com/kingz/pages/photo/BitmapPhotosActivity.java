package com.kingz.pages.photo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kingz.customdemo.R;
import com.kingz.graphics.GlideApp;
import com.zeke.kangaroo.utils.BitMapUtils;
import com.zeke.kangaroo.utils.ScreenShotUtils;

import java.util.ArrayList;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/6 23:05
 * description:
 * //TODO 解决listView的复用问题
 */
public class BitmapPhotosActivity extends PhotosActivity {
    public static final String TAG = "BitmapPhotosActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        srcBitmap = BitMapUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.sunyanzi_1), 680, 420);
        waterMark = BitMapUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.m), 110, 132);

        //Lazy load
        datas = new ArrayList<>();
        datas.add(new ItemInfo("原始图片", srcBitmap));
        datas.add(new ItemInfo("平移图片", BitMapUtils.setZoomImg(srcBitmap, 160, 90)));
        datas.add(new ItemInfo("放大/缩小图片", BitMapUtils.setZoomImg(srcBitmap, 160, 90)));
        datas.add(new ItemInfo("旋转图片---水平", BitMapUtils.setRotateImage(180, srcBitmap)));
        datas.add(new ItemInfo("旋转图片---Y轴", BitMapUtils.setRotateImage_XYZ(0f, 20f, 0f, srcBitmap)));
        datas.add(new ItemInfo("圆角图片", BitMapUtils.setRoundCorner(srcBitmap, 45)));
        datas.add(new ItemInfo("圆形图片", BitMapUtils.setBitmapCircle(srcBitmap)));
        datas.add(new ItemInfo("斜切图片", BitMapUtils.setSkew(srcBitmap, -0.3f, 0)));
        datas.add(new ItemInfo("水印---图片", BitMapUtils.createWaterMarkBitmap(srcBitmap, waterMark,
                                            srcBitmap.getWidth() - waterMark.getWidth(),
                                            0)));
        datas.add(new ItemInfo("水印---文字", BitMapUtils.createWaterMarkText(srcBitmap, "测试水印",
                                            srcBitmap.getWidth() - srcBitmap.getWidth() / 2,
                                            srcBitmap.getHeight() - srcBitmap.getHeight() / 2)));
        datas.add(new ItemInfo("倒影", BitMapUtils.setInvertedBitmap(srcBitmap, srcBitmap.getHeight() / 3)));

        datas.add(new ItemInfo("加投影",srcBitmap));
        datas.add(new ItemInfo("截屏", srcBitmap));
        datas.add(new ItemInfo("GIF", null));

        //datas.add("高斯模糊",BitMapUtils.guassBlur(srcBitmap,this,15.5f));
        super.onCreate(savedInstanceState);
        setImageView();
    }


    private void setImageView() {
        img1.setImageBitmap(srcBitmap);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        if(datas.get(position).getName().equals("GIF")){
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.android) //设置“加载中”状态时显示的图片
                    .override(200, 200)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.drawable.alert_dialog_icon); //设置“加载失败”状态时显示的图片

            GlideApp.with(this)
                    .load("http://p1.pstatp.com/large/166200019850062839d3")
                    .apply(requestOptions)
                    .into(img1);
        }
        setShowBitMap(datas.get(position).dstBitmap);
        if(datas.get(position).getName().equals("加投影")){
            Paint paint = new Paint();
            paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.HINTING_ON | Paint.FILTER_BITMAP_FLAG);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
            paint.setColor(0xFF00ff00);
            BitMapUtils.setShadow(datas.get(position).dstBitmap,
                    new Canvas(datas.get(position).dstBitmap), paint,
                    20, 0x00FF00,
                    50, 50);
        }else if(datas.get(position).getName().equals("截屏")){
            setShowBitMap(ScreenShotUtils.screenShot(getWindow().getDecorView()));
        }
    }

    private void setShowBitMap(final Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }

        RequestOptions ros = new RequestOptions()
                .placeholder(R.drawable.android) //设置“加载中”状态时显示的图片
//                .override(200, 200)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.alert_dialog_icon); //设置“加载失败”状态时显示的图片

        GlideApp.with(this)
                .asBitmap()
                .load(bitmap)
                .apply(ros)
                .into(img1);

      /*  runOnUiThread(new Runnable() {
            @Override
            public void run() {
                img1.setBackgroundColor(Color.TRANSPARENT);
                img1.setImageBitmap(bitmap);
            }
        });*/
    }
}

