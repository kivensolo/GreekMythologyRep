package com.kingz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.App;
import com.kingz.customdemo.R;
import com.kingz.net.OkHttpClientManager;
import com.kingz.recyclerview.data.MgPosterBean;
import com.zeke.kangaroo.utils.ZLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * author: King.Z
 * date:  2016/8/7 22:41
 * description:  芒果海报数据Adapter
 */
public class MgPosterAdapter extends CommonRecyclerAdapter<MgPosterBean> {

    private static final String TAG = "MgPosterAdapter";
    private static final int TYPE_DEFAULT = 1; // 默认海报
    //瀑布流模拟高度数据
    private List<Integer> mHeights;

    public MgPosterAdapter(Context context) {
        super(context, new ArrayList<MgPosterBean>());
        mHeights = new ArrayList<>();
    }

    public MgPosterAdapter(Context context, ArrayList<MgPosterBean> dataModels) {
        super(context, dataModels);
        mHeights = new ArrayList<>();
    }

    public void attachData(ArrayList<MgPosterBean> dataModels) {
        mData.addAll(dataModels);
    }

    @Override
    protected int getItemLayout(int type) {
        return R.layout.photoitem_recycler_view;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 5) {
            return TYPE_DEFAULT;
        }
        return super.getItemViewType(position);
    }

    /**
     * viewHolder持有view的信息，用作缓存
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        if (viewType == TYPE_DEFAULT) {
            ImageView img = viewHolder.itemView.findViewById(R.id.recom_poster);
            img.setBackground(App.getAppInstance().getAppContext().getResources().getDrawable(R.drawable.bg1));
        }
        return viewHolder;
    }

    /**
     * 绑定每一项数据
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        ZLog.i(TAG, "onBindViewHolder() holder=" + holder + "---position:" + position);

        // 随机高度, 模拟瀑布效果.
//        if (mHeights.size() <= position) {
//            mHeights.add((int) (50 + Math.random() * 200));
//        }

//        ViewGroup.LayoutParams lp = holder.getTvLabel().getLayoutParams();
//        lp.height = mHeights.get(position);
//        holder.getTvLabel().setLayoutParams(lp);

        MgPosterBean data = mData.get(position);
        TextView tvLabel = holder.getView(R.id.item_text);
        tvLabel.setText(data.getTitle());
        TextView dateTime = holder.getView(R.id.item_date);
        dateTime.setText(data.getUpdateInfo());
        ImageView posterView = holder.getView(R.id.recom_poster);
        //TODO 获取缓存
        OkHttpClientManager.displayImage(posterView, data.getImg());
        setHolderListeners(holder);
    }


    public void addData(int position) {
        MgPosterBean data = new MgPosterBean();
        data.setTitle("测试数据" + position);
        data.setUpdateInfo("即将更新");
        mData.add(position, data);
        //应该会触发重新布局  部分view应该是dirty view
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    private static Date getBeforeDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, i * (-1));
        return calendar.getTime();
    }

}
