package com.kingz.pages.photo.filmlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingz.customdemo.R;

/**
 * author: King.Z
 * date:  2016/8/7 22:44
 * description:
 */
public class MgPosterViewHolder extends RecyclerView.ViewHolder{
    private ImageView mPosterView;
    private TextView mTvLabel;
    private TextView mTvDateTime;

    public MgPosterViewHolder(View itemView) {
        super(itemView);
        mPosterView = (ImageView) itemView.findViewById(R.id.recom_poster);
        mTvLabel = (TextView) itemView.findViewById(R.id.item_text);
        mTvDateTime = (TextView) itemView.findViewById(R.id.item_date);
    }

    public ImageView getmPosterView() {
        return mPosterView;
    }

    public TextView getTvLabel() {
        return mTvLabel;
    }

    public TextView getTvDateTime() {
        return mTvDateTime;
    }
}
