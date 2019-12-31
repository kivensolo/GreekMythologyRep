package com.mplayer.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.kingz.holder.CommonViewHolder;
import com.provider.ChannelData;
import com.zeke.kangaroo.adapter.CommonListAdapter;

import java.util.List;

/**
 * author: King.Z
 * date: 2016 2016/3/23 23:52
 * description:
 */
public class ChanellListAdapter extends CommonListAdapter {

    public ChanellListAdapter(List listDatas) {
        super(listDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder holder = CommonViewHolder.create(position, convertView,
				parent, R.layout.simple_listviewitem);

        ImageView imageView = holder.getView(R.id.commonadapter_item_img);
        TextView textView = holder.getView(R.id.commonadapter_item_text);
        ChannelData data = (ChannelData) mData.get(position);
//        imageView.setImageBitmap(data.img);
        textView.setText(data.channelName);

		return holder.getInflateView();
    }
}
