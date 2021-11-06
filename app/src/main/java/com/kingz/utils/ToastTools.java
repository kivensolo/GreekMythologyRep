package com.kingz.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.kingz.customdemo.R;

import java.lang.reflect.Field;

/**
 * Created by KingZ.
 * Data: 2016 2016/2/1
 * Discription:  Toast工具类
 */
public class ToastTools {
    private static ToastTools mToastTools = null;
    private Toast mToast;

    public enum ToastType{
        ATMOSPHERE, MGTV
    }

    private ToastTools() {}

    //懒汉式，双重检验
    public void showToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static ToastTools i() {
        if(mToastTools == null){
            synchronized (ToastTools.class){
                if (mToastTools == null) {
                    mToastTools = new ToastTools();
                }
            }
        }
        return mToastTools;
    }

     public void showCustomToastByType(Context context, String msg,ToastType type) {
        int res = R.layout.dialog_tip_learn;
        switch (type) {
            case ATMOSPHERE:
                res = R.layout.dialog_tip_learn;
                break;
            case MGTV:
                res = R.layout.custom_toast_layout;
                break;
            default:
                break;
        }
        showTipToast(context,msg, res);
    }

    private void showTipToast(Context context, String msg, int res) {
        if (mToast == null) {
            mToast = new Toast(context);
        }
        mToast.setText(msg);
        //设置自定义布局
        View root = LayoutInflater.from(context).inflate(res, null);
        mToast.setView(root);
        mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 50);
        //mToast.setDuration(Toast.LENGTH_SHORT);

        // 自定义Toast动效
        try {
            Field mTNField = mToast.getClass().getDeclaredField("mTN");
            mTNField.setAccessible(true);
            Object mTNObject = mTNField.get(mToast);
            Field paramsField = mTNObject.getClass().getDeclaredField("mParams");
            /*WindowManager.LayoutParams mParams的权限是private*/
            paramsField.setAccessible(true);
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) paramsField.get(mTNObject);
            if(layoutParams != null){
                layoutParams.windowAnimations = R.style.CustomToastAnimationStyle;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        mToast.show();
    }
    /**
     * 清空Toast
     */
    public void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }


}
