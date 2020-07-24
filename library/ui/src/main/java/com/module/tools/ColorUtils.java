package com.module.tools;


import android.content.res.Resources;
import android.os.Build;

/**
 * author：KingZ
 * date：2020/2/17
 * description：颜色工具类
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
     * @param alpha     透明度程度  有效值0~1.0f
     * @param srcColor  原始颜色值
     * @return 带透明度的颜色值
     */
    public static int getColorWithAlpha(float alpha, int srcColor) {
        // ARGB 255,255,255,255   真色彩32位
        // 透明度占高8位  所以左移24位,确定透明度
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        // ARGB  00000000 11111111 11111111 11111111
        //      将baseColor转为无透明度的颜色
        int rgb = 0x00ffffff & srcColor;
        // A+RGB
        return a + rgb;
    }

    /**
     * 生成随机颜色
     * @return 随机颜色 ffxxxxxx
     */
    public static int randomColor() {
        return 0xff000000 |
                ((int) ((float) (255 * Math.random()) * 255.0f + 0.5f) << 16) |
                ((int) ((float) (255 * Math.random()) * 255.0f + 0.5f) << 8) |
                (int) ((float) (255 * Math.random()) * 255.0f + 0.5f);
    }
}
