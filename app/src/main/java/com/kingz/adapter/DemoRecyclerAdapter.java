package com.kingz.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.App;
import com.kingz.customdemo.R;
import com.kingz.mode.RecycleDataInfo;
import com.kingz.pages.photo.filmlist.MgPosterViewHolder;
import com.kingz.posterfilm.data.MgPosterBean;
import com.kingz.utils.OkHttpClientManager;
import com.kingz.utils.ZLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/7 22:41
 * description:
 *   RecycleView的数据适配器
 */
public class DemoRecyclerAdapter extends RecyclerView.Adapter<MgPosterViewHolder> {

    private static final String TAG="DemoRecyclerAdapter";
    public final int testViewTypeCode = 10086;
//    RecycleDataInfo
    private ArrayList mCacheData;
    //瀑布流模拟高度数据
    private List<Integer> mHeights;
    private OnItemClickLitener mOnItemClickLitener;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public DemoRecyclerAdapter(){
        mCacheData = new ArrayList();
        mHeights = new ArrayList<>();
    }

    public DemoRecyclerAdapter(ArrayList dataModels){
        if(dataModels == null){
            throw new IllegalArgumentException("DataModel must not be null");
        }
        mCacheData = dataModels;
        mHeights = new ArrayList<>();
    }

    public void attachData(ArrayList dataModels){
        mCacheData.addAll(dataModels);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 5){
            return testViewTypeCode;
        }
        return super.getItemViewType(position);
    }

    /**
     * viewHolder持有view的信息，用作缓存
     */
    @Override
    public MgPosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建item的view
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photoitem_recycler_view, parent, false);
        if(viewType == testViewTypeCode){
            ImageView img = (ImageView) itemView.findViewById(R.id.recom_poster);
            img.setBackground(App.getAppInstance().getAppContext().getResources().getDrawable(R.drawable.bg1));
        }
        return new MgPosterViewHolder(itemView);
    }

    /**
     * 绑定每一项数据
     */
    @Override
    public void onBindViewHolder(@NonNull final MgPosterViewHolder holder, int position) {
        ZLog.i(TAG,"onBindViewHolder() holder="+holder+"---position:"+position);

        // 随机高度, 模拟瀑布效果.
//        if (mHeights.size() <= position) {
//            mHeights.add((int) (50 + Math.random() * 200));
//        }

//        ViewGroup.LayoutParams lp = holder.getTvLabel().getLayoutParams();
//        lp.height = mHeights.get(position);
//        holder.getTvLabel().setLayoutParams(lp);

        Object obj = mCacheData.get(position);
        if(obj instanceof RecycleDataInfo){
            RecycleDataInfo dataModel = (RecycleDataInfo)obj;
            holder.getTvLabel()
                    .setText(dataModel.getLabel());
            holder.getTvDateTime()
                    .setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                            .format(dataModel.getDateTime()));
        }else if(obj instanceof MgPosterBean){
            MgPosterBean data = (MgPosterBean)obj;
            holder.getTvLabel().setText(data.getTitle());
            holder.getTvDateTime().setText(data.getUpdateInfo());
            //TODO 获取缓存
            OkHttpClientManager.displayImage(holder.getmPosterView(),data.getImg());
        }

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
        return mCacheData.size();
    }

    public void addData(int position) {
        RecycleDataInfo model = new RecycleDataInfo();
        model.setDateTime(getBeforeDay(new Date(), position));
        model.setLabel("New Item");

        mCacheData.add(position, model);
        //应该会触发重新布局  部分view应该是dirty view
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mCacheData.remove(position);
        notifyItemRemoved(position);
    }

    private static Date getBeforeDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, i * (-1));
        return calendar.getTime();
    }

}
