package com.customview.livstview;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.kingz.uiusingListViews.R;
import com.kingz.uiusingListViews.SpanInfoData;

import java.util.List;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/1/7 17:02
 * description: Span类的数据适配器
 */
public class CustomAdapter extends BaseAdapter{

    private static final String TAG = "CustomAdapter";
    private LayoutInflater mInflater;
    private List<SpanInfoData> dataList;

    public CustomAdapter(Context context, List<SpanInfoData> dataList, ListView listView) {
        mInflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_spans_item,null,false);
            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.title_a_id);
            viewHolder.spanTextTv = (TextView) convertView.findViewById(R.id.spantext_id);
            viewHolder.SpanDescTv = (TextView) convertView.findViewById(R.id.spanDesc_id);
            viewHolder.SpanRightTv = (TextView) convertView.findViewById(R.id.spanRightInfo_id);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.titleTv.setText(dataList.get(position).itemTitle);
        viewHolder.spanTextTv.setText(dataList.get(position).spanText);
        viewHolder.SpanDescTv.setText(dataList.get(position).spanDesc);
        viewHolder.SpanRightTv.setText(dataList.get(position).rightText);
        switch (position){
            case 0:
                SpannableStringBuilder ssb = new SpannableStringBuilder(dataList.get(position).spanText);
                ssb.setSpan(new URLSpan("https://www.baidu.com"),0,4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                Log.i(TAG,"item_1 设置span");
                break;
            case 1:
//                ssb.setSpan(new UnderlineSpan(), 0,4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                break;
            case 2:
                break;
            case 3:
                break;
        }
        return convertView;
    }
}

class ViewHolder{
    public TextView titleTv;
    public TextView spanTextTv;
    public TextView SpanDescTv;
    public TextView SpanRightTv;
}
