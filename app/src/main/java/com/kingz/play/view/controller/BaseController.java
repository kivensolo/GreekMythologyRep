package com.kingz.play.view.controller;

import android.view.View;


/**
 * author：KingZ
 * date：2019/7/31
 * description：UI控制器基类
 */

public abstract class BaseController implements Displayable{
    //当前controller根view
    View rootView;

    @Override
    public boolean isShown() {
        return rootView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void close() {
        rootView.setVisibility(View.GONE);
    }

    @Override
    public void bringToFront() {
        rootView.bringToFront();
    }
}
