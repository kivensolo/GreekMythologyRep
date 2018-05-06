package com.kingz.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static android.graphics.Bitmap.Config.ARGB_8888;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/3/27 18:36
 * description: BitMap工具类
 * <p>
 * 知识点：
 * <p>
 * Android图像处理之Bitmap类:http://www.open-open.com/lib/view/open1333418945202.html
 * <p>
 * http://mp.weixin.qq.com/s?__biz=MzI3MDE0NzYwNA==&mid=2651433713&idx=1&sn=d152b053221c4c0bf1baa684b2a51e9c&scene=23&srcid=0805emSJb7dH8hdYfIcQiChP#rd
 * <p>
 * ||------> inBitmap属性 [Bitmap优化的一个方法]
 * 该属性表示重用该Bitmap的内存区域，避免多次重复向dvm申请开辟新的内存区域。
 * 可以一次性申请多个模板的内存，解码时候匹配使用申请的内存.如565模板、4444模板、8888模板。
 * 使用内存模板的方法，即使是上千张的图片，也只会仅仅只需要占用屏幕所能够显示的图片数量的内存大小。
 * 起作用的条件：
 * Bitmap:isMutable = true; //可变的
 * BitmapFactory.Options:  inSampleSize = 1;
 * The sample size is the number of pixels in either dimension that correspond to a single pixel in the decoded bitmap
 * 样本大小是在任一维度对应的解码位图的一个像素点的像素数。
 * 所以如果inSampleSize == 4 则说明原来一个像素点大小的位置要放四个像素点，
 * 所以图像的宽高将会变成原来的1/4，像素量会变成1/16.
 * 注意：解码器使用基于2的幂的最终值，任何其他值将舍入到最接近的2的幂。
 * <p>
 * ||=====>inJustDecodeBounds属性[图片加载避免OOM]
 * Options.inJustDecodeBounds = true的时候，解码的bitmap为null，只是把图片的宽高放在了Options里面。
 * 不给其分配内存空间，但是可以查询图片信息（options.outHeight (图片原始高度)和option.outWidth(图片原始宽度)）
 * <p>
 * <p>
 * ※※※※※※※※ Matrix ※※※※※※※※
 * 矩阵相关：
 * Android的位置3*3矩阵:
 * MSCALE_X    MSKEW_X     MTRANS_X
 * MSKEW_Y     MSCALE_Y    MTRANS_Y
 * MPERSP_0    MPERSP_1    MPERSP_2
 * MSCALE_X & MSCALE_Y ------- 控制比例
 * MSCALE_X & MSKEW_X & MSKEW_Y & MSCALE_Y ------- 控制旋转
 * MTRANS_X & MTRANS_Y ------- 控制平移
 * MPERSP_0 & MPERSP_1 & MPERSP_2 官方未明确介绍，似乎是控制透视的效果
 * <p>
 * Matrix matrix = new Matrix(); 生成的是单位矩阵：  |1 0 0|
 * |0 1 0|
 * |0 0 1|
 * 矩阵前乘  例如：变换矩阵为A，原始矩阵为B，pre方法的含义即是A*B：
 * pre系列方法：preScale，preTranslate，preRotate，preSkew，
 * set系列方法：setScale，setTranslate，setRotate，setSkew，
 * <p>
 * 矩阵后乘  例如：变换矩阵为A，原始矩阵为B，post方法的含义即是B*A
 * post系列方法：postScale，postTranslate，postRotate，postSkew。
 * <p>
 * 注意：后调用的pre操作先执行，而后调用的post操作则后执行。
 * <p>
 * //图片的默认矩阵
 * //      float[] values = {
 * //              1.0f, 0f, 0f,
 * //              0f, 1.0f, 0f,
 * //              0f, 0f, 1.0f
 * //      };
 */
public class BitMapUtils {

    private static final Bitmap.Config defaultConfig = ARGB_8888;
    public static final String TAG_RECYCLER = "BMP Recycler";
    public static final String TAG = BitMapUtils.class.getSimpleName();
    public static final boolean DEBUG = false;

    private static ThreadLocal<byte[]> _local_buf;
    private static ThreadLocal<BufferedInputStream> _localBufferedInputStream;

