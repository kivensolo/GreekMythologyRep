package com.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/3/27 18:36
 * description: BitMap工具类
 */
public class BitMapUtils {
    /**
	 * 旋转图片
	 *
	 * @param angle     旋转角度
	 * @param bitmap	目标图片
	 * @return Bitmap	旋转后的图片
	 */
	public static Bitmap rotateImage(int angle, Bitmap bitmap) {
		// 图片旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 得到旋转后的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * drawable转换为Bitmap
	 * @param drawable  目标Drawable
	 * @param width		要求宽度
	 * @param height	要求的高度
     * @return  转换后的BitMap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width,height,
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width,height);
        drawable.draw(canvas);
        return bitmap;
    }


}