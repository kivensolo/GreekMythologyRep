package com.module.tools;


import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;

/**
 * author：KingZ
 * date：2020/2/17
 * description：颜色工具类
 * https://blog.csdn.net/u010134293/article/details/52813756
 */
public class ColorUtils {

    /**
     * 兼容性的根据colorId获取颜色值
     */
    public static int getColor(Resources resources, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return resources.getColor(colorId, null);
        } else {
            //noinspection deprecation
            return resources.getColor(colorId);
        }
    }

    /**
     * 将指定颜色进行alpha转换
     * @param color  原始颜色值
     * @param alpha     颜色的新透明度程度  [0..1]
     * @return 带新透明度的颜色值
     */
    @ColorInt
    public static int replaceColorAlpha(@ColorInt int color,
                                        @FloatRange(from = 0.0, to = 1.0) float factor) {
        // ARGB 255,255,255,255   真色彩32位
        // 透明度占高8位  所以左移24位,确定透明度
        int a = Math.min(255, Math.max(0, (int) (factor * 255.0f))) << 24;
        // ARGB  00000000 11111111 11111111 11111111
        //将baseColor转为无透明度的颜色
        int rgb = 0x00ffffff & color;
        // A+RGB
        return a + rgb;

        /*int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);*/
    }

    /**
     * 生成随机颜色(透明度100%)
     * @return 随机颜色 ffxxxxxx
     */
    @ColorInt
    public static int randomColor() {
        return 0xff000000 |
                ((int) ((float) (255 * Math.random()) * 255.0f + 0.5f) << 16) |
                ((int) ((float) (255 * Math.random()) * 255.0f + 0.5f) << 8) |
                 (int) ((float) (255 * Math.random()) * 255.0f + 0.5f);
    }

    /**
     * 生成随机颜色(透明度也随机)
     * @return 随机的ARGB颜色值
     */
    @ColorInt
    public static int randomAlphaColor() {
        return ((int) ((float) (255 * Math.random()) * 255.0f + 0.5f) << 24) |
               ((int) ((float) (255 * Math.random()) * 255.0f + 0.5f) << 16) |
               ((int) ((float) (255 * Math.random()) * 255.0f + 0.5f) << 8) |
                (int) ((float) (255 * Math.random()) * 255.0f + 0.5f);
    }

    /**
     * 通过给定比例, 混合ARGB的色值;
     *
     * @param color1 第1个ARGB的颜色值
     * @param color2 第2个ARGB的颜色值
     * @param ratio  2个ARBG颜色值的混合比例;
     *               0.0则会得到color1，0.5会得到两个颜色均匀的混合值，1.0会得到color2;
     */
    @ColorInt
    public static int blendARGB(@ColorInt int color1, @ColorInt int color2,
            @FloatRange(from = 0.0, to = 1.0) float ratio) {
        final float inverseRatio = 1 - ratio;
        float a = Color.alpha(color1) * inverseRatio + Color.alpha(color2) * ratio;
        float r = Color.red(color1) * inverseRatio + Color.red(color2) * ratio;
        float g = Color.green(color1) * inverseRatio + Color.green(color2) * ratio;
        float b = Color.blue(color1) * inverseRatio + Color.blue(color2) * ratio;
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }

    @ColorInt
    public static int shiftColor(
            @ColorInt int color,
             @FloatRange(from = 0.0f, to = 2.0f) float by) {
        if (by == 1f) {
            return color;
        }
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= by; // value component
        return Color.HSVToColor(hsv);
    }

    @ColorInt
    public static int shiftColorDown(@ColorInt int color) {
        return shiftColor(color, 0.9f);
    }

    @ColorInt
    public static int shiftColorUp(@ColorInt int color) {
        return shiftColor(color, 1.1f);
    }
}
