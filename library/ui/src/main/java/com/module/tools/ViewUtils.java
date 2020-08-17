package com.module.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * author: King.Z
 * date:  2016/7/21 15:38
 * description: View相关的工具类
 */
public class ViewUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private ViewUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static void measureWidthAndHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
    }

    /**
     * 修改整个界面所有控件的字体
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
        setViewBorder(v, canvas, 1, Color.CYAN, borderPaint);
    }

    public static void setViewBorder(View v, Canvas canvas, int width, int color, Paint borderPaint) {
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(width);
        borderPaint.setColor(color);
        canvas.drawRect(1, 1, v.getMeasuredWidth(), v.getMeasuredHeight() - 1, borderPaint);
    }

    /**
     * 当前view是否水平横向
     *
     * @param v 判断水平方向的view
     * @return true: 该view水平横向
     * fasle: 该view垂直方向
     */
    public static boolean isLandScape(View v) {
        Resources resources = v.getResources();
        int state = resources.getConfiguration().orientation;
        return state == Configuration.ORIENTATION_LANDSCAPE;
    }


    //临时添加代码

    /**
     * 动态获取水波纹效果的Drawable
     *
     * @param context
     * @return
     */
    public Drawable getRippleBackgroundDrawable(Context context) {
        Drawable drawable = null;
        try {
            if (context != null) {
                TypedValue typedValue = new TypedValue();
                context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
                int[] attribute = new int[]{android.R.attr.selectableItemBackground};
                TypedArray typedArray = context.getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
                drawable = typedArray.getDrawable(0);
                typedArray.recycle();
            }
        } catch (Exception ignored) {
        }
        return drawable;
    }


    /**
     * 判断RecyclerView是否滑动到底，然后进行跳转Activity
     *
     * @param recyclerView
     * @param cxt
     * @param clazz         跳转的Activity
     * @param direction    RecyclerView可以滑动的方向
     * @param paramas          传递的参数
     */
    public void scrollEndEnterActivity(RecyclerView recyclerView, Context cxt, Class<?> clazz, int direction,
                                       Pair<String,String>... paramas) {
        try {
            switch (direction) {
                case RecyclerView.HORIZONTAL:
                    //判断不能左滑，即互动到底
                    if (recyclerView != null && !recyclerView.canScrollHorizontally(1)) {
                        startActivityWithParamas(cxt, clazz, paramas);
                    }
                    break;
                case RecyclerView.VERTICAL:
                    //判断不能上滑，即互动到底
                    if (recyclerView != null && !recyclerView.canScrollVertically(1)) {
                        cxt.startActivity(new Intent(cxt, clazz));
                    }
                    break;
                default:
                    break;

            }
        } catch (Exception ignored) {}
    }

    /**
     * 启动Activity
     *
     * @param cxt
     * @param cls
     * @param paramas
     */
    public void startActivityWithParamas(Context cxt, Class<?> cls, Pair<String,String>... paramas) {
        try {
            if (cxt != null) {
                Intent intent = new Intent(cxt, cls);
                for (Pair<String, String> pair : paramas) {
                        intent.putExtra(pair.first, pair.second);
                }
                cxt.startActivity(intent);
            }
        } catch (Exception ignored) {}
    }

    /**
     * 获取View的截图
     *
     * @param view view
     * @return Bitmap
     */
    public Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = null;
        if (view != null) {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            bitmap = view.getDrawingCache();
        }
        return bitmap;
    }
}
