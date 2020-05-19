package com.kingz.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.kingz.customdemo.R;
import com.zeke.kangaroo.utils.UIUtils;
import com.zeke.kangaroo.utils.ZLog;
import com.zeke.ktx.App;

/**
 * author: King.Z
 * date:  2016/12/10 20:46
 * description: 原生View绘制的按钮
 * 支持焦点以及按压状态的样式切换
 */
public class HighLightButton extends AppCompatButton {

    private static final String TAG = "HightLightButton";

    public static final int DEFAULT_SIZE = UIUtils.dip2px(App.instance.getBaseContext(),26);
    public static final int FOCUSED_SIZE = UIUtils.dip2px(App.instance.getBaseContext(),35);
    /*焦点标记*/
    private boolean mFocusedFlag = false;
    /*选定标记*/
    private boolean mSelectedFlag = false;

    public HighLightButton(Context context) {
        this(context, null);
    }

    public HighLightButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighLightButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initParms();
    }

    private void initParms() {
        ZLog.i(TAG,"initParms() ");
        this.setFocusable(true);
        this.setText("原生控件自定义焦点状态");
        this.setTextColor(0xFF646464);
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, DEFAULT_SIZE);
        getPaint().setFakeBoldText(false); //非粗体
        this.setBackgroundColor(Color.WHITE);
        this.setGravity(Gravity.CENTER);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        ZLog.i(TAG, "focusChangeView isfocused:" + focused
                + ",direction=" + direction
                + ",previouslyFocusedRect=" + previouslyFocusedRect);
        if (focused) {
            //出入眼帘
            setFocusedClass(true, R.drawable.text_focused_bg_shape);
        }else if (mSelectedFlag) {
            //被烙印心中
            setFocusedClass(false, R.drawable.text_selected_bg_shape);
		}else{
            //只犹一缕轻烟飘过
            setTextColor(0xFF666666);
			setBackgroundColor(Color.WHITE);
			setTextSize(TypedValue.COMPLEX_UNIT_PX, DEFAULT_SIZE);
            getPaint().setShadowLayer(3, 1, 1, Color.TRANSPARENT);
			getPaint().setFakeBoldText(false);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return super.onCheckIsTextEditor();
    }

    private void setFocusedClass(boolean fakeBoldText, int text_selected_bg_shape) {
        this.setTextColor(Color.BLACK);
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, FOCUSED_SIZE);
        getPaint().setFakeBoldText(fakeBoldText); //非粗体
        getPaint().setShadowLayer(3, 1, 1, Color.RED);
        this.setBackgroundResource(text_selected_bg_shape);
    }

    public class onFocusedChangeedLis implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            ZLog.i(TAG, "onFocusedChangeedLis focusChangeView is:" + v + ",isHasFocus=" + hasFocus);

        }
    }

    public boolean ismFocusedFlag() {
        return mFocusedFlag;
    }

    public boolean ismSelectedFlag() {
        return mSelectedFlag;
    }
}
