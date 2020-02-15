package com.kingz.pages.photo.filmlist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.kingz.customdemo.R;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/7 22:55
 * description:
 *   注意parent和child的使用方式.
 */
public class MyItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.activatedBackgroundIndicator};
    private Drawable mDivider;
    private int mOrientation;

    public MyItemDecoration(Context context,int orientation) {
        //final TypedArray array = context.obtainStyledAttributes(ATTRS);
        //mDivider = array.getDrawable(0);
        //array.recycle();
        mDivider = context.getResources().getDrawable(R.drawable.listview_divider);
        mOrientation = orientation;

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //ZLog.i("recyclerview - itemdecoration", "onDraw() +1");
        if (mOrientation == LinearLayout.VERTICAL) {
            drawVertical(c, parent);
        } else if(mOrientation == LinearLayout.HORIZONTAL) {
            drawHorizontal(c, parent);
        }else{
            drawHorizontal(c, parent);
            drawVertical(c, parent);
        }
    }

    // Item之间的留白
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //设置绘制范围
        if (mOrientation == LinearLayout.VERTICAL) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else if(mOrientation == LinearLayout.HORIZONTAL){
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }else{
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
        }
    }

    // 水平线
    private void drawHorizontal(Canvas c, RecyclerView parent) {

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final int left = child.getLeft() + child.getPaddingLeft();
            final int right = child.getWidth() + child.getLeft() - child.getPaddingRight();
            //mDivider绘制在item内部
            final int top = child.getBottom() - mDivider.getIntrinsicHeight() - child.getPaddingBottom();
//            final int top = child.getBottom() + mDivider.getIntrinsicHeight();
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    // 竖直线
    private void drawVertical(Canvas c, RecyclerView parent) {

        final int childCount = parent.getChildCount();

        // 在每一个子控件的右侧画线
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int right = child.getRight() - child.getPaddingRight();
            int left = right - mDivider.getIntrinsicWidth();
            final int top = child.getTop() + child.getPaddingTop();
            final int bottom = child.getTop() + child.getHeight() - child.getPaddingBottom();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

}
