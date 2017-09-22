package com.kingz.utils;

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

 * ||------> inBitmap属性 [Bitmap优化的一个方法]
 *      该属性表示重用该Bitmap的内存区域，避免多次重复向dvm申请开辟新的内存区域。
 * 可以一次性申请多个模板的内存，解码时候匹配使用申请的内存.如565模板、4444模板、8888模板。
 * 使用内存模板的方法，即使是上千张的图片，也只会仅仅只需要占用屏幕所能够显示的图片数量的内存大小。
 * 起作用的条件：
 * Bitmap:isMutable = true; //可变的
 * BitmapFactory.Options:  inSampleSize = 1;
 *  The sample size is the number of pixels in either dimension that correspond to a single pixel in the decoded bitmap
 *  样本大小是在任一维度对应的解码位图的一个像素点的像素数。
 *  所以如果inSampleSize == 4 则说明原来一个像素点大小的位置要放四个像素点，
 *  所以图像的宽高将会变成原来的1/4，像素量会变成1/16.
 *  注意：解码器使用基于2的幂的最终值，任何其他值将舍入到最接近的2的幂。
 *
 * ||=====>inJustDecodeBounds属性[图片加载避免OOM]
 *      Options.inJustDecodeBounds = true的时候，解码的bitmap为null，只是把图片的宽高放在了Options里面。
 *  不给其分配内存空间，但是可以查询图片信息（options.outHeight (图片原始高度)和option.outWidth(图片原始宽度)）
 *
 *
 * ※※※※※※※※ Matrix ※※※※※※※※
 * 矩阵相关：
 * Android的位置3*3矩阵:
 *  MSCALE_X    MSKEW_X     MTRANS_X
 *  MSKEW_Y     MSCALE_Y    MTRANS_Y
 *  MPERSP_0    MPERSP_1    MPERSP_2
 *  MSCALE_X & MSCALE_Y ------- 控制比例
 *  MSCALE_X & MSKEW_X & MSKEW_Y & MSCALE_Y ------- 控制旋转
 *  MTRANS_X & MTRANS_Y ------- 控制平移
 *  MPERSP_0 & MPERSP_1 & MPERSP_2 官方未明确介绍，似乎是控制透视的效果
 *
 *  Matrix matrix = new Matrix(); 生成的是单位矩阵：  |1 0 0|
 *                                                   |0 1 0|
 *                                                   |0 0 1|
 *       矩阵前乘  例如：变换矩阵为A，原始矩阵为B，pre方法的含义即是A*B：
 *          pre系列方法：preScale，preTranslate，preRotate，preSkew，
 *          set系列方法：setScale，setTranslate，setRotate，setSkew，
 *
 *       矩阵后乘  例如：变换矩阵为A，原始矩阵为B，post方法的含义即是B*A
 *          post系列方法：postScale，postTranslate，postRotate，postSkew。
 *
 *          注意：后调用的pre操作先执行，而后调用的post操作则后执行。
 *
 *  //图片的默认矩阵
 *  //      float[] values = {
 *  //              1.0f, 0f, 0f,
 *  //              0f, 1.0f, 0f,
 *  //              0f, 0f, 1.0f
 *  //      };
 */
public class BitMapUtils {

    private static final Bitmap.Config defaultConfig = ARGB_8888;
	public static final String TAG_RECYCLER = "BMP Recycler";
	public static final boolean DEBUG = false;

    private static ThreadLocal<byte[]> _local_buf;
    private static ThreadLocal<BufferedInputStream> _localBufferedInputStream;

    static {
        _local_buf = new ThreadLocal<byte[]>();
        _localBufferedInputStream = new ThreadLocal<BufferedInputStream>();
    }


