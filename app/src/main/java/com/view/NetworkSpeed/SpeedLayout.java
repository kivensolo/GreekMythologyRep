package com.view.NetworkSpeed;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.kingz.customdemo.R;
import com.kingz.callback.ServiceSelectBtnCallBack;
import com.kingz.widgets.HighLightButton;

/**
 * @author minglu.jiang
 *         <p>
 *         2013-6-21
 *         <p>
 *         网络优化
 */
public class SpeedLayout extends LinearLayout implements ServiceSelectBtnCallBack {

    public SpeedLayout(Context context) {
        super(context);
        init(context);
    }

    public SpeedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        inflate(context, R.layout.speed_item, this);
        this.setOrientation(LinearLayout.VERTICAL);
        SeekBar seekBar = (SeekBar) findViewById(R.id.service_seekbar);
//        seekBar.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
    }

    @Override
    public void setNextLeftButton(HighLightButton button) {

    }
}
