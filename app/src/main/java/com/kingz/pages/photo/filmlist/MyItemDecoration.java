package com.kingz.pages.photo.filmlist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.kingz.customdemo.R;

/**
 * author: King.Z
 * date:  2016/8/7 22:55
 * description:
 *   注意parent和child的使用方式.
 */
public class MyItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.activatedBackgroundIndicator};
    private Drawable mDivider;
    private int mOrientation;

    /**
     * getItemOffsets 是针对每一个 ItemView。
     * onDraw 方法是针对 RecyclerView 本身。
     * 所以在 onDraw 方法中需要遍历屏幕上可见的 ItemView，
     * 分别获取它们的位置信息，然后分别的绘制对应的分割线。
     * @param c Canvas
     * @param parent RecyclerView
     * @param state RecyclerView.State
     */
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

    public MyItemDecoration(Context context,int orientation) {
        //final TypedArray array = context.obtainStyledAttributes(ATTRS);
        //mDivider = array.getDrawable(0);
        //array.recycle();
        mDivider = context.getResources().getDrawable(R.drawable.listview_divider);
        mOrientation = orientation;

    }


    /**
     * onDrawOver的使用方法和onDraw类似，只是绘制的顺序不同。
     * getItemOffsets会覆盖onD
     * raw的绘制，onDrawOver则会绘制在最上面。
     * 可用于给itemView加角标等功能。
     * @param c  Canvas
     * @param parent RecyclerView
     * @param state RecyclerView.State
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

    }

    /**
     * 设置ItemView的内嵌偏移长度 inset (outRect矩形与ItemView的间隔.);
     * 默认的情况下，top、left、right、bottom都是0，所以矩形和ItemView就重叠了。
     *
     * (left,top)
     *     ┌--------------┐
     *     | ┌----------┐ |
     *     | | ItemView | |
     *     | └----------┘ |
     *     |OutRect       |
     *     └--------------┘
     *              (right,bottom)
     *
     *  源码分析:
     *  ---- RecyclerView.LayoutManager.measureChild
     *   ---> mRecyclerView.getItemDecorInsetsForChild(childView)
     *  测量所有的子view的宽和高，得到子view的Rect。然后就能得到这一块真正的宽和高(包括padding的值)。
     */
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