    private BitMapUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /************************************ 图形变换  Start*****************************************/
    /**
     * 旋转图片
     * 效果是围绕Z轴
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
     * 旋转图片 ---- 围绕指定轴线
     *
     * @param bitmap 目标图片
     * @return Bitmap    旋转后的图片
     */
    public static Bitmap setRotateImage_XYZ(float rotate_x,float rotate_y,float rotate_z,Bitmap bitmap) {
        Camera camera = new Camera();
        Matrix matrix = new Matrix();
        //Canvas canvas = new Canvas(bitmap);
        camera.save();
        //canvas.save();
        camera.rotate(rotate_x,rotate_y,rotate_z);
        camera.getMatrix(matrix); //将单元矩阵应用于camera
        camera.restore();
        matrix.preTranslate(-bitmap.getWidth()/2,-bitmap.getHeight()/2);
        matrix.postTranslate(bitmap.getWidth()/2,bitmap.getHeight()/2);
        //canvas.concat(matrix);
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
     * 水平对称--图片关于X轴对称
     */
	private Bitmap testSymmetryX(Bitmap bm) {
		Matrix matrix = new Matrix();
		int height =bm.getHeight();
		int width =bm.getWidth();
		float matrixValues[] = { 1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f };
		matrix.setValues(matrixValues);
		//若是matrix.postTranslate(0, height);//表示将图片上下倒置
		matrix.postTranslate(0, height*2);
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
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? ARGB_8888 : Bitmap.Config.RGB_565);
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
     * @param res Resources
     * @param resId        ResourcesId
     * @param reqWidth  reqWidth
     * @param reqHeight reqHeight
     * @return 解码后的位图
     */
    public static Bitmap decodeResource(Resources res, int resId,int reqWidth,int reqHeight) {
        TypedValue value = new TypedValue();
        res.openRawResource(resId, value);

         // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options =new BitmapFactory.Options();
        options.inJustDecodeBounds =true;
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
        return BitmapFactory.decodeStream(inputStream,null, opts);
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
        ZLog.d("BitMapUtils","calculateInSampleSize()  inSampleSize = " + inSampleSize);
        return inSampleSize;
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

    public static Bitmap replaceBitmapColor(Bitmap oldBitmap,int oldColor,int newColor){
        return replaceBitmapColor(oldBitmap,oldColor,newColor, ARGB_8888);
    }

    public static Bitmap replaceBitmapColor(Bitmap oldBitmap, int oldColor, int newColor, Bitmap.Config config){
        Bitmap mBitmap = oldBitmap.copy(config,true);
        int mWidth = mBitmap.getWidth();
        int mHeight = mBitmap.getHeight();
        int mArrayColorLength = mWidth * mHeight;
        int currentPixelColor = 0;
        for(int i=0; i < mHeight;i++){
            for(int j=0;j < mWidth;j++){
                currentPixelColor = mBitmap.getPixel(j,i);
                if(currentPixelColor == oldColor){
                    mBitmap.setPixel(j,i,newColor);
                }
            }
        }
        return mBitmap;
    }

    public static int calBitmapPixelsCount(Bitmap bmp) {
		return bmp.getWidth() * bmp.getHeight();
	}

	public static void addShadow(Canvas canvas,int shadowSize, int shadowColor,
                                 int offsetX,int offsetY,
                                 Bitmap srcBitmap,Paint paintShadow){
        //绘制阴影，param1：模糊半径；param2：x轴大小：param3：y轴大小；param4：阴影颜色

        Rect rectSrc = new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight());
		Rect rectDst = new Rect(rectSrc);
        canvas.saveLayer(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), paintShadow,Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        DrawUtils.offsetRect(rectDst,offsetX,offsetY);
        RectF rectF = new RectF(rectDst);
        paintShadow.setShadowLayer(shadowSize,0,0,shadowColor);

        canvas.drawRoundRect(rectF, 20f, 20f, paintShadow);
        canvas.restore();
        //
        //BlurMaskFilter blurFilter = new BlurMaskFilter(1,BlurMaskFilter.Blur.NORMAL);
        //Bitmap shadowBitmap = srcBitmap.extractAlpha(paintShadow,offsetXY);
        //Bitmap copy = shadowBitmap.copy(Bitmap.Config.ARGB_8888, true);

    }

    /**
     *
     * @param bitmap
     * @param context
     * @param radius 模糊度 max:25.f
     * @return
     */
	public static Bitmap guassBlur(Bitmap bitmap, Context context,float radius) {
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
        blurScript.setRadius(clamp(radius,0f,25.0f));

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

    public static float clamp(float values, float min, float max){
        return Math.min(Math.max(values,min),max);
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
