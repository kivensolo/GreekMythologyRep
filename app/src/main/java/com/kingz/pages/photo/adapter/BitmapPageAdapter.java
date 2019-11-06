package com.kingz.pages.photo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingz.adapter.CommonListAdapter;
import com.kingz.customdemo.R;
import com.kingz.holder.BaseViewHolder;
import com.kingz.pages.photo.PhotosActivity;

import java.util.List;

/**
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/8/25 19:15 <br>
 * description: bitmap页面的适配器 <br>
 */
public class BitmapPageAdapter extends CommonListAdapter<PhotosActivity.ItemInfo> {

    public BitmapPageAdapter(Context contex, List<PhotosActivity.ItemInfo> mDatas) {
        super(contex, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        commonViewHolder = BaseViewHolder.getHolder(mContex,convertView,parent, R.layout.simplelist_every_item,position);
        TextView nameItem = commonViewHolder.getView(R.id.list_item);
        PhotosActivity.ItemInfo itemInfo = mData.get(position);
        String text = itemInfo != null ? itemInfo.getName() : "";
        nameItem.setText(text);
        return commonViewHolder.getInflateView();
    }

    @Override
    protected void fillData(CommonViewHolder holder, int position) {

    }
}
