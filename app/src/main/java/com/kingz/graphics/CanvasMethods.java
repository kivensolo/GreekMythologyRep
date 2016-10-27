package com.kingz.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.base.BaseActivity;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/10/13 16:02 <br>
 * description: Canvas各种方法 <br>
 */

public class CanvasMethods extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout root = new LinearLayout(this);
        root.setLayoutParams(lps);
        setContentView(root);

    }

    class DemoCanvasVeiw extends View{
        public DemoCanvasVeiw(Context context) {
            super(context);
            initView();
        }
        public void initView(){

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }
    }
}
