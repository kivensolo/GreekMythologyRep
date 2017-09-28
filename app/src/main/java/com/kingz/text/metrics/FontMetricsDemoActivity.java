package com.kingz.text.metrics;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * author: King.Z <br>
 * date:  2017/4/24 13:50 <br>
 * description: 字体测量 <br>
 *     [baseline]----基准点
 *     [Ascent]-----baseline之上至字符最高处的距离
 *     [Descent]-----baseline之下至字符最低处的距离
 *     [Leading]-----上一行字符的descent到下一行的ascent之间的距离
 *     [Top]------最高字符到baseline的值，Max(Ascent)
 *     [Bottom]-----最低字符到baseline的值，Max(Descent)
 *
 *     ascent + descent 可以看成文字的height
 *     获取height ： mPaint.ascent() + mPaint.descent()
 *     获取width ： mPaint.measureText(text)
 */
public class FontMetricsDemoActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
        setContentView(root);
        FontMetricView view =  new FontMetricView(this);
        addContentView(view,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
