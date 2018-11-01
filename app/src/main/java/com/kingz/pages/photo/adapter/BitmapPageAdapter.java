package com.kingz.pages.photo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kingz.adapter.CommonAdapter;
import com.kingz.adapter.CommonViewHolder;
import com.kingz.customdemo.R;
import com.kingz.holder.CommViewHodler;
import com.kingz.pages.photo.BitmapPhotosActivity;

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
        nameItem.setText(((BitmapPhotosActivity.ItemInfo)(mData.get(position))).getName());
        return commonViewHolder.getmConvertView();
    }

    @Override
    protected void fillData(CommonViewHolder holder, int position) {

    }
}
