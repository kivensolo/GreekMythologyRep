package com.kingz.scroll.view_pager.sc_pagerinfo;

import android.content.Context;
import android.util.AttributeSet;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class SCViewPager extends ViewPager {

    private ArrayList<SCViewAnimation> mViewAnimation;

    public SCViewPager(Context context) {
        super(context);
        this.mViewAnimation = new ArrayList<>();
    }

    public SCViewPager(Context context, AttributeSet attr) {
        super(context, attr);
        this.mViewAnimation = new ArrayList<>();
    }

    public void addAnimation(SCViewAnimation inViewAnimation) {
        mViewAnimation.add(inViewAnimation);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);

        for (int i = 0; i < mViewAnimation.size(); i++) {
            mViewAnimation.get(i).applyAnimation(position, positionOffset);
        }

    }

}