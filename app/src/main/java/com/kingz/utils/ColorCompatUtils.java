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
}
