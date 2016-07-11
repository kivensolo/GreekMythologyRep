package com.kingz.uiusingLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by KingZ on 2016/1/21.
 * Discription:深入学习android的View
 */
public class DeepIntoView extends View{

    public DeepIntoView(Context context) {
        super(context);
    }

    public DeepIntoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeepIntoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         setMeasuredDimension(200, 200);
        //把默认的测量流程覆盖掉了,不管在布局文件中定义MyView这个视图的大小是多少，最终在界面上显示的大小都将会是200*200。
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //需要注意的是，在setMeasuredDimension()方法调用之后，我们才能
        //使用getMeasuredWidth()和getMeasuredHeight()来获取视图测量
        //出的宽高，以此之前调用这两个方法得到的值都会是0。
    }
}
