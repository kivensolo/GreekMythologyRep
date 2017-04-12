package com.kingz.pages.photo;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;

import com.kingz.customdemo.R;


public class FilmListRecyclerViewHolder extends ViewHolder {
    private ImageView mImageView;

    public FilmListRecyclerViewHolder(View itemView) {
        super(itemView);
        mImageView = (ImageView) itemView;
        mImageView.setBackgroundColor(mImageView.getContext().getResources().getColor(R.color.darksalmon));
    }

    public ImageView getmImageView() {
        return mImageView;
    }
}
