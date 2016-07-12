package com.customview.pages;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.customview.views.DrawBallView;
import com.kingz.uiusingListViews.R;

/**
 * Created by KingZ on 2015/11/23.
 * Discription:
 */
public class FingerBallActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(-1,-1);
        rootLayout.setGravity(LinearLayout.VERTICAL);
        rootLayout.setLayoutParams(lp);
        rootLayout.setBackgroundColor(Color.BLACK);
        super.setContentView(rootLayout);

        DrawBallView drawView = new DrawBallView(this);
        drawView.setMinimumHeight(400);
        drawView.setMinimumWidth(500);
        drawView.setBackgroundColor(getResources().getColor(R.color.white));
        rootLayout.addView(drawView);
    }
}
