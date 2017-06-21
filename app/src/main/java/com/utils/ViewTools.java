package com.utils;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * author: King.Z
 * date:  2016/7/21 15:38
 * description: View相关的工具类
 */
public class ViewTools {

     /**
     * 测量View的宽高
     *
     * @param view View
     */
    public static void measureWidthAndHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
    }

    /**
     *  修改整个界面所有控件的字体
     */
    public static void changeFonts(ViewGroup root, String path, Activity act) {
        //path是字体路径
        Typeface tf = Typeface.createFromAsset(act.getAssets(), path);
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(tf);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(tf);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(tf);
            } else if (v instanceof ViewGroup) {
                changeFonts((ViewGroup) v, path, act);
            }
        }
    }

    // 修改整个界面所有控件的字体大小
    public static void changeTextSize(ViewGroup root, int size, Activity act) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof Button) {
                ((Button) v).setTextSize(size);
            } else if (v instanceof EditText) {
                ((EditText) v).setTextSize(size);
            } else if (v instanceof TextView) {
                ((TextView) v).setTextSize(size);
            } else if (v instanceof ViewGroup) {
                changeTextSize((ViewGroup) v, size, act);
            }
        }
    }

    // 不改变控件位置，修改控件大小
    public static void changeItemWH(View v, int W, int H) {
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.width = W;
        params.height = H;
        v.setLayoutParams(params);
    }

    public static void setViewBorder(View v, Canvas canvas, Paint borderPaint) {
        setViewBorder(v,canvas,1,Color.CYAN,borderPaint);
    }

    public static void setViewBorder(View v, Canvas canvas, int width, int color, Paint borderPaint) {
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(width);
        borderPaint.setColor(color);
        canvas.drawRect(1, 1, v.getMeasuredWidth(), v.getMeasuredHeight() - 1, borderPaint);
    }



}
