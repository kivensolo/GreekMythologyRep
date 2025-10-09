package com.zeke.demo.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * author：ZekeWang
 * date：2025/4/28
 * description：适合用于展示自定义view效果的固定宽高尺寸的容器View
 */
public class FixedSizeCustomViewContainer extends View {
    public FixedSizeCustomViewContainer(Context context) {
        super(context);
    }

    public FixedSizeCustomViewContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedSizeCustomViewContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(300, 300);
    }
}
