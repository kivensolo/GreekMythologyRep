package com.kingz.uiusingLayout;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.kingz.customDemo.R;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/5/6 17:59 <br>
 * description: 向 Activity 添加片段方法之一: 在 Activity 的布局文件内声明片段 <br>
 */
public class FragmentPageFromXml extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_frame);
    }
}
