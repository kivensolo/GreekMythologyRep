package com.customview;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by KingZ on 2015/11/24.
 * Discription: 自定义的进度条、有原生的  View的
 */
public class CuastomProgressbar extends ProgressBar{


    private static final int DEFAULT_TEXT_SIZE = 10;
    private static final int DEFAULT_TEXT_COLOR = 0XFFFC00D1;
    private static final int DEFAULT_COLOR_UNREACHED_COLOR = 0xFFd3d6da;
    private static final int DEFAULT_HEIGHT_REACHED_PROGRESS_BAR = 2;
    private static final int DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR = 2;
    private static final int DEFAULT_SIZE_TEXT_OFFSET = 10;

    private Context context;
    private PopupWindow popupDialog;
    private LayoutInflater layoutInflater;
    private RelativeLayout layout;
    private RelativeLayout layout_bg;
    private View circleView;

    private Paint mPaint = new Paint();     //进度条画笔
    private int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mTextSize = 6;


    public CuastomProgressbar(Context context) {
        super(context);
    }

    public CuastomProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 判断是否显示
     * @return
     */
    private boolean isShowing(){
        if (popupDialog != null && popupDialog.isShowing()) {
            return true;
        }
        return false;
    }

    private void display(){
//        dismiss();
    }


}


