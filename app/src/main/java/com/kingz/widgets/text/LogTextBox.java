package com.kingz.widgets.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;

import com.module.tools.ViewUtils;

/**
 * author: King.Z <br>
 * date:  2017/1/25 15:21 <br>
 * description: 一个可编辑并且带默认滚动条的文本控件 <br>
 */

public class LogTextBox extends androidx.appcompat.widget.AppCompatTextView {
    private boolean borderFlag = false;
    private Paint  borderPaint = new Paint();
    public LogTextBox(Context context) {
        this(context, null);
    }

    public LogTextBox(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public LogTextBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected MovementMethod getDefaultMovementMethod() {
        return ScrollingMovementMethod.getInstance();
    }

    @Override
    public Editable getText() {
        return (Editable) super.getText();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, BufferType.EDITABLE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(borderFlag){
            ViewUtils.setViewBorder(this, canvas, borderPaint);
        }
    }

    public void setBorder(boolean isUseBorder){
        borderFlag = isUseBorder;
    }
}
