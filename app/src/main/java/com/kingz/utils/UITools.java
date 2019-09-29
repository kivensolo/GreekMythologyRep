package com.kingz.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kingz.customdemo.R;
import com.module.tools.ScreenTools;

import java.lang.reflect.Method;

public class UITools {
    private final static String TAG = "UITools";

    /**
     * 获取容器控件中的第一个可以得到焦点的view
     *
     * @param parent 容器控件
     * @return 第一个view, 没有则返回空
     */
    public static final View getFirstFocusableView(ViewParent parent) {
        try {
            ViewGroup group = (ViewGroup) parent;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                if (group.getChildAt(i).getVisibility() == View.VISIBLE &&
                        group.getChildAt(i).isEnabled() &&
                        group.getChildAt(i).isFocusable()) {
                    return group.getChildAt(i);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getFirstFocusableView error:" + e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * 最近一次浮出框的时间
     */
    private static long lastTosatTime = 0;

    /**
     * @param width  width
     * @param height height
     * @Description: 设置一个View的宽高
     */
    public static void setViewSize(View view, int width, int height) {
        if (view == null) {
            Log.e(TAG, "setViewSize view==null");

            return;
        }
        if (view.getLayoutParams() == null) {
            Log.e(TAG, "setViewSize view.getLayoutParams()==null");
            return;
        }
        view.getLayoutParams().width = ScreenTools.Operation(width);
        view.getLayoutParams().height = ScreenTools.Operation(height);
    }

    /**
     *
     */
    public static void setViewMargin(View view, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        if (view == null) {
            Log.e(TAG, "setViewMargin view==null");
            return;
        }
        if (view.getLayoutParams() == null) {
            Log.e(TAG, "setViewMargin view.getLayoutParams()==null");
            return;
        }
        MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        params.rightMargin = rightMargin;
        params.bottomMargin = bottomMargin;
    }

    public final static void ShowCustomToast(Context context, String str) {
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.linearlayout_test, null);//id随便写的
        root.setBackgroundResource(R.drawable.android_shape_define); //id随便写的
        ImageView mIcon = (ImageView) root.findViewById(R.id.image_local); //id随便写的
        LinearLayout.LayoutParams mIconParams = (LinearLayout.LayoutParams) mIcon.getLayoutParams();
        mIconParams.width = ScreenTools.Operation(32);
        mIconParams.height = ScreenTools.Operation(32);
        mIconParams.bottomMargin = ScreenTools.Operation(5);
        mIconParams.topMargin = ScreenTools.Operation(5);
        mIconParams.leftMargin = ScreenTools.Operation(15);

        mIcon.setLayoutParams(mIconParams);

        TextView txtContent = (TextView) root.findViewById(android.R.id.message);
        txtContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenTools.Operation(24));
        txtContent.setMaxEms(20);
        txtContent.setShadowLayer(3, 1, 1, Color.BLACK);
        txtContent.setText(str);
        txtContent.setSingleLine();
        LinearLayout.LayoutParams mtxtParams = (LinearLayout.LayoutParams) txtContent.getLayoutParams();
        mtxtParams.rightMargin = ScreenTools.Operation(15);

        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, ScreenTools.Operation(0), ScreenTools.Operation(50));
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(root);
        toast.show();
    }


    /**
     * 通过系统反射方法强制隐藏状态栏
     * 适用系统应该是:
     * Build.VERSION.SDK_INT <  Build.VERSION_CODES.LOLLIPOP
     * 5.0后的系统,把{@link Context#STATUS_BAR_SERVICE}弄成hide的了
     *
     * @param context
     */
    public void hindStatuBarByInvoke(Context context) {
        try {
            Class<?> localClass2 = Class.forName("android.os.SystemProperties");
            Method localMethod2 = localClass2.getMethod("set", String.class,
                    String.class);
            String arg1 = "sys.statusbar.forcehide";
            String arg2 = "true";
            localMethod2.invoke(null, arg1, arg2);
            ZLog.i(TAG, "强制隐藏状态栏");
            Object localObject = context.getSystemService("statusbar");
            Class<?> localClass1 = localObject.getClass();
            int i = localClass1.getField("DISABLE_MASK").getInt(null);
            Method localMethod1 = localClass1.getMethod("disable", int.class);
            localMethod1.invoke(localObject, i);
            ZLog.i(TAG, "App hideStatusBar OK");
        } catch (Exception localException2) {
            localException2.printStackTrace();
        }
    }

}
