package com.customview.scroll;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/6/27 0:36 <br>
 * description: 手势监听的实现类 <br>
 */
public class GestureListenerImpl implements GestureDetector.OnGestureListener {
    //触摸屏幕时均会调用该方法
    @Override
    public boolean onDown(MotionEvent e) {
        Log.i("TAG","---> 手势中的onDown方法");
        return false;
    }

    //手指在屏幕上拖动时会调用该方法
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i("TAG","---> 手势中的onFling方法");
        return false;
    }

    //手指长按屏幕时均会调用该方法
    @Override
    public void onLongPress(MotionEvent e) {
        Log.i("TAG","---> 手势中的onLongPress方法");
    }

    //手指在屏幕上滚动时会调用该方法
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i("TAG","---> 手势中的onScroll方法");
        return false;
    }

    //手指在屏幕上按下,且未移动和松开时调用该方法
    @Override
    public void onShowPress(MotionEvent e) {
        Log.i("TAG","---> 手势中的onShowPress方法");
    }

    //轻击屏幕时调用该方法
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i("TAG","---> 手势中的onSingleTapUp方法");
        return false;
    }
}
