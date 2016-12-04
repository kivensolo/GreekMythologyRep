package com.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Base64;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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
 *
 * ※※※※※※※※ decodeResource()和decodeFile()  ※※※※※※※※
 *  decodeFile()用于读取SD卡上的图，得到的是图片的原始尺寸
 *  decodeResource()用于读取Res、Raw等资源，得到的是图片的原始尺寸 * 缩放系数，
 *
 *  缩放系数依赖于屏幕密度，参数可以通过BitMapFactory.Option的几个参数调整。
 *  public boolean inScaled    //默认True
 *  public int inDensity;       //无dip的文件夹下默认160
 *  public int inTargetDensity; //取决具体屏幕
 *  |---------> 缩放系数 = inTargetDensity / inDensity;
 * <p>
 *
 * 现在有一张720×720的图片:
 * ||------> inScaled属性
 *  如果inScaled设置为false，则不进行缩放，解码后图片大小为720×720;
 *  如果inScaled设置为true或者不设置，则根据inDensity和inTargetDensity计算缩放系数。
 *      ※ 【默认情况】
 *      把这张图片放到drawable目录下, 默认：
            以720p的红米3为例子，
            缩放系数 = inTargetDensity(具体320 / inDensity（默认160）= 2 = density，
            解码后图片大小为1440×1440。
            以1080p的MX4为例子，
            缩放系数 = inTargetDensity(具体480 / inDensity（默认160）= 3 = density,
            解码后图片大小为2160×2160。
 *      ※ 【dpi文件夹的影响】
 *          把图片放到drawable或者raw这样不带dpi的文件夹，会按照上面的算法计算。
 *          如果放到xhdpi会怎样呢？ 在MX4上，放到xhdpi，解码后图片大小为1080 x 1080。
 *          因为放到有dpi的文件夹，会影响到inDensity的默认值，放到xhdpi为160 x 2 = 320;
 *          所以缩放系数 = 480（屏幕） / 320 （xhdpi） = 1.5; 所以得到的图片大小为1080 x 1080。
 *
 * ※※※※※※※※ Matrix ※※※※※※※※
 * 矩阵相关：
 *       矩阵前乘  例如：变换矩阵为A，原始矩阵为B，pre方法的含义即是A*B：
 *          pre系列方法：preScale，preTranslate，preRotate，preSkew，
 *          set系列方法：setScale，setTranslate，setRotate，setSkew，
 *       矩阵后乘  例如：变换矩阵为A，原始矩阵为B，post方法的含义即是B*A
 *          post系列方法：postScale，postTranslate，postRotate，postSkew。
 *
 *          注意：后调用的pre操作先执行，而后调用的post操作则后执行。
 *
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
    public static Bitmap setRotateImage(int angle, Bitmap bitmap) {
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
    public static Bitmap setZoomImg(Bitmap bm, int newWidth, int newHeight) {
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
        Rect rect = new Rect(0, 0, bm.getWidth(), bm.getHeight());   //Src
        RectF rectF = new RectF(0, 0, bm.getWidth(), bm.getHeight());  //Dst

        //新建一个新的输出图片
        Bitmap roundBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBitmap);
        //圆角矩形(Dst)，radius为圆角大小
        canvas.drawRoundRect(rectF, radius, radius, paint);
        //设置PorterDuffXfermode，把圆角矩形(Dst)套在原Bitmap(Src)上取交集得到圆角Bitmap。
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bm, rect, rect, paint);
        return roundBitmap;
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
        Bitmap circleBitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
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
     * @param bm                原图
     * @param reflectHight      倒影图高度
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
        Bitmap totalBitMap = Bitmap.createBitmap(width, height + reflectHight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(totalBitMap);
        canvas.drawBitmap(bm, 0, 0, null);
        Paint paint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, paint); //两图间间隔
        canvas.drawBitmap(reflectBitmap, 0, height + reflectionGap, null); //绘制倒影图

        //画渐变蒙城
        //线性梯度渐变
        paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                bm.getHeight()+reflectionGap, 0,
                totalBitMap.getHeight() + reflectionGap,
                0x50000000,0x90000000,Shader.TileMode.CLAMP);
        paint.setShader(shader);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN)); //倒影遮罩效果 取交集显示下面
        canvas.drawRect(0, height, width, totalBitMap.getHeight() + reflectionGap, paint);
        return totalBitMap;
    }

    public static Bitmap setInvertedBitmapById(Context context,int resId, int reflectHight) {
        Bitmap bm= BitmapFactory.decodeResource(context.getResources(),resId);
        int reflectionGap = 30; //倒影图和原图之间的距离
        int width = bm.getWidth();
        int height = bm.getHeight();
        if(reflectHight == 0){
            reflectHight = height/3;
        }
        //创建单元矩阵
        Matrix matrix = new Matrix();
        //Scale不变，Y轴反转
        matrix.preScale(1, -1);
        //倒影图
        Bitmap reflectBitmap = Bitmap.createBitmap(bm, 0, height - reflectHight, width, reflectHight, matrix, false);
        //总长度的空BitMap
        Bitmap totalBitMap = Bitmap.createBitmap(width, height + reflectHight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(totalBitMap);
        canvas.drawBitmap(bm, 0, 0, null);
        Paint paint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, paint); //两图间间隔
        canvas.drawBitmap(reflectBitmap, 0, height + reflectionGap, null); //绘制倒影图

        //画渐变蒙城
        //线性梯度渐变
        paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                bm.getHeight()+reflectionGap, 0,
                totalBitMap.getHeight() + reflectionGap,
                Color.BLACK,Color.TRANSPARENT,Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN)); //倒影遮罩效果 取交集显示下面
        canvas.drawRect(0, height+reflectionGap, width, totalBitMap.getHeight() + reflectionGap, paint);
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
        Bitmap newb = Bitmap.createBitmap(photoWidth, photoHeight, Bitmap.Config.ARGB_8888);
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
        Bitmap icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888);
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

    /************************************ 图形变换 End************************************/


    /************************************ BitMap转变 ************************************/


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
            config = Bitmap.Config.ARGB_8888;
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
     * 获取Resource 图片资源
     * @param resources
     * @param id
     * @return
     */
    public static Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

    public static Bitmap decodeStream(InputStream inputStream) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        return BitmapFactory.decodeStream(inputStream,null, opts);
    }
    /**
     * 手动设置缩放系数
     * TestCode
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
        return BitmapFactory.decodeStream(inputStream,null, opts);
    }


}
