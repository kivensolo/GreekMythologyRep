package com.kingz.play.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * author：KingZ
 * date：2019/7/31
 * description：
 */
public class BasePlayPop extends PopupWindow {
    protected Context mContext;
    protected View contentView;

    protected BasePlayPop(Context mContext, int layoutId) {
        super(mContext);
        this.mContext = mContext;
        contentView = LayoutInflater.from(mContext).inflate(layoutId, null);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setFocusable(true);
        setOutsideTouchable(true);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(contentView);
    }
}
