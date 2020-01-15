package com.kingz.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.kingz.holder.CommonViewHolder;
import com.zeke.kangaroo.adapter.CommonListAdapter;

import java.util.HashSet;
import java.util.List;

/**
 * author：KingZ
 * date：2019/10/10
 * description：属性动画Demo页面数据适配器
 */
public class PropertyAdapter extends CommonListAdapter<String> {

    private TextView textView;
    public HashSet<Integer> clickedPos = new HashSet<>();
    public PropertyAdapter(List<String> datas) {
        super(datas);
    }

    public void addClickedPos(int pos) {
        clickedPos.add(pos);
    }

    public void removeClickedPos(int pos) {
        clickedPos.remove(pos);
    }

    private boolean isClicked(int pos){
        return clickedPos.contains(pos);
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
        textView = viewHolder.getView(R.id.list_item);
        int textColor;
        int bkgColor;
        if (isClicked(position)) {
            bkgColor = R.color.deepskyblue;
            textColor = R.color.suncolor;
        } else {
            bkgColor = R.color.transparent;
            textColor = R.color.lightskyblue;
        }
        convertView.setBackgroundColor(parent.getContext().getResources().getColor(bkgColor));
        textView.setTextColor(parent.getContext().getResources().getColor(textColor));
        textView.setText(mData.get(position));
        return viewHolder.getInflateView();
    }
}
