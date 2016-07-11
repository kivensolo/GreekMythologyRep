package com.customview.scroll;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Scroller;
import com.kingz.uiusingActivity.BaseActivity;
import com.kingz.uiusingListViews.R;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/6/24 13:25 <br>
 * description: 实现View滑动的几种方式 <br>
 * <p/>
 * A:实现滑动的最朴素直接的方式就是使用View类自带的
 *   scrollTo/scrollBy方法了。scrollBy方法是滑动指定的位移量，
 *   而scrollTo方法是滑动到指定位置.
 */
public class ScrollTestActivity extends BaseActivity {
    private static final String TAG = ScrollTestActivity.class.getSimpleName();

    private Button scrollBtn;
    private Scroller mScroller;
    GestureDetector mGestureDetector = new GestureDetector(mContext,new GestureListenerImpl());


    @Override
    public void showLoadingDialog() {
        super.showLoadingDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolltest);
        findID();
    }

    @Override
    protected void findID() {
        scrollBtn = (Button) findViewById(R.id.scrollto_btn);
        final View linear = findViewById(R.id.horiz_linear);
        scrollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"坐标： x=" + scrollBtn.getScrollX() + "; y="+scrollBtn.getScrollY());
                linear.scrollBy(-20,-10);
            }
        });
    }

    @Override
    public void InData() {
        super.InData();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
         return mGestureDetector.onTouchEvent(event);
    }


    /****滑动效果处理***/


}
