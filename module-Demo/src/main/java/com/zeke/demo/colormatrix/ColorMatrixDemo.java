package com.zeke.demo.colormatrix;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingz.module.common.BaseActivity;
import com.kingz.module.common.router.RouterConfig;
import com.zeke.demo.R;

/**
 * author: King.Z <br>
 * date:  2017/5/26 22:01 <br>
 */
@Route(path = RouterConfig.PAGE_COLOR_MATRIX)
public class ColorMatrixDemo extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colormatrix);
    }
}
