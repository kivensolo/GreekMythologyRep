package com.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/1/22 16:14
 * description:
 */
public class SimpleLayout extends ViewGroup{

    public static final String TAG = "SimpleLayout";

    public SimpleLayout(Context context) {
        super(context);
    }

    public SimpleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Measure子View
        if(getChildCount() > 0){
            View childView = getChildAt(0);
            Log.i(TAG,childView.getParent().toString());
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);  //测量出子视图的大小
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(getChildCount() > 0){
            View childView = getChildAt(0);
            //调用childView.getMeasuredWidth()和childView.getMeasuredHeight()方法得到的值就是在onMeasure()方法中测量出的宽和高。
            childView.layout(10, 10, childView.getMeasuredWidth(),childView.getMeasuredHeight());
            Log.i(TAG,"childView  视图的 宽 : " + childView.getWidth()
                    + "-----高：" + childView.getHeight()
                    + "; widthMeasureSpec = " + childView.getMeasuredWidth()
                    +"  ---- heightMeasureSpec = " + childView.getMeasuredHeight());
        }
    }
}
