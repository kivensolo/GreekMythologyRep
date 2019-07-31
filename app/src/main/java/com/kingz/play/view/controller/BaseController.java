package com.kingz.play.view.controller;

import android.view.View;


/**
 * Copyright (c) 2015, 北京视达科科技有限责任公司 All rights reserved.
 * author：KingZ
 * date：2019/7/31
 * description：
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
