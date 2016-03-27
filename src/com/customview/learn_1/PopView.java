package com.customview.learn_1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by KingZ.
 * Data: 2016 2016/3/13
 * Discription: 简单的自定义View练习______1
 */
public class PopView extends View{

    public PopView(Context context) {
        super(context,null);
    }
    public PopView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public PopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
