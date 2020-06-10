package com.kingz.utils;


import android.content.res.Resources;
import android.os.Build;

/**
 * author：KingZ
 * date：2020/2/17
 * description：颜色兼容性工具类
 */
public class ColorCompatUtils {

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
     * @param alpha     透明度程度  0~1.0f
     * @param baseColor 颜色值
     * @return 新的颜色值
     */
    public static int getColorWithAlpha(float alpha, int baseColor) {
        // ARGB 255,255,255,255   真色彩32位
        // 透明度占高8位  所以左移24位,确定透明度
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        // ARGB  00000000 11111111 11111111 11111111
        //      将baseColor转为无透明度的颜色
        int rgb = 0x00ffffff & baseColor;
        // A+RGB
        return a + rgb;
    }
}
