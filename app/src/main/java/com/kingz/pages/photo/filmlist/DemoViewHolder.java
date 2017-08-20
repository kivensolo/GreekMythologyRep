package com.kingz.pages.photo.filmlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kingz.customdemo.R;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/7 22:44
 * description:
 */
public class DemoViewHolder extends RecyclerView.ViewHolder{

    private TextView mTvLabel; // 标签
    private TextView mTvDateTime; // 日期

    public DemoViewHolder(View itemView) {
        super(itemView);
        mTvLabel = (TextView) itemView.findViewById(R.id.item_text);
        mTvDateTime = (TextView) itemView.findViewById(R.id.item_date);
    }

    public TextView getTvLabel() {
        return mTvLabel;
    }

    public TextView getTvDateTime() {
        return mTvDateTime;
    }
}
