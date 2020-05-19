package com.kingz.pages.photo.filmlist;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

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
