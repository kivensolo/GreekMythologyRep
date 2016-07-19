package com.mplayer;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.provider.CommonAdapter;
import com.provider.CommonViewHolder;
import com.datainfo.ChannelData;
import com.kingz.uiusingListViews.R;

import java.util.List;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/3/23 23:52
 * description:
 */
public class ChanellListAdapter extends CommonAdapter{

    public ChanellListAdapter(Context context, List listDatas, int layoutId) {
        super(context, listDatas, layoutId);
    }

    @Override
    protected void fillData(CommonViewHolder holder, int position) {
        ImageView imageView = holder.getView(R.id.commonadapter_item_img);
        TextView textView = holder.getView(R.id.commonadapter_item_text);
        ChannelData data = (ChannelData) listDatas.get(position);
//        imageView.setImageBitmap(data.img);
        textView.setText(data.channelName);
    }
}
