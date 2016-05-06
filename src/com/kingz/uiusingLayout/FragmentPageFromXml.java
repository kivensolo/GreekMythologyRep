package com.kingz.uiusingLayout;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.kingz.uiusingListViews.R;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/5/6 17:59 <br>
 * description: 使用XML添加Fragment <br>
 */
public class FragmentPageFromXml extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.srclayout_headline_layout);
    }
}
