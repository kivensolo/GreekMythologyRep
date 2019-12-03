package com.module.views.edittext;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * author：KingZ
 * date：2019/11/4
 * description：带清除功能的EditText
 *  方案1：布局组合封装，即组合EditText和删除按钮，封装成一个整体来使用。
 *  方案2：使用 CompoundDrawable 属性
 *
 *  设置ClearDrawable:
 *      android:drawableEnd="@mipmap/clear"
 *  取消EditText下划线：
 *      android:background="@null"
 */
public class CleanableEditText extends EditText implements OnFocusChangeListener {
    private final String TAG = "ClearableEditText";
    private Drawable mClearDrawable;
    private boolean hasFocus;
    private OnFocusChangeListener mOnFocusChangeListener;

    public CleanableEditText(Context context) {
        this(context, null);
    }

    public CleanableEditText(Context context,
                             AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public CleanableEditText(Context context,
                             AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initEditText();
    }

    private void initEditText() {
        hasFocus = hasFocus();

        mClearDrawable = getCompoundDrawables()[2];
        if(mClearDrawable == null) {
            mClearDrawable = getCompoundDrawablesRelative()[2];
        }
        if(mClearDrawable != null) {
            //设置图标的Bounds, getIntrinsicWidth()获取显示出来的大小而不是原图片的大小
            mClearDrawable.setBounds(0, 0,
                    mClearDrawable.getIntrinsicWidth(),
                    mClearDrawable.getIntrinsicHeight());
        }
        //默认设置隐藏图标
        setClearIconVisible(false);
        //设置焦点改变的监听
        setOnFocusChangeListener(this);

        //对文本内容改变进行监听
        addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable paramEditable) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                setClearIconVisible(hasFocus && s.length() > 0);
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mClearDrawable = null;
    }

    /**
     * 添加触摸事件 点击之后 出现 清空editText的效果
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && mClearDrawable != null) {
            //getTotalPaddingRight()图标左边缘至控件右边缘的距离 D-->E
            //getCompoundDrawablePadding()表示从文本右边到图标左边缘的距离 C-->D

            //A<->B<------------------->C<->D<-->E
            // |---------------------------------|
            // |👨| 123456789            |  | X  |
            // |---------------------------------|

            // MotionEvent.getX()  触摸点相对于其所在组件原点的x坐标
            // MotionEvent.getRawX()  触摸点相对于其所在组件原点的x坐标

            int left = getWidth() - getTotalPaddingRight() - getCompoundDrawablePadding(); //C的x位置
            boolean touchable = event.getX() > left && event.getX() < getWidth();
            if (touchable) {
                this.setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (!focused) {
            setCompoundDrawables(null, null, null, null);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        setClearIconVisible(hasFocus && !TextUtils.isEmpty(getText()));
        if(mOnFocusChangeListener != null){
            mOnFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    /**
     * 1、本身则调用父类的实现，
     * 2、外部设置的就透传处理
     */
    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener lsr) {
        if(lsr instanceof CleanableEditText){
            super.setOnFocusChangeListener(lsr);
        } else {
            mOnFocusChangeListener = lsr;
        }
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     */
    protected void setClearIconVisible(boolean visible) {
        if(mClearDrawable == null) return;
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                right, getCompoundDrawables()[3]);
    }
}
