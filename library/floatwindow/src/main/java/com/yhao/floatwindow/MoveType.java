package com.yhao.floatwindow;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 悬浮控件移动类型枚举定义类
 */
public class MoveType {
    //强制固定，不可设置新的位置
    static final int fixed = 0;
    //不可拖动，可以设置位置
    public static final int inactive = 1;
    //可拖动
    public static final int active = 2;
    //可拖动，释放后强制自动贴边 （默认）
    public static final int slide_force = 3;
    //可拖动，释放后靠近边缘一定距离才自动贴边
    public static final int slide_magnet = 4;
    //只可垂直拖动
    public static final int slide_vertical = 5;
    //可拖动，释放后自动回到原位置
    public static final int back = 6;

    @IntDef({fixed, inactive, active, slide_force, slide_vertical, slide_magnet, back})
    @Retention(RetentionPolicy.SOURCE)
    @interface MOVE_TYPE {
    }
}
