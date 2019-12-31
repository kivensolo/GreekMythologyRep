package com.kingz.scroll.view_pager.sc_pagerinfo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple indicator view of ImageView.
 */
public class ImageIndicatorView extends LinearLayout {

    private List<ImageView> dots;

    /**
     * Default max capacity.
     */
    private static int MAX_CAPACITY = 15;

    private int numberOfDots = 0;
    /**
     * Ressource id of selected state.
     */
    private int mSelectedRessource;
    /**
     * Ressource id of unSelected state.
     */
    private int mUnSelectedRessource;

    public ImageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageIndicatorView setDotRessource(int selected, int unSelected) {
        mSelectedRessource = selected;
        mUnSelectedRessource = unSelected;
        return this;
    }

    /**
     * If you need to expand capacity.
     * Need to call before {@link #setDots(int)}
     * @param capacity capacity that you what.
     * @return DotsView
     */
    public ImageIndicatorView setMaxCapacity(int capacity){
        MAX_CAPACITY = capacity > MAX_CAPACITY ? capacity : MAX_CAPACITY;
        return this;
    }

    /**
     * SetTotal Dot Counts.
     * @param dotCounts 总的Dot个数
     */
    public ImageIndicatorView setDots(int dotCounts) {
        if(dotCounts > MAX_CAPACITY){
            dotCounts = MAX_CAPACITY;
        }
        numberOfDots = dotCounts;
        dots = new ArrayList<>();

        for(int i = 0; i < numberOfDots; i++) {
            ImageView child = new ImageView(getContext());
            child.setImageDrawable(getResources().getDrawable(mUnSelectedRessource));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            addView(child, params);
            dots.add(child);
        }
        selectDot(0); // default select 0 posion.
        return this;
    }

    public ImageIndicatorView selectDot(int idx) {
        for(int i = 0; i < numberOfDots; i++) {
            int drawableId = (i==idx)?(mSelectedRessource):(mUnSelectedRessource);
            Drawable drawable = getResources().getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
        return this;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }
}
