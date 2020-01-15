package com.kingz.adapter;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.kingz.holder.CommonViewHolder;
import com.zeke.kangaroo.adapter.CommonListAdapter;

import java.util.List;

/**
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/8/25 19:15 <br>
 * description: bitmap页面的适配器 <br>
 */
public class BitmapListAdapter extends CommonListAdapter<String> {

    public static final String TAG = "BitmapListAdapter";
    public int currentItemPostion = 0;

    public int getCurrentItemPostion() {
        return currentItemPostion;
    }

    public void setCurrentItemPostion(int currentItemPostion) {
        this.currentItemPostion = currentItemPostion;
    }

    public BitmapListAdapter(List<String> mDatas) {
        super( mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder viewHolder;
        if(convertView == null){
            viewHolder = CommonViewHolder.create(position, null,
                    parent, R.layout.simplelist_every_item);
            convertView = viewHolder.getInflateView();
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (CommonViewHolder) convertView.getTag();
        }
        TextView nameItem = viewHolder.getView(R.id.list_item);
        Resources resources = parent.getContext().getResources();
        if (currentItemPostion == position) {
            convertView.setBackgroundColor(resources.getColor(R.color.deepskyblue));
            nameItem.setTextColor(resources.getColor(R.color.suncolor));
        }else{
            convertView.setBackgroundColor(resources.getColor(R.color.transparent));
            nameItem.setTextColor(resources.getColor(R.color.lightskyblue));
        }
        String itemInfo = mData.get(position);
        String text = itemInfo != null ? itemInfo : "";
        nameItem.setText(text);
        return convertView;
    }
}