    static {
        _local_buf = new ThreadLocal<byte[]>();
        _localBufferedInputStream = new ThreadLocal<BufferedInputStream>();
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private BitMapUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /************************************ 图形变换  Start*****************************************/
    /**
     * 旋转图片
     * 效果是围绕Z轴
     *
     * @param angle  旋转角度
     * @param bitmap 目标图片
     * @return Bitmap    旋转后的图片
     */
    public static Bitmap setRotateImage(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix(); // 每一种变化都包括set，pre，post三种，分别为设置、矩阵先乘、矩阵后乘。
        matrix.postRotate(angle);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 旋转图片 ---- 围绕指定轴线
     *
     * @param bitmap 目标图片
     * @return Bitmap    旋转后的图片
     */
    public static Bitmap setRotateImage_XYZ(float rotate_x, float rotate_y, float rotate_z, Bitmap bitmap) {
        Camera camera = new Camera();
        Matrix matrix = new Matrix();
        //Canvas canvas = new Canvas(bitmap);
        camera.save();
        //canvas.save();
        camera.rotate(rotate_x, rotate_y, rotate_z);
        camera.getMatrix(matrix); //将单元矩阵应用于camera
        camera.restore();
        matrix.preTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
        matrix.postTranslate(bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        //canvas.concat(matrix);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 按比例缩放/裁剪图片
     * 通过矩阵方式
     */
    public static Bitmap setZoomImg(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 设置图片倾斜
     *
     * @param bm    源BitMap
     * @param skewX x轴倾斜度
     * @param skewY y轴倾斜度
     * @return
     */
    public static Bitmap setSkew(Bitmap bm, float skewX, float skewY) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 取得想要倾斜的matrix参数
        Matrix matrix = new Matrix();
        matrix.setSkew(skewX, skewY);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;

    }

    /**
     * 控制Matrix以px和py为轴心进行倾斜。
     * 例如，创建一个Matrix对象，并将其以(100,100)为轴心在X轴和Y轴上均倾斜0.1
     * Matrix m=new Matrix();
     * m.setSkew(0.1f,0.1f,100,100);
     *
     * @param bm
     * @param kx
     * @param ky
     * @param px
     * @param py
     * @return
     */
    public static Bitmap setSkew(Bitmap bm, float kx, float ky, float px, float py) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 取得想要倾斜的matrix参数
        Matrix matrix = new Matrix();
        matrix.setSkew(kx, ky, px, py);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 水平对称--图片关于X轴对称
     */
    private Bitmap testSymmetryX(Bitmap bm) {
        Matrix matrix = new Matrix();
        int height = bm.getHeight();
        int width = bm.getWidth();
        float matrixValues[] = {1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
        matrix.setValues(matrixValues);
        //若是matrix.postTranslate(0, height);//表示将图片上下倒置
        matrix.postTranslate(0, height * 2);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    /**
     * 设置圆角
     *
     * @param bm     源BitMap
     * @param radius 圆角弧度
     * @return
     */
    public static Bitmap setRoundCorner(Bitmap bm, int radius) {
        //初始化画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //准备裁剪的矩阵
        Rect srcRect = new Rect(0, 0, bm.getWidth(), bm.getHeight());     //Src
        RectF dstRect = new RectF(0, 0, bm.getWidth(), bm.getHeight());   //Dst

        //新建一个新位图
        Bitmap newBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        //圆角矩形(Dst)，radius为圆角大小
        canvas.drawRoundRect(dstRect, radius, radius, paint);
        //设置PorterDuffXfermode为SRC_IN --- 在Src上绘制两者的交集部分
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bm, srcRect, srcRect, paint);
        return newBitmap;
    }

    /**
     * 绘制圆形BitMap ---- 原理同绘制圆角矩形
     * 从圆角、圆形的处理上能看的出来绘制任意多边形都是可以的
     *
     * @param bm 源BitMap
     * @return
     */
    public static Bitmap setBitmapCircle(Bitmap bm) {
        int min = bm.getWidth() < bm.getHeight() ? bm.getWidth() : bm.getHeight();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap circleBitmap = Bitmap.createBitmap(min, min, ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        //绘制圆形
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //居中显示
        int left = -(bm.getWidth() - min) / 2;
        int top = -(bm.getHeight() - min) / 2;

        canvas.drawBitmap(bm, left, top, paint);
        return circleBitmap;
    }

    /**
     * 倒影效果
     *
     * @param bm           原图
     * @param reflectHight 倒影图高度
     * @return
     */
    public static Bitmap setInvertedBitmap(Bitmap bm, int reflectHight) {
        int reflectionGap = 4; //倒影图和原图之间的距离
        int width = bm.getWidth();
        int height = bm.getHeight();
        //创建单元矩阵
        Matrix matrix = new Matrix();
        //Scale不变，Y轴反转
        matrix.preScale(1, -1);
        //倒影图
        Bitmap reflectBitmap = Bitmap.createBitmap(bm, 0, height - reflectHight, width, reflectHight, matrix, false);
        //总长度的空BitMap
        Bitmap totalBitMap = Bitmap.createBitmap(width, height + reflectHight, ARGB_8888);
        Canvas canvas = new Canvas(totalBitMap);
        canvas.drawBitmap(bm, 0, 0, null);
        Paint paint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, paint); //两图间间隔
        canvas.drawBitmap(reflectBitmap, 0, height + reflectionGap, null); //绘制倒影图

        //画渐变蒙城
        //线性梯度渐变
        paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                bm.getHeight() + reflectionGap, 0,
                totalBitMap.getHeight() + reflectionGap,
                0x50000000, 0x90000000, Shader.TileMode.CLAMP);
        paint.setShader(shader);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN)); //倒影遮罩效果 取交集显示下面
        canvas.drawRect(0, height, width, totalBitMap.getHeight() + reflectionGap, paint);
        return totalBitMap;
    }

    public static Bitmap setInvertedBitmapById(Context context, int resId, int reflectHight) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resId);
        int reflectionGap = 30; //倒影图和原图之间的距离
        int width = bm.getWidth();
        int height = bm.getHeight();
        if (reflectHight == 0) {
            reflectHight = height / 3;
        }
        //创建单元矩阵
        Matrix matrix = new Matrix();
        //Scale不变，Y轴反转
        matrix.preScale(1, -1);
        //倒影图
        Bitmap reflectBitmap = Bitmap.createBitmap(bm, 0, height - reflectHight, width, reflectHight, matrix, false);
        //总长度的空BitMap
        Bitmap totalBitMap = Bitmap.createBitmap(width, height + reflectHight, ARGB_8888);
        Canvas canvas = new Canvas(totalBitMap);
        canvas.drawBitmap(bm, 0, 0, null);
        Paint paint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, paint); //两图间间隔
        canvas.drawBitmap(reflectBitmap, 0, height + reflectionGap, null); //绘制倒影图

