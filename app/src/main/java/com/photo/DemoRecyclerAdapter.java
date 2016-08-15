package com.photo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingz.customdemo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/7 22:41
 * description:
 *   RecycleView的数据适配器
 */
public class DemoRecyclerAdapter extends RecyclerView.Adapter<DemoViewHolder> {

    private List<PhotoDataModel> mDataModels;
    private List<Integer> mHeights;

    public DemoRecyclerAdapter(List<PhotoDataModel> dataModels){
        if(dataModels == null){
            throw new IllegalArgumentException("DataModel must not be null");
        }
        mDataModels = dataModels;
        mHeights = new ArrayList<>();
    }

    /**
     * 创建ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public DemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(DemoViewHolder holder, int position) {
         PhotoDataModel dataModel = mDataModels.get(position);

        // 随机高度, 模拟瀑布效果.
        if (mHeights.size() <= position) {
            mHeights.add((int) (100 + Math.random() * 300));
        }

        ViewGroup.LayoutParams lp = holder.getTvLabel().getLayoutParams();
        lp.height = mHeights.get(position);

        holder.getTvLabel().setLayoutParams(lp);

        holder.getTvLabel().setText(dataModel.getLabel());
        holder.getTvDateTime().setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                .format(dataModel.getDateTime()));
    }

    @Override
    public int getItemCount() {
        return mDataModels.size();
    }

    public void addData(int position) {
        PhotoDataModel model = new PhotoDataModel();
        model.setDateTime(getBeforeDay(new Date(), position));
        model.setLabel("No. " + (int) (new Random().nextDouble() * 20.0f));

        mDataModels.add(position, model);
        //通知插入--更新
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mDataModels.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 获取日期的前一天
     *
     * @param date 日期
     * @param i    偏离
     * @return 新的日期
     */
    private static Date getBeforeDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, i * (-1));
        return calendar.getTime();
    }

}
