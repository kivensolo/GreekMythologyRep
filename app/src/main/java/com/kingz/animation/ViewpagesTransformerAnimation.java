package com.kingz.animation;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * author: King.Z
 * date:  2016/11/20 16:25
 * description: 一个简单的ViewPager的页面切换效果
 */
public class ViewpagesTransformerAnimation implements ViewPager.PageTransformer {
    private static final float MAX_SCALE = 1.0f;
    private static final float MIN_SCALE = 0.75f;
    private static final float MAX_ALPHA = 1.0f;
    private static final float MIN_ALPHA = 0.5f;
    private static final float MAX_ROTATE = 25;
    private static final float MIN_ROTATE = 10;

    private float scaleFactor = MIN_SCALE;
    private float rotate = MAX_ROTATE;
    private float alpha = MAX_ALPHA;

    @Override
    public void transformPage(View page, float position) {
        scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
        alpha = Math.min(MAX_ALPHA,  Math.abs(1 - Math.abs(position)));
        rotate = MAX_ROTATE * Math.abs(position);
        if (position < 0) {
            setPageParms(page, scaleFactor, rotate,alpha);
        } else if (0 <= position && position > 1) {
            setPageParms(page, scaleFactor, -rotate,alpha);
        } else {
            setPageParms(page, scaleFactor, -rotate,alpha);
        }
    }

    private void setPageParms(View page, float scaleFactor, float rotat,float alpha) {
        page.setScaleX(scaleFactor);
        page.setScaleY(scaleFactor);
        page.setAlpha(alpha);
        page.setRotationY(rotat);
    }
}
