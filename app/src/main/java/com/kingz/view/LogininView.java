package com.kingz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.kingz.customdemo.R;

public class LogininView extends LinearLayout {

    private EditText edit1, edit2;

    public LogininView(Context context) {
        super(context);
        loadView();
    }

    public LogininView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadView();
    }

    public LogininView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadView();
    }

    private void loadView(){
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.form_view, this);
        edit1 = (EditText) findViewById(R.id.edit1);
        edit2 = (EditText) findViewById(R.id.edit2);
    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        edit1.setFocusable(focusable);
        edit2.setFocusable(focusable);
    }
}