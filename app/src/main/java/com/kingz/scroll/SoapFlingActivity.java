package com.kingz.scroll;

import android.os.Bundle;
import android.view.ViewGroup;
import com.BaseActivity;
import com.kingz.customdemo.R;

/**
 * author: King.Z <br>
 * date:  2016/6/24 13:25 <br>
 *     肥皂滚动页面
 */
public class SoapFlingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolltest);
        DragAndFlingView filingView = new DragAndFlingView(this);
        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        addContentView(filingView,lps);
    }
}
