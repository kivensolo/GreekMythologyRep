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

    /**
     * 调整颜色亮度
     * @param color Tthe argb color to convert. The alpha component is ignored.
     * @param by  The offset of Value in HSV.
     * @return The argb color of converted.
     */
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

    //<editor-fold desc="预制渐变颜色数组">

    /**
     * 冰蓝光晕：冷色调玻璃高光效果，适合 Surface 扫光
     */
    public static final int[] ICE_BLUE = {
            Color.TRANSPARENT,
            Color.parseColor("#1A00BFFF"),
            Color.parseColor("#5587CEEB"),
            Color.parseColor("#CCE0FFFF"),
            Color.parseColor("#FFFFFF"),
            Color.parseColor("#CCE0FFFF"),
            Color.parseColor("#5587CEEB"),
            Color.parseColor("#1A00BFFF"),
            Color.TRANSPARENT
    };

    /**
     * 金色流光：暖色调金属光泽效果
     */
    public static final int[] GOLDEN_SHIMMER = {
            Color.TRANSPARENT,
            Color.parseColor("#10FFD700"),
            Color.parseColor("#55FFA500"),
            Color.parseColor("#BBFFD700"),
            Color.parseColor("#FFFFFF"),
            Color.parseColor("#BBFFD700"),
            Color.parseColor("#55FFA500"),
            Color.parseColor("#10FFD700"),
            Color.TRANSPARENT
    };

    /**
     * 日落暖光：橙红渐变，从暗到亮的暖色
     */
    public static final int[] SUNSET_GLOW = {
            Color.parseColor("#0DFF4500"),
            Color.parseColor("#33FF6347"),
            Color.parseColor("#66FFD700"),
            Color.parseColor("#CCFF4500"),
            Color.parseColor("#FFFF6347")
    };

    /**
     * 极光绿：青绿到亮白的北极光效果
     */
    public static final int[] AURORA_GREEN = {
            Color.TRANSPARENT,
            Color.parseColor("#1000FF7F"),
            Color.parseColor("#3300CED1"),
            Color.parseColor("#7700FF7F"),
            Color.parseColor("#CC7FFFD4"),
            Color.parseColor("#FFFFFF"),
            Color.parseColor("#CC7FFFD4"),
            Color.parseColor("#7700FF7F"),
            Color.parseColor("#3300CED1"),
            Color.parseColor("#1000FF7F"),
            Color.TRANSPARENT
    };

    /**
     * 赛博紫：紫到品红的霓虹效果
     */
    public static final int[] CYBER_PURPLE = {
            Color.TRANSPARENT,
            Color.parseColor("#159B59FF"),
            Color.parseColor("#33BF40BF"),
            Color.parseColor("#77D500F9"),
            Color.parseColor("#BBE040FB"),
            Color.parseColor("#FFE040FB"),
            Color.parseColor("#BBE040FB"),
            Color.parseColor("#77D500F9"),
            Color.parseColor("#33BF40BF"),
            Color.parseColor("#159B59FF"),
            Color.TRANSPARENT
    };

    /**
     * 玫瑰金：粉金色调的柔和光泽
     */
    public static final int[] ROSE_GOLD = {
            Color.TRANSPARENT,
            Color.parseColor("#15FFB6C1"),
            Color.parseColor("#44F4A460"),
            Color.parseColor("#77FFDAB9"),
            Color.parseColor("#BBFFB6C1"),
            Color.parseColor("#FFFAF0E6"),
            Color.parseColor("#BBFFB6C1"),
            Color.parseColor("#77FFDAB9"),
            Color.parseColor("#44F4A460"),
            Color.parseColor("#15FFB6C1"),
            Color.TRANSPARENT
    };

    /**
     * 翡翠青：深青到浅绿的玉石质感
     */
    public static final int[] JADE_CYAN = {
            Color.TRANSPARENT,
            Color.parseColor("#15008B8B"),
            Color.parseColor("#33008080"),
            Color.parseColor("#6620B2AA"),
            Color.parseColor("#AA40E0D0"),
            Color.parseColor("#DDA0FFE0"),
            Color.parseColor("#AA40E0D0"),
            Color.parseColor("#6620B2AA"),
            Color.parseColor("#33008080"),
            Color.parseColor("#15008B8B"),
            Color.TRANSPARENT
    };

    /**
     * 银白闪光：纯白金属高光扫过效果
     */
    public static final int[] SILVER_FLASH = {
            Color.TRANSPARENT,
            Color.parseColor("#0AC0C0C0"),
            Color.parseColor("#22D3D3D3"),
            Color.parseColor("#55E8E8E8"),
            Color.parseColor("#AAF0F0F0"),
            Color.parseColor("#DDFFFFFF"),
            Color.parseColor("#FFFFFF"),
            Color.parseColor("#DDFFFFFF"),
            Color.parseColor("#AAF0F0F0"),
            Color.parseColor("#55E8E8E8"),
            Color.parseColor("#22D3D3D3"),
            Color.parseColor("#0AC0C0C0"),
            Color.TRANSPARENT
    };

    /**
     * 钴蓝烈焰：深蓝到亮蓝的高对比光带
     */
    public static final int[] COBALT_FLAME = {
            Color.TRANSPARENT,
            Color.parseColor("#150000CD"),
            Color.parseColor("#331E90FF"),
            Color.parseColor("#664169E1"),
            Color.parseColor("#AA6495ED"),
            Color.parseColor("#DD87CEEB"),
            Color.parseColor("#FFADD8E6"),
            Color.parseColor("#DD87CEEB"),
            Color.parseColor("#AA6495ED"),
            Color.parseColor("#664169E1"),
            Color.parseColor("#331E90FF"),
            Color.parseColor("#150000CD"),
            Color.TRANSPARENT
    };

    /**
     * 薄暮橙粉：从暗紫到橙粉的晚霞效果
     */
    public static final int[] DUSK_CORAL = {
            Color.TRANSPARENT,
            Color.parseColor("#15FF7F50"),
            Color.parseColor("#33FF6B6B"),
            Color.parseColor("#66FF8C69"),
            Color.parseColor("#AAFFA07A"),
            Color.parseColor("#DDFFB347"),
            Color.parseColor("#FFFFD700"),
            Color.parseColor("#DDFFB347"),
            Color.parseColor("#AAFFA07A"),
            Color.parseColor("#66FF8C69"),
            Color.parseColor("#33FF6B6B"),
            Color.parseColor("#15FF7F50"),
            Color.TRANSPARENT
    };

    //</editor-fold>
}
