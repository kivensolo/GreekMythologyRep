package com.kingz.utils;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;

/**
 * author：KingZ
 * date：2019/7/31
 * description：view工具类
 */
public class ViewTools {

    /**
     * 当前view是否水平横向
     * @param v 判断水平方向的view
     * @return
     *  true: 该view水平横向
     *  fasle: 该view垂直方向
     */
    public static boolean isLandScape(View v){
        Resources resources = v.getResources();
        int state = resources.getConfiguration().orientation;
        return state == Configuration.ORIENTATION_LANDSCAPE;
    }
}
