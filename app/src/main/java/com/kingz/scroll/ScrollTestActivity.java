package com.kingz.scroll;

import android.os.Bundle;
import android.view.ViewGroup;

import com.BaseActivity;
import com.kingz.customdemo.R;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/6/24 13:25 <br>
 * description: 实现View滑动的几种方式 <br>
 * <p/>
 * A:实现滑动的最朴素直接的方式就是使用View类自带的
 * scrollTo/scrollBy方法了。scrollBy方法是滑动指定的位移量，
 * 而scrollTo方法是滑动到指定位置.
 */
public class ScrollTestActivity extends BaseActivity {
    private static final String TAG = ScrollTestActivity.class.getSimpleName();
    private FlingView filingView;

    @Override
    public void showLoadingDialog() {
        super.showLoadingDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolltest);
        filingView = new FlingView(this);
        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        addContentView(filingView,lps);
    }
}
