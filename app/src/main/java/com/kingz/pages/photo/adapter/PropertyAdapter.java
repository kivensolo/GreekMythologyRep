package com.kingz.pages.photo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.kingz.holder.CommonViewHolder;
import com.zeke.kangaroo.adapter.CommonListAdapter;

import java.util.List;

/**
 * author：KingZ
 * date：2019/10/10
 * description：
 */
public class PropertyAdapter extends CommonListAdapter<String> {

    private TextView nameItem;
    public PropertyAdapter(List<String> datas) {
        super(datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder holder = CommonViewHolder.create(position,convertView,
                parent, R.layout.simplelist_every_item);
        nameItem = holder.getView(R.id.list_item);
        Object o = mData.get(position);
        String text = (String) o;
        nameItem.setText(text);
        return holder.getInflateView();
    }
}
