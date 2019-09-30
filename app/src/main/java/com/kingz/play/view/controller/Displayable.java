package com.kingz.play.view.controller;

/**
 * author：KingZ
 * date：2019/7/31
 * description：组件显示控制接口
 */
public interface Displayable {
    boolean isShown();

    void show();

    void close();

    void bringToFront();
}
