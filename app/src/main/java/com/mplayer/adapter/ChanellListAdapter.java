package com.mplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.kingz.adapter.CommonListAdapter;
import com.kingz.adapter.CommonViewHolder;
import com.kingz.customdemo.R;
import com.provider.ChannelData;

import java.util.List;

/**
 * author: King.Z
 * date: 2016 2016/3/23 23:52
 * description:
 */
public class ChanellListAdapter extends CommonListAdapter {

    public ChanellListAdapter(Context context, List listDatas) {
        super(context, listDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder holder = CommonViewHolder.getViewHolder(mContex, convertView,
				parent, R.layout.simple_listviewitem, position);
		fillData(holder, position);
		return holder.getConvertView();
    }

    @Override
    protected void fillData(CommonViewHolder holder, int position) {
        ImageView imageView = holder.getView(R.id.commonadapter_item_img);
        TextView textView = holder.getView(R.id.commonadapter_item_text);
        ChannelData data = (ChannelData) mData.get(position);
//        imageView.setImageBitmap(data.img);
        textView.setText(data.channelName);
    }

}
