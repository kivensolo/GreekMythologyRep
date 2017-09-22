package com.kingz.view.listview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.kingz.customdemo.R;
import com.provider.SpanInfoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/1/6 18:11
 * description: 各种Span，可以提供我们完成一些在TextView中的特殊内容。
 *          （比如：部分内容颜色、字体、大小不同等等，更有部分字体可点击。）
 *          还有一个SpannableStringBuilder，可以帮助我们设置Span。
 */
//@SuppressWarnings("all")
public class CustomListViewActivity extends Activity{

    public ListView listView;
    public CustomAdapter mAdapter;
    private List<SpanInfoData> spanInfoList;
    private SpanInfoData spanBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customlistview_layout);
        initViews();
    }

    private void initViews() {

        spanInfoList = new ArrayList<SpanInfoData>();
        listView = (ListView) findViewById(R.id.customControlsPage_id);
        addData();
        mAdapter = new CustomAdapter(this, spanInfoList,listView);
        listView.setAdapter(mAdapter);
    }

    /**
     * 添加数据项
     */
    private void addData() {
         addItem("URL_Span","Carry you home",
                 "setMovementMethod():在单击链接时设置要执行的动作." +
                 "setHighlightColor():点击后的背景颜色"," ");
         addItem("Underline_Span","Carry you home","879879846878"," ");
    }

    public void addItem(String title, String spanText, String desc, String rightText){
          spanBean = new SpanInfoData();
          spanBean.itemTitle = title;
          spanBean.spanText = spanText;
          spanBean.spanDesc = desc;
          spanBean.rightText = rightText;
          spanInfoList.add(spanBean);
    }
}
