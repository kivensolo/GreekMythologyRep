package com.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.widget.ScrollView;

/**
 * author: King.Z <br>
 * date:  2017/7/10 0:31 <br>
 * description: 截屏工具类 <br>
 */

public class ScreenShotUtils {

    /**
     * View对象转Bitmap
     * 在View类中的onDraw方法的参数Canvas是View绘制的背景，
     * 要将View转换为Bitmap实际上就是让Canvas上的绘制操作绘制到Bitmap上。
     * View转化为Bitmap也称为截屏
     *
     * protected void onDraw(Canvas canvas)
     * public void buildDrawingCache()
     * public void destroyDrawingCache()
     * public Bitmap getDrawingCache()
     * public void setDrawingCacheEnabled(boolean enabled)
     *
     * @param view
     * @return
     */
    public static final Bitmap screenShot(View view) {
        if (null == view) {
            throw new IllegalArgumentException("parameter can't be null.");
        }
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    /**
     * Activity转Bitmap不带状态栏
     * @param activity
     * @return
     */
    public final Bitmap screenShot(Activity activity) {
        if (null == activity) {
            throw new IllegalArgumentException("parameter can't be null.");
        }
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);

        int statusBarHeight = frame.top;

        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);

        int width = point.x;
        int height = point.y;

        Bitmap b2 = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b2;
    }

    /**
     *  ScrollView转长Bitmap(类似锤子便签的截长图)
     * @param scrollView  所需截图的ScrollView
     * @return
     */
    public final Bitmap screenShot(ScrollView scrollView) {
       if (null == scrollView) {
           throw new IllegalArgumentException("parameter can't be null.");
       }
       int height = 0;
       Bitmap bitmap;
       for (int i = 0, s = scrollView.getChildCount(); i < s; i++) {
           height += scrollView.getChildAt(i).getHeight();
           scrollView.getChildAt(i).setBackgroundResource(android.R.drawable.screen_background_light);
       }
       bitmap = Bitmap.createBitmap(scrollView.getWidth(), height, Bitmap.Config.ARGB_8888);
       final Canvas canvas = new Canvas(bitmap);
       scrollView.draw(canvas);
       return bitmap;
   }

}
