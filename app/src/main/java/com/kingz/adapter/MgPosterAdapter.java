package com.kingz.adapter;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.App;
import com.kingz.customdemo.R;
import com.kingz.net.OkHttpClientManager;
import com.kingz.recyclerview.data.MgPosterBean;
import com.zeke.kangaroo.adapter.CommonRecyclerAdapter;
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

    public MgPosterAdapter() {
        super(new ArrayList<MgPosterBean>());
        mHeights = new ArrayList<>();
    }

    public MgPosterAdapter(ArrayList<MgPosterBean> dataModels) {
        super(dataModels);
        mHeights = new ArrayList<>();
    }

    public void attachData(ArrayList<MgPosterBean> dataModels) {
        addAll(dataModels);
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
        final Context context = parent.getContext();
        final ViewHolder holder = super.onCreateViewHolder(parent, viewType);
        if (viewType == TYPE_DEFAULT) {
            ImageView img = holder.itemView.findViewById(R.id.recom_poster);
            img.setBackground(App.getAppInstance().getAppContext().getResources().getDrawable(R.drawable.bg1));
        }
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                Toast.makeText(context, pos + " click", Toast.LENGTH_SHORT).show();
            }
        });

        final Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (holder.getLayoutPosition() != 1) {
                    if(vibrator != null){
                        vibrator.vibrate(70); //震动70毫秒
                    }
                    //TODO 进行优化
//                    mItemTouchHelper.startDrag(holder);
                }else{
                    Toast.makeText(context, "这是固定的Item", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        return holder;
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

        MgPosterBean data = getItem(position);
        TextView tvLabel = holder.getView(R.id.item_text);
        tvLabel.setText(data.getTitle());
        TextView dateTime = holder.getView(R.id.item_date);
        dateTime.setText(data.getUpdateInfo());
        ImageView posterView = holder.getView(R.id.recom_poster);
        //TODO 获取缓存
        OkHttpClientManager.displayImage(posterView, data.getImg());

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }


    public void addData(int position) {
        MgPosterBean data = new MgPosterBean();
        data.setTitle("测试数据" + position);
        data.setUpdateInfo("即将更新");
        addItem(data,position);
        //应该会触发重新布局  部分view应该是dirty view
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        remove(position);
        notifyItemRemoved(position);
    }

    private static Date getBeforeDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, i * (-1));
        return calendar.getTime();
    }

}
