package com.kingz.pages.photo;

import android.os.Bundle;
import android.widget.SeekBar;
import com.BaseActivity;
import com.kingz.customdemo.R;
import com.utils.ZLog;
import com.view.views.ColorMartixImageView;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2017/5/26 22:01 <br>
 * description: ColorMatrix的使用 <br>
 * setARGB (int a, int r, int g, int b):用于设置画笔颜色，A 代表 alpha（透明度），R 代表Red （红色），G 代表 Green（绿色），B 代表 Blue（蓝色）
 * 色值采用16进制，取值在 0 － 255 之间 ，0（0x00） 即 完全没有 ，255(0xff) 代表满值 ;
 * setAlpha(int a): 用于设置Paint 的透明度；
 * setColor(int color):同样设置颜色，如果是常用色，可以使用Color 类中定义好的一些色值 ，eg：Color.WHITE
 * setColorFilter(ColorFilter filter):设置颜色过滤器,可以通过颜色过滤器过滤掉对应的色值，比如去掉照片颜色，生成老照片效果；
 * ColorFilter有以下几个子类可用:
 * ColorMatrixColorFilter
 * LightingColorFilter
 * PorterDuffColorFilter
 * <p>
 * 在Android中，图片是以一个个 RGBA 的像素点的形式加载到内存中的，所以如果需要改变图片的颜色，
 * 就需要针对这一个个像素点的RGBA的值进行修改，其实主要是RGB，A是透明度；
 * 修改图片 RGBA 的值需要ColorMatrix类的支持，它定义了一个 4*5 的float[]类型的矩阵，矩阵中每一行表示 RGBA 中的一个参数。
 * 颜色矩阵M是以一维数组的方式进行存储的
 * m=[a,b,c,d,e,  ----- 红色
 * f,g,h,i,j,  ----- 绿色
 * k,l,m,n,o,  ----- 蓝色
 * p,q,r,s,t]  ----- 透明度  颜色矩阵
 * <p>
 * 而对于一张图像来说，展示的颜色效果取决于图像的RGBA（红色、绿色、蓝色、透明度）值。
 * 而图像的 RGBA 值则存储在一个5*1的颜色分量矩阵C中，由颜色分量矩阵C可以控制图像的颜色效果。
 * 颜色分量矩阵为：
 * |R|
 * |G|
 * c=|B|
 * |A|
 * |1|
 * 利用ColorFilter 和 ColorMatrixColorFilter类 和 Paint 的setColorFilter
 * 就可以改变图片的展示效果（颜色，饱和度，对比度等）
 */
public class ColorMatrixDemo extends BaseActivity {
    private static final String TAG = "ColorMatrixDemo";
    private SeekBar seekBarA;
    private SeekBar seekBarR;
    private SeekBar seekBarG;
    private SeekBar seekBarB;
    private ColorMartixImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colormatrix);
        initSeekBars();
    }


    private void initSeekBars() {
        imageView = (ColorMartixImageView) findViewById(R.id.corlormartix_view);
        seekBarA = (SeekBar) findViewById(R.id.seekBar_alpha);
        seekBarR = (SeekBar) findViewById(R.id.seekBar_red);
        seekBarG = (SeekBar) findViewById(R.id.seekBar_green);
        seekBarB = (SeekBar) findViewById(R.id.seekBar_blue);
        seekBarA.setOnSeekBarChangeListener(seekBarChange);
        seekBarR.setOnSeekBarChangeListener(seekBarChange);
        seekBarG.setOnSeekBarChangeListener(seekBarChange);
        seekBarB.setOnSeekBarChangeListener(seekBarChange);
    }

    private SeekBar.OnSeekBarChangeListener seekBarChange = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            ZLog.d(TAG, "progress=" + progress);
            float filter = (float) progress / 100;
            if (seekBar == seekBarA) {
                imageView.mAlphaFilter = filter;
            } else if (seekBar == seekBarR) {
                imageView.mRedFilter = filter;
            } else if (seekBar == seekBarG) {
                imageView.mGreenFilter = filter;
            } else if (seekBar == seekBarB) {
                imageView.mBlueFilter = filter;
            }
            imageView.setArgb(imageView.mAlphaFilter, imageView.mRedFilter,
                    imageView.mGreenFilter,
                    imageView.mBlueFilter);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
