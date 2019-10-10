package com.kingz.pages.photo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingz.adapter.CommonAdapter;
import com.kingz.adapter.CommonViewHolder;
import com.kingz.customdemo.R;
import com.kingz.holder.BaseViewHodler;

import java.util.List;

/**
 * author：KingZ
 * date：2019/10/10
 * description：
 */
public class PropertyAdapter extends CommonAdapter<String> {
    private TextView nameItem;
    public PropertyAdapter(Context contex, List<String> datas) {
        super(contex, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        commonViewHolder = BaseViewHodler.getHolder(mContex,convertView,parent, R.layout.simplelist_every_item,position);
        nameItem = (TextView) commonViewHolder.getView(R.id.list_item);
        Object o = mData.get(position);
        String text = (String) o;
        nameItem.setText(text);
        return commonViewHolder.getmConvertView();
    }

    @Override
    protected void fillData(CommonViewHolder holder, int position) {

    }
}
