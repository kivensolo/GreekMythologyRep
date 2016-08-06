package com.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/3/27 18:36
 * description: BitMap工具类
 * <p/>
 * 知识点：
 * http://mp.weixin.qq.com/s?__biz=MzI3MDE0NzYwNA==&mid=2651433713&idx=1&sn=d152b053221c4c0bf1baa684b2a51e9c&scene=23&srcid=0805emSJb7dH8hdYfIcQiChP#rd
 * 1、decodeResource()和decodeFile()
 * decodeFile()用于读取SD卡上的图，得到的是图片的原始尺寸
 * decodeResource()用于读取Res、Raw等资源，得到的是图片的原始尺寸 * 缩放系数，
 * 缩放系数依赖于屏幕密度，参数可以通过BitMapFactory.Option的几个参数调整。
 * public boolean inScaled  //默认True
 * public int inDensity;       //无dip的文件夹下默认160
 * public int inTargetDensity; //取决具体屏幕
 * <p/>
 * ※ inScaled属性
 * 如果inScaled设置为false，则不进行缩放，解码后图片大小为720×720;
 * 如果inScaled设置为true或者不设置，则根据inDensity和inTargetDensity计算缩放系数。
 */
public class BitMapUtils {

    private BitMapUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /************************************ 图形变换  Start*****************************************/
    /**
     * 旋转图片
     *
     * @param angle  旋转角度
     * @param bitmap 目标图片
     * @return Bitmap    旋转后的图片
     */
    public static Bitmap rotateImage(int angle, Bitmap bitmap) {
        // 图片旋转矩阵
        Matrix matrix = new Matrix(); // 每一种变化都包括set，pre，post三种，分别为设置、矩阵先乘、矩阵后乘。
        matrix.postRotate(angle);
        // 得到旋转后的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 按比例缩放/裁剪图片
     * 通过矩阵方式
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 设置图片倾斜
     * @param bm 源BitMap
     * @param skewX x轴倾斜度
     * @param skewY y轴倾斜度
     * @return
     */
    public static Bitmap setSkew(Bitmap bm, int skewX, int skewY) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 取得想要倾斜的matrix参数
        Matrix matrix = new Matrix();
        matrix.setSkew(skewX, skewY);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 设置圆角
     * @param bm 源BitMap
     * @param radius 圆角弧度
     * @return
     */
    public static Bitmap setRoundCorner(Bitmap bm,int radius){
        //初始化画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //准备裁剪的矩阵
        Rect rect = new Rect(0,0,bm.getWidth(),bm.getHeight());
        RectF rectF= new RectF(0,0,bm.getWidth(),bm.getHeight());

        // 建立对应 bitmap
        Bitmap roundBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBitmap);
        //圆角矩形，radius为圆角大小
        canvas.drawRoundRect(rectF,radius,radius,paint);
        //设置PorterDuffXfermode，把圆角矩阵套在原Bitmap上取交集得到圆角Bitmap。
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bm,rect,rect,paint);
        return roundBitmap;
    }

    /**
     * 绘制圆形BitMap ---- 原理同绘制圆角矩形
     * 从圆角、圆形的处理上能看的出来绘制任意多边形都是可以的
     * @param bm 源BitMap
     * @return
     */
    public static Bitmap setCircle(Bitmap bm){
        int min = bm.getWidth() < bm.getHeight() ? bm.getWidth() :bm.getHeight();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap circleBitmap = Bitmap.createBitmap(min,min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        //绘制圆形
        canvas.drawCircle(min/2,min/2,min/2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //居中显示
        int left = -(bm.getWidth() - min) / 2;
        int top = -(bm.getHeight() - min) / 2;

        canvas.drawBitmap(bm,left,top,paint);
        return circleBitmap;
    }

    /**
     * drawable转换为Bitmap
     *
     * @param drawable 目标Drawable
     * @param width    要求宽度
     * @param height   要求的高度
     * @return 转换后的BitMap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable, int width, int height) {
        if (drawable == null) {
            return null;
        }
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap 转 Drawable
     */
    @SuppressWarnings("deprecation")
    public static Drawable bitmap2Drawable(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        BitmapDrawable bd = new BitmapDrawable(bm);
        bd.setTargetDensity(bm.getDensity());
        return new BitmapDrawable(bm);
    }

    /************************************ 图形变换 End************************************/


    /************************************ BitMap转变 ************************************/

    /**
     * 把bitmap转换成base64
     *
     * @param bitmap        目标BitMap
     * @param bitmapQuality
     * @return
     */
    public static String getBase64FromBitmap(Bitmap bitmap, int bitmapQuality) {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, bStream);
        byte[] bytes = bStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 把base64转换成bitmap
     */
    public static Bitmap getBitmapFromBase64(String string) {
        byte[] bitmapArray = null;
        try {
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }

    /**
     * Bitmap 转 byte[]
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte[] 转 Bitmap
     */
    public static Bitmap bytes2Bitmap(byte[] b) {
        if (b.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }


}
