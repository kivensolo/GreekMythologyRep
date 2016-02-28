package com.customview;

/**
 * Created by KingZ on 2015/12/27.
 * Discription: Scroller类是滚动的一个封装类，可以实现View的平滑滚动效果
 * scrollBy() ：
 *
 * scrollTo(int x,int y):
 *  先判断传进来的(x, y)值是否和View的X, Y偏移量相等，
 *  如果不相等，就调用onScrollChanged()方法来通知界面
 *  发生改变，然后重绘界面，所以这样子就实现了移动效果。
 *      | 向右滑动 mScrollX就为负数，向左滑动mScrollX为正数
 *      |
 *      |
 *      |
 */
public class ScrollerSlider {
}
