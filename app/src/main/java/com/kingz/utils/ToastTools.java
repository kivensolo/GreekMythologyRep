package com.kingz.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.kingz.customdemo.R;

/**
 * Created by KingZ.
 * Data: 2016 2016/2/1
 * Discription:  Toast工具类
 */
public class ToastTools {
    private static ToastTools mToastTools = null; //单例
    private Toast mToast;

    public ToastTools() {
    }

    //懒汉式，双重检验
    public static ToastTools getInstance() {
        if(mToastTools == null){
            synchronized (ToastTools.class){
                if (mToastTools == null) {
                    mToastTools = new ToastTools();
                }
            }
        }
        return mToastTools;
    }

    public void showToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void showMgtvWaringToast(Context context, String info) {
        if (mToast == null) {
            mToast = new Toast(context);
        }
//        View view = View.inflate(this,R.layout.custom_toast_layout,null);
        //设置View
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.custom_toast_layout, null);
        root.setBackgroundResource(R.drawable.custom_toast_bg);

        ImageView mIcon = (ImageView) root.findViewById(R.id.toast_img);
        LinearLayout.LayoutParams mIconParams = (LinearLayout.LayoutParams) mIcon.getLayoutParams();
        mIconParams.width = 32;
        mIconParams.height = 32;
//		mIconParams.bottomMargin = 5;
//		mIconParams.topMargin = 15;
        mIconParams.leftMargin = 10;
        mIcon.setLayoutParams(mIconParams);

        //设置文字
        TextView txtContent = (TextView) root.findViewById(R.id.toast_info);
        txtContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, 24);
        txtContent.setMaxEms(15);
        txtContent.setShadowLayer(3, 1, 1, Color.BLACK);
        txtContent.setText(info);
        txtContent.setSingleLine();
        txtContent.setPadding(10, 5, 5, 10);
        LinearLayout.LayoutParams mtxtParams = (LinearLayout.LayoutParams) txtContent.getLayoutParams();
        mtxtParams.rightMargin = 15;

        //设置需要显示的View
        mToast.setView(root);
        mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 50);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
    /**
     * 清空Toast
     */
    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }


}
