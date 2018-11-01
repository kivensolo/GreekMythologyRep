package com.kingz.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kingz.customdemo.R;
import com.kingz.mode.RecycleDataInfo;
import com.kingz.pages.photo.filmlist.DemoViewHolder;
import com.kingz.utils.ZLog;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/7 22:41
 * description:
 *   RecycleView的数据适配器
 */
public class DemoRecyclerAdapter extends RecyclerView.Adapter<DemoViewHolder> {

    private static final String TAG="DemoRecyclerAdapter";
    private List<RecycleDataInfo> mDataModels;
    private List<Integer> mHeights;
    private OnItemClickLitener mOnItemClickLitener;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public DemoRecyclerAdapter(List<RecycleDataInfo> dataModels){
        if(dataModels == null){
            throw new IllegalArgumentException("DataModel must not be null");
        }
        mDataModels = dataModels;
        mHeights = new ArrayList<>();
    }

    @Override
    public DemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ZLog.i(TAG,"onBindViewHolder()");
        //创建item的view
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photoitem_recycler_view, parent, false);
        return new DemoViewHolder(itemView);
    }

    /**
     * 绑定每一项数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final DemoViewHolder holder, int position) {
        ZLog.i(TAG,"onBindViewHolder() holder="+holder+"---position:"+position);
        RecycleDataInfo dataModel = mDataModels.get(position);
        // 随机高度, 模拟瀑布效果.
        if (mHeights.size() <= position) {
            mHeights.add((int) (50 + Math.random() * 200));
        }

        ViewGroup.LayoutParams lp = holder.getTvLabel().getLayoutParams();
        lp.height = mHeights.get(position);
        holder.getTvLabel().setLayoutParams(lp);

        holder.getTvLabel().setText(dataModel.getLabel());
        holder.getTvDateTime()
               .setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
               .format(dataModel.getDateTime()));

//        if (mOnItemClickLitener != null) {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = holder.getLayoutPosition();
//                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
//                }
//            });
//
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int pos = holder.getLayoutPosition();
//                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
//                    return false;
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return mDataModels.size();
    }

    public void addData(int position) {
        RecycleDataInfo model = new RecycleDataInfo();
        model.setDateTime(getBeforeDay(new Date(), position));
        model.setLabel("New Item");

        mDataModels.add(position, model);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mDataModels.remove(position);
        notifyItemRemoved(position);
    }

    private static Date getBeforeDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, i * (-1));
        return calendar.getTime();
    }

}
