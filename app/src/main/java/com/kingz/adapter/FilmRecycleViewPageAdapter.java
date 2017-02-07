package com.kingz.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kingz.databean.PosterGroupInfo;
import com.photo.FilmListRecyclerViewHolder;
import com.utils.OkHttpClientManager;

import java.util.List;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2017/2/7 21:51
 * description:
 */

public class FilmRecycleViewPageAdapter extends RecyclerView.Adapter<FilmListRecyclerViewHolder> {

    /**
     * 每一个海报的View
     */
    private ImageView itemView;

    /**
     * 影片数据
     */
    private List<PosterGroupInfo.Poster> posterList;
    private OnItemClickListener clickListener;

    /**
     * Item回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int posotion);
        void onItemLongClick(View view, int posotion);
    }


    /**
     * 设置回调监控
     */
    public void setOnItemClickLinstener(OnItemClickListener listener) {
        this.clickListener = listener;
    }


    public FilmRecycleViewPageAdapter(List<PosterGroupInfo.Poster> posterList) {
        if (posterList == null) {
            throw new IllegalArgumentException("posterList must not be null");
        }
        this.posterList = posterList;
    }

    @Override
    public FilmListRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        itemView = new ImageView(viewGroup.getContext());
        itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new FilmListRecyclerViewHolder(itemView);
    }

    /**
     * 每一个ViewHolder的数据绑定回调
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(FilmListRecyclerViewHolder viewHolder, int i) {
        PosterGroupInfo.Poster mPoster = posterList.get(i);
        //请求网络图片数据
        OkHttpClientManager.displayImage(viewHolder.getmImageView(), mPoster.poster_ur);
        ViewGroup.LayoutParams lp = viewHolder.getmImageView().getLayoutParams();
//        lp.height = ;
//        viewHolder.getTvLabel().setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return posterList.size();
    }


}
