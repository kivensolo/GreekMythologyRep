package com.photo;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.BaseActivity;
import com.kingz.customDemo.R;
import com.utils.BitMapUtils;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/6 23:05
 * description:
 */
public class PhotosActivity extends BaseActivity {

    public static final String TAG = "PhotosActivity";

    private Bitmap srcBitmap;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private ImageView img6;

    @Override
    protected void findID() {
        super.findID();
        setContentView(R.layout.photos_activity);
        img1 = (ImageView) findViewById(R.id.normal_pic);
        img2 = (ImageView) findViewById(R.id.translate_pic);
        img3 = (ImageView) findViewById(R.id.scale_pic);
        img4 = (ImageView) findViewById(R.id.rotate_pic);
        img5 = (ImageView) findViewById(R.id.skew_pic);
        img6 = (ImageView) findViewById(R.id.circle_pic);
        setImageView();
    }

    private void setImageView() {
        srcBitmap = BitMapUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.wang), 320, 180);
        //加载原始图片
        img1.setImageDrawable(getResources().getDrawable(R.drawable.wang));
        //加载平移的图片
        img2.setImageBitmap(srcBitmap);

        //加载剪切的图片
        img3.setImageBitmap(BitMapUtils.zoomImg(srcBitmap,160,90));

        //旋转(顺时针)
        img4.setImageBitmap(BitMapUtils.rotateImage(180, srcBitmap));

        //圆角矩形
        img5.setImageBitmap(BitMapUtils.setRoundCorner(srcBitmap,45));

        //圆形图片
        img6.setImageBitmap(BitMapUtils.setCircle(srcBitmap));
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
}