        //画渐变蒙城
        //线性梯度渐变
        paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                bm.getHeight() + reflectionGap, 0,
                totalBitMap.getHeight() + reflectionGap,
                Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN)); //倒影遮罩效果 取交集显示下面
        canvas.drawRect(0, height + reflectionGap, width, totalBitMap.getHeight() + reflectionGap, paint);
        return totalBitMap;
    }

    /**
     * 生成水印图片
     *
     * @param photo     原图片
     * @param watermark 水印图片
     * @param mark_x    水印X坐标
     * @param mark_y    水印Y坐标
     * @return
     */
    public static Bitmap createWaterMarkBitmap(Bitmap photo, Bitmap watermark, int mark_x, int mark_y) {
        //左上角 mark_x = 0；mark_y=0;
        //右上角 mark_x = photo.getWidth() - watermark.getWidth()；mark_y=0;
        //左下角 mark_x = 0；mark_y=photo.getHeight() - watermark.getHeight();
        /*左上角 mark_x = photo.getWidth() - watermark.getWidth()；
        /mark_y = photo.getHeight() - watermark.getHeight();*/
        if (photo == null) {
            return null;
        }
        int photoWidth = photo.getWidth();
        int photoHeight = photo.getHeight();
        int markWidth = watermark.getWidth();
        int markHeight = watermark.getHeight();

        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(photoWidth, photoHeight, ARGB_8888);
        // 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);

        // draw src into
        // 在 0，0坐标开始画入src
        cv.drawBitmap(photo, 0, 0, null);
        // draw watermark into
        // 在src的右下角画入水印
        cv.drawBitmap(watermark, mark_x, mark_y, null);
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }

    /**
     * 生成水印文字
     *
     * @param photo  原图片
     * @param str    水印文字
     * @param mark_x 水印X坐标
     * @param mark_y 水印Y坐标
     * @return
     */
    public static Bitmap createWaterMarkText(Bitmap photo, String str, int mark_x, int mark_y) {
        int width = photo.getWidth();
        int hight = photo.getHeight();
        //建立一个空的BItMap
        Bitmap icon = Bitmap.createBitmap(width, hight, ARGB_8888);
        //初始化画布绘制的图像到icon上
        Canvas canvas = new Canvas(icon);

        Paint photoPaint = new Paint();     //建立画笔
        photoPaint.setDither(true);         //获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);   //过滤一些

        //创建一个指定的新矩形的坐标
        Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());
        //创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, hight);
        //将photo 缩放或则扩大到 dst使用的填充区photoPaint
        canvas.drawBitmap(photo, src, dst, photoPaint);

        //设置画笔
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        textPaint.setTextSize(20.0f);//字体大小
        //采用默认的宽度
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        //采用的颜色
        textPaint.setColor(Color.parseColor("#5FCDDA"));
        //影音的设置
        //textPaint.setShadowLayer(3f, 1, 1,this.getResources().getColor(android.R.color.background_dark));
        //绘制上去字，开始未知x,y采用那只笔绘制
        canvas.drawText(str, mark_x, mark_y, textPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return icon;
    }
    /**------------------------------------ 图形变换 End -----------------------------------*/


    /**------------------------------------ 图形色彩变换 Start -----------------------------------*/
    /**
     * 将彩色图转换为灰度图
     *
     * @param img 原Bitmap
     * @return 返回转换好的位图
     */
    public static Bitmap convertGreyImg(Bitmap img) {
        if (img == null) {
            return null;
        }
        int width = img.getWidth();
        int height = img.getHeight();
        Bitmap.Config imgConfig = img.getConfig();
        int[] pixels = new int[width * height];
        img.getPixels(pixels, 0, width, 0, 0, width, height);
        @SuppressWarnings("NumericOverflow")
        int alpha = 0xFF << 24;  //默认将bitmap当成24位真色彩图片
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, imgConfig);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    /**
     * 更改图片色系，变亮或变暗
     *
     * @param delta 图片的亮暗程度值，越小图片会越亮，取值范围(0,24)
     */
    public static Bitmap adjustTone(Bitmap src, int delta) {
        if (delta >= 24 || delta <= 0) {
            return null;
        }
        // 设置高斯矩阵
        int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int idx = 0;
        int[] pixels = new int[width * height];

        src.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR += (pixR * gauss[idx]);
                        newG += (pixG * gauss[idx]);
                        newB += (pixB * gauss[idx]);
                        idx++;
                    }
                }
                newR /= delta;
                newG /= delta;
                newB /= delta;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 将彩色图转换为黑白图
     *
     * @param bmp 位图
     * @return 返回转换好的位图
     */
    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap.Config imgConfig = bmp.getConfig();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        int alpha = 0xFF << 24; // 默认将bitmap当成24色图片
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap newBmp = Bitmap.createBitmap(width, height, imgConfig);
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }

    /**
     * Apply a blur to a Bitmap
     *
     * @param context    Application context
     * @param sentBitmap Bitmap to be converted
     * @param radius     Desired Radius, 0 < r < 25
     * @return a copy of the image with a blur
     */
    @SuppressLint("InlinedApi")
    public static Bitmap blur(Context context, Bitmap sentBitmap, int radius) {

        if (radius < 0) {
            radius = 0;
            if (DEBUG) {
                ZLog.w(TAG, "radius must be 0 < r < 25 , forcing radius=0");
            }
        } else if (radius > 25) {
            radius = 25;
            if (DEBUG) {
                ZLog.w(TAG, "radius must be 0 < r < 25 , forcing radius=25");
            }
        }

        if (Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs,
                    sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs,
                    input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
                    Element.U8_4(rs));
            script.setRadius(radius /* e.g. 3.f */);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }
    /**------------------------------------ 图形色彩变换 End -----------------------------------*/


    /**----------------------------------- BitMap转变  Start -----------------------------------*/


    /**
     * 从指定的view的缓存中获取Bitmap
     *
     * @param view View
     * @return view对应的bitmap
     */
    public static Bitmap getBitmapFromViewCache(View view) {
        view.clearFocus();
        view.setPressed(false);
        // 能画缓存就返回false
        boolean willNotCache = view.willNotCacheDrawing();
        view.setWillNotCacheDrawing(false);
        int color = view.getDrawingCacheBackgroundColor();
        view.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            view.destroyDrawingCache();
        }
        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        if (cacheBitmap == null) {
            if (DEBUG) {
                Log.e(TAG, "failed getViewBitmap(" + view + ")", new RuntimeException());
            }
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        // Restore the view
        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCache);
        view.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    /**
     * 从指定的view获取Bitmap
     *
     * @param view View
     * @return view对应的bitmap
     */
    public static Bitmap getBitMapFromView(View view) {
        if (view == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Create a Canvas which the View will be rendering.
        Canvas canvas = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        //Manually render this view (and all of its children) to the given Canvas.
        view.draw(canvas);
        return bitmap;
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
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
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

    /**
     * 合并Bitmap
     *
     * @param bgd 背景Bitmap
     * @param fg  前景Bitmap
     * @return 合成后的Bitmap
     */
    public static Bitmap combineImages(Bitmap bgd, Bitmap fg) {
        if (bgd == null || fg == null) {
            return bgd == null ? (fg == null ? null : fg) : bgd;
        }
        Bitmap bmp;
        int width = bgd.getWidth() > fg.getWidth() ? bgd.getWidth() : fg.getWidth();
        int height = bgd.getHeight() > fg.getHeight() ? bgd.getHeight() : fg.getHeight();
        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(bgd, 0, 0, null);
        canvas.drawBitmap(fg, 0, 0, paint);

        return bmp;
    }


    /**
     * Returns the in memory size of the given {@link Bitmap} in bytes.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static int getBitmapByteSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Workaround for KitKat initial release NPE in Bitmap, fixed in MR1. See issue #148.
            try {
                return bitmap.getAllocationByteCount();
            } catch (NullPointerException e) {
                // Do nothing.
            }
        }
        return bitmap.getHeight() * bitmap.getRowBytes();
    }

    /**
     * 根据指定编码格式获取Bitmap大小
     *
     * @param width
     * @param height
     * @param config
     * @return
     */
    public static int getBitmapByteSize(int width, int height, Bitmap.Config config) {
        return width * height * getBytesPerPixel(config);
    }

    private static int getBytesPerPixel(Bitmap.Config config) {
        // A bitmap by decoding a gif has null "config" in certain environments.
        if (config == null) {
            config = ARGB_8888;
        }

        int bytesPerPixel;
        switch (config) {
            case ALPHA_8:
                bytesPerPixel = 1;
                break;
            case RGB_565:
            case ARGB_4444:
                bytesPerPixel = 2;
                break;
            case ARGB_8888:
            default:
                bytesPerPixel = 4;
        }
        return bytesPerPixel;
    }


    /**
     * 获取应用内图片资源，获取得到的图片是原始尺寸*缩放系数
     *
     * @param res       Resources
     * @param resId     ResourcesId
     * @param reqWidth  reqWidth
     * @param reqHeight reqHeight
     * @return 解码后的位图
     */
    public static Bitmap decodeResource(Resources res, int resId, int reqWidth, int reqHeight) {
        TypedValue value = new TypedValue();
        res.openRawResource(resId, value);

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        //adjust screen density
        //options.inTargetDensity = value.density;

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeStream(InputStream inputStream) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        return BitmapFactory.decodeStream(inputStream, null, opts);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        //out size is more than request size.
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        ZLog.d("BitMapUtils", "calculateInSampleSize()  inSampleSize = " + inSampleSize);
        return inSampleSize;
    }

    /**
     * 手动设置缩放系数
     * TestCode
     *
     * @param inputStream
     * @return
     */
    public static Bitmap decodeStreamCustomOpts(InputStream inputStream) {
        TypedValue value = new TypedValue();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = 1;
        opts.inDensity = 160;
        opts.inTargetDensity = 160;
        return BitmapFactory.decodeStream(inputStream, null, opts);
    }

    public static Bitmap replaceBitmapColor(Bitmap oldBitmap, int oldColor, int newColor) {
        return replaceBitmapColor(oldBitmap, oldColor, newColor, ARGB_8888);
    }

    public static Bitmap replaceBitmapColor(Bitmap oldBitmap, int oldColor, int newColor, Bitmap.Config config) {
        Bitmap mBitmap = oldBitmap.copy(config, true);
        int mWidth = mBitmap.getWidth();
        int mHeight = mBitmap.getHeight();
        int mArrayColorLength = mWidth * mHeight;
        int currentPixelColor = 0;
        for (int i = 0; i < mHeight; i++) {
            for (int j = 0; j < mWidth; j++) {
                currentPixelColor = mBitmap.getPixel(j, i);
                if (currentPixelColor == oldColor) {
                    mBitmap.setPixel(j, i, newColor);
                }
            }
        }
        return mBitmap;
    }

    public static int calBitmapPixelsCount(Bitmap bmp) {
        return bmp.getWidth() * bmp.getHeight();
    }

    public static void addShadow(Canvas canvas, int shadowSize, int shadowColor,
                                 int offsetX, int offsetY,
                                 Bitmap srcBitmap, Paint paintShadow) {
        //绘制阴影，param1：模糊半径；param2：x轴大小：param3：y轴大小；param4：阴影颜色

        Rect rectSrc = new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight());
        Rect rectDst = new Rect(rectSrc);
        canvas.saveLayer(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), paintShadow, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        DrawUtils.offsetRect(rectDst, offsetX, offsetY);
        RectF rectF = new RectF(rectDst);
        paintShadow.setShadowLayer(shadowSize, 0, 0, shadowColor);

        canvas.drawRoundRect(rectF, 20f, 20f, paintShadow);
        canvas.restore();
        //
        //BlurMaskFilter blurFilter = new BlurMaskFilter(1,BlurMaskFilter.Blur.NORMAL);
        //Bitmap shadowBitmap = srcBitmap.extractAlpha(paintShadow,offsetXY);
        //Bitmap copy = shadowBitmap.copy(Bitmap.Config.ARGB_8888, true);

    }

    /**
     * @param bitmap
     * @param context
     * @param radius  模糊度 max:25.f
     * @return
     */
    public static Bitmap guassBlur(Bitmap bitmap, Context context, float radius) {
        // 用需要创建高斯模糊bitmap创建一个空的bitmap
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), ARGB_8888);

        // 初始化Renderscript，该类提供了RenderScript context，创建其他RS类之前必须先创建这个类，其控制RenderScript的初始化，资源管理及释放
        RenderScript rs = RenderScript.create(context);
        // 创建高斯模糊对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 创建Allocations，此类是将数据传递给RenderScript内核的主要方法，并制定一个后备类型存储给定类型
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //设定模糊度(注：Radius最大只能设置25.f)
        blurScript.setRadius(clamp(radius, 0f, 25.0f));

        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);
        // recycle the original bitmap
        bitmap.recycle();
        // After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return outBitmap;
    }

    public static float clamp(float values, float min, float max) {
        return Math.min(Math.max(values, min), max);
    }

    public static void recycleBitmap(Bitmap bmp) {
        if (bmp == null) {
            return;
        }
        if (!bmp.isMutable()) {
            if (DEBUG) {
                Log.e(TAG_RECYCLER, "bitmap immutable!!!");
            }
            return;
        }
        if (bmp.isRecycled()) {
            if (DEBUG) {
                Log.e(TAG_RECYCLER, "bitmap recycled!!!");
            }
            return;
        }
        int byteCount = calBitmapPixelsCount(bmp);
        if (byteCount <= 0) {
            if (DEBUG) {
                Log.e(TAG_RECYCLER, "invalid bitmap bytecount! " + byteCount);
            }
            return;
        }
    }


}
