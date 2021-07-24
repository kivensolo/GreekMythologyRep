package com.kingz.graphics;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.kingz.customdemo.databinding.ActivityArcsDemoBinding;
import com.kingz.module.common.BaseActivity;

/**
 * author: King.Z
 * date:  2016/10/25 21:33
 * description: 弧形的使用
 *
 * Update 2021/7/24：
 *   改为ViewBinding,并测试reset等api.
 *
* <p>
 * FIXME 有严重的性能损耗
 */
public class Arcs extends BaseActivity {
    ActivityArcsDemoBinding arcsDemoBinding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arcsDemoBinding = ActivityArcsDemoBinding.inflate(LayoutInflater.from(this));
        setContentView(arcsDemoBinding.getRoot());
//        LogTextBox textLogView = arcsDemoBinding.textLogView;
        arcsDemoBinding.arcsSampleView.setLsr(new ArcsSampleView.IInvalidateLis() {
            @Override
            public void onInvalidate(float sweep) {
//                if(sweep == 0f){
//                    textLogView.setText("");
//                }
//                textLogView.append("curretn sweep:" + sweep + "\n");
//                int totalH = textLogView.getLineCount() * textLogView.getLineHeight();
//                if (totalH > (textLogView.getHeight() - textLogView.getLineHeight())) {
//                    textLogView.scrollTo(0, totalH - textLogView.getHeight() + textLogView.getLineHeight());
//                }
            }
        });
    }

}
