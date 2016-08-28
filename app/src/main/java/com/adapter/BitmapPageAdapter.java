package com.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.holder.CommViewHodler;
import com.kingz.customdemo.R;

import java.util.List;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/8/25 19:15 <br>
 * description: bitmap页面的适配器 <br>
 */
public class BitmapPageAdapter extends CommonAdapter {

    private TextView nameItem;

    public BitmapPageAdapter(Context contex, List mDatas) {
        super(contex, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        commonViewHolder = CommViewHodler.getHolder(mContex,convertView,parent, R.layout.simplelist_every_item,position);
        nameItem = commonViewHolder.getView(R.id.list_item);
        nameItem.setText((String)mDatas.get(position));
        return commonViewHolder.getmConvertView();
    }
}
