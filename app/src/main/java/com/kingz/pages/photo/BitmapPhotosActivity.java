package com.kingz.pages.photo;

import android.graphics.*;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.kingz.customdemo.R;
import com.utils.BitMapUtils;

import java.util.ArrayList;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/6 23:05
 * description: Listview显示的页面
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

        //datas.add("高斯模糊",BitMapUtils.guassBlur(srcBitmap,this,15.5f));
        super.onCreate(savedInstanceState);
        setImageView();
    }


    private void setImageView() {
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
        super.onItemClick(parent, view, position, id);
        setShowBitMap(datas.get(position).dstBitmap);
        if(datas.get(position).name.equals("加投影")){
            Paint paint = new Paint();
            paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.HINTING_ON | Paint.FILTER_BITMAP_FLAG);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
            paint.setColor(0xFF00ff00);
            BitMapUtils.addShadow(new Canvas(datas.get(position).dstBitmap),20,0x00FF00,50,50,datas.get(position).dstBitmap,paint);
        }

        //Glide.with(this)
        //    .load("http://nuuneoi.com/uploads/source/playstore/cover.jpg")
        //    .error(R.mipmap.sample_2)
        //    .fitCenter() //图片比ImageView大的时候，就会按照比例对图片进行缩放，并将图片居中显示。如果这张图片比ImageView小，那么就会根据比例对图片进行扩大，然后将其居中显示
        //    .centerCrop()
        //    .into(img1);
    }

    private void setShowBitMap(final Bitmap bitmap) {
        if (bitmap == null) {
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

    public class ItemInfo {
        private String name = "";
        public Bitmap dstBitmap;

        public ItemInfo(String name, Bitmap dstBitmap) {
            this.name = name;
            this.dstBitmap = dstBitmap;
        }
        public String getName(){
            return name;
        }
    }
}

