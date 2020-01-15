package com.kingz.pages.photo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
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
 * description: bitmap效果测试
 */
public class BitmapPhotosActivity extends PhotosActivity {
    public static final String TAG = "BitmapPhotosActivity";
    public static int WIDTH;
    public static int HEIGHT;
    public static int current_pos = -1;
    protected Bitmap waterMark;

    protected Bitmap srcBitmap;
    protected Bitmap changeSizeBitmap;
    protected Bitmap rotate180_Bitmap;
    protected Bitmap rotateYZ_Bitmap;
    protected Bitmap Rounded_Bitmap;
    protected Bitmap Skew_Bitmap;
    protected Bitmap pic_waterMarkBitmap;
    protected Bitmap text_waterMarkBitmap;
    protected Bitmap shadowBitmap;
    protected Bitmap screenShotBitmap;
    protected Bitmap GuassBlurBitmap;

    String[] fakeData = new String[]{"Source Pic", "Change Size",
            "Rotate_180", "Rotate_Y&Z", "Rounded",
            "Skew", "Pic_Watermark", "Text_Watermark",
            "Shadow", "screenShot", "GuassBlur", "GIF"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WIDTH = UIUtils.dip2px(this, 370);
        HEIGHT = UIUtils.dip2px(this, 240);
        ZLog.d(TAG, "WIDTH=" + WIDTH + ";HEIGHT=" + HEIGHT);
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

        changeSizeBitmap = BitMapUtils.setZoomImg(srcBitmap, UIUtils.dip2px(this, 190),
                UIUtils.dip2px(this, 120));
        rotate180_Bitmap = BitMapUtils.setRotateImage(180, srcBitmap);
        rotateYZ_Bitmap = BitMapUtils.setRotateImage_XYZ(0f, 20f, 10f, srcBitmap);
        Rounded_Bitmap = BitMapUtils.setRoundCorner(srcBitmap, 45);
        Skew_Bitmap = BitMapUtils.setSkew(srcBitmap, -0.3f, 0);

        int mark_x = srcBitmap.getWidth() - waterMark.getWidth();
        pic_waterMarkBitmap = BitMapUtils.createWaterMarkBitmap(srcBitmap, waterMark,
                mark_x, 0);


        int shawdo_mark = srcBitmap.getWidth() - srcBitmap.getWidth() / 2;
        int mark_y = srcBitmap.getHeight() - srcBitmap.getHeight() / 4;
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        textPaint.setTextSize(UIUtils.dip2px(this, 20.0f));
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setColor(getResources().getColor(R.color.yellow));
        text_waterMarkBitmap = BitMapUtils.createWaterMarkText(srcBitmap,
                "WaterMark",
                shawdo_mark, mark_y,textPaint);

        shadowBitmap = BitMapUtils.setInvertedBitmap(srcBitmap, srcBitmap.getHeight() / 3);
        screenShotBitmap = ScreenShotUtils.screenShot(getWindow().getDecorView());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        if (current_pos == position) {
            return;
        } else {
            current_pos = position;
        }
        String name = datas.get(position);
        ZLog.d(TAG, "onItemClick(): position=" + position + ";name=" + name);
        if (TextUtils.equals(name, "Source Pic")) {
            setShowBitMap(srcBitmap);
        } else if (TextUtils.equals(name, "Change Size")) {
            setShowBitMap(changeSizeBitmap);
        } else if (TextUtils.equals(name, "Rotate_180")) {
            setShowBitMap(rotate180_Bitmap);
        } else if (TextUtils.equals(name, "Rotate_Y&Z")) {
            setShowBitMap(rotateYZ_Bitmap);
        } else if (TextUtils.equals(name, "Rounded")) {
            setShowBitMap(Rounded_Bitmap);
        } else if (TextUtils.equals(name, "Skew")) {
            setShowBitMap(Skew_Bitmap);
        } else if (TextUtils.equals(name, "Pic_Watermark")) {
            setShowBitMap(pic_waterMarkBitmap);
        } else if (TextUtils.equals(name, "Text_Watermark")) {
            setShowBitMap(text_waterMarkBitmap);
        } else if (TextUtils.equals(name, "Shadow")) {
            setShowBitMap(shadowBitmap);
        } else if (TextUtils.equals(name, "screenShot")) {
            setShowBitMap(screenShotBitmap);
        } else if (TextUtils.equals(name, "GuassBlur")) {
//            setShowBitMap(BitMapUtils.guassBlur(srcBitmap, this, 15.5f));
        } else if (TextUtils.equals(name, "GIF")) {
//            showGif("http://p1.pstatp.com/large/166200019850062839d3");  // 烟花gif 无法加载
            showGif("http://5b0988e595225.cdn.sohucs.com/images/20181224/19b7010333504f64a6e7b278f2c42bd4.gif");  // 正常加载
//            showGif("http://hbimg.b0.upaiyun.com/6f0a29b41f7cd9fc2daf075f53be1960208ca650fb20b-tr4Abg_fw658"); // 死神gif 正常加载
        }
    }

    private void setShowBitMap(final Bitmap bitmap) {
        if (bitmap == null) {
            ZLog.e("setShowBitMap bitmap == null");
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                picView.setBackgroundColor(Color.TRANSPARENT);
                picView.setImageBitmap(bitmap);
            }
        });
    }

    private void showGif(String url) {
        RequestOptions requestOptions = new RequestOptions()
//                .placeholder(R.drawable.android) //设置“加载中”状态时显示的图片
                .override(UIUtils.dip2px(this, 350),
                        UIUtils.dip2px(this, 240))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .error(R.drawable.alert_dialog_icon); //设置“加载失败”状态时显示的图片

        GlideApp.with(this)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade(600))
                .into(picView);
    }

    @Override
    protected void onDestroy() {


//        Field[] fields = getClass().getDeclaredFields();
//        for (Field field : fields) {
//            field.setAccessible(true);
//            if (!Bitmap.class.isAssignableFrom(field.getType())) {
//                //判断该字段的类型是不是Bitmap或是Bitmap的子类
//                continue;
//            }
//            try {
//                @SuppressWarnings("unchecked")
//                Class<Bitmap> bitmapClass = (Class<Bitmap>) field.getDeclaringClass();
//                Method method = bitmapClass.getMethod("recycle");
//                method.invoke(field);
//                field.set(bitmapClass, null);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }
        srcBitmap.recycle();
        srcBitmap = null;
        changeSizeBitmap.recycle();
        changeSizeBitmap = null;
        rotate180_Bitmap.recycle();
        rotate180_Bitmap = null;
        rotateYZ_Bitmap.recycle();
        rotateYZ_Bitmap = null;
        Rounded_Bitmap.recycle();
        Rounded_Bitmap = null;
        Skew_Bitmap.recycle();
        Skew_Bitmap = null;
        pic_waterMarkBitmap.recycle();
        pic_waterMarkBitmap = null;
        text_waterMarkBitmap.recycle();
        text_waterMarkBitmap = null;
        shadowBitmap.recycle();
        shadowBitmap = null;
        screenShotBitmap.recycle();
        screenShotBitmap = null;

        super.onDestroy();
    }
}

