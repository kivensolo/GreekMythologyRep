package com.kingz.animation;

import android.graphics.Camera;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/11/20 16:25
 * description:ViewPage的页面切换效果
 */
public class ViewpageTransformerAnimation implements ViewPager.PageTransformer {
    private static final float MAX_SCALE = 1.0f;
    private static final float MIN_SCALE = 0.75f;
    private static final float MAX_ALPHA = 1.0f;
    private static final float MIN_ALPHA = 0.5f;
    private static final float MAX_ROTATE = 25;
    private static final float MIN_ROTATE = 10;
    private Camera camera = new Camera();

    private float scaleFactor = MIN_SCALE;
    private float rotat = MAX_ROTATE;
    private float alpha = MAX_ALPHA;

    @Override
    public void transformPage(View page, float position) {
        scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
        alpha = Math.min(MAX_ALPHA,  Math.abs(1 - Math.abs(position)));
        rotat = MAX_ROTATE * Math.abs(position);
        if (position < 0) {
            setPageParms(page, scaleFactor, rotat,alpha);
        } else if (0 <= position && position > 1) {
            setPageParms(page, scaleFactor, -rotat,alpha);
        } else {
            setPageParms(page, scaleFactor, -rotat,alpha);
        }
    }

    private void setPageParms(View page, float scaleFactor, float rotat,float alpha) {
        page.setScaleX(scaleFactor);
        page.setScaleY(scaleFactor);
        page.setAlpha(alpha);
        page.setRotationY(rotat);
    }
}
