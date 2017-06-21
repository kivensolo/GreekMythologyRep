package com.kingz.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.*;
import android.widget.PopupWindow;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2017/6/21 21:45 <br>
 * description: CommonPopupWindow <br>
 */
public class PopupController {
    /**
     * PopupWindowd的布局文件id
     */
    private int layoutResId;
    private Context context;
    /**
     * tips:产品原型
     */
    private PopupWindow popupWindow;
    /**
     * 弹窗布局View
     */
    View mPopupView;
    private View mView;
    private Window mWindow;

    PopupController(Context context, PopupWindow popupWindow) {
        this.context = context;
        this.popupWindow = popupWindow;
    }

    public void setView(int layoutResId) {
        mView = null;
        this.layoutResId = layoutResId;
        installContent();
    }

     public void setView(View view) {
        mView = view;
        this.layoutResId = 0;
        installContent();
    }

    private void installContent() {
        if (layoutResId != 0) {
            mPopupView = LayoutInflater.from(context).inflate(layoutResId, null);
        } else if (mView != null) {
            mPopupView = mView;
        }
        popupWindow.setContentView(mPopupView);
    }

    /**
     * 设置宽度
     * @param width  宽
     * @param height 高
     */
    private void setWidthAndHeight(int width, int height) {
        if (width == 0 || height == 0) {
            //如果没设置宽高，默认是WRAP_CONTENT
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            popupWindow.setWidth(width);
            popupWindow.setHeight(height);
        }
    }

     /**
     * 如果是全屏弹窗
      * 设置Window背景灰色程度
     * @param level 0.0f-1.0f
     */
    void setBackGroundLevel(float level) {
        mWindow = ((Activity) context).getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = level;
        mWindow.setAttributes(params);
    }

    /**
     * 设置显示动画
     */
    private void setAnimationStyle(int animationStyle) {
        popupWindow.setAnimationStyle(animationStyle);
    }

     /**
     * 设置Outside是否可点击
      *
     * @param outsideTouchable 外部是否可点击
     */
    private void setOutsideTouchable(boolean outsideTouchable) {
        popupWindow.setOutsideTouchable(outsideTouchable);
        popupWindow.setFocusable(outsideTouchable);
    }

    private void setBackgroundDrawable(Drawable background){
        //new ColorDrawable(0x00000000)  设置透明背景
        popupWindow.setBackgroundDrawable(background);
    }

    /**
     * 设置popupWindow 是否可触摸
     *
     * @param touchable
     */
    private void setTouchable(boolean touchable){
        popupWindow.setTouchable(touchable);
    }

    static class PopupParams {
        public Context mContext;
        int layoutResId;             //自定义popUp_View的布局id
        View mView;                  //自定义popUp_View
        int mWidth, mHeight;         //弹窗的宽和高
        boolean isShowBg;            //是否显示背景
        boolean isShowAnim;          //是否显示动画
        float bg_level;              //屏幕背景灰色程度
        int animationStyle;          //动画Id
        boolean isTouchable = true;

        PopupParams(Context mContext) {
            this.mContext = mContext;
        }

        void apply(PopupController controller) {
            if (mView != null) {
                controller.setView(mView);
            } else if (layoutResId != 0) {
                controller.setView(layoutResId);
            } else {
                throw new IllegalArgumentException("PopupView's contentView is null");
            }
            controller.setWidthAndHeight(mWidth, mHeight);
            //设置outside可点击
            controller.setOutsideTouchable(isTouchable);
            if (isShowBg) {
                //设置背景
                controller.setBackGroundLevel(bg_level);
            }
            if (isShowAnim) {
                controller.setAnimationStyle(animationStyle);
            }
        }
    }

}
