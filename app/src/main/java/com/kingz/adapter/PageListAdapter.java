package com.kingz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.kingz.mode.ListBillData;
import com.zeke.kangaroo.adapter.CommonRecyclerAdapter;

import java.util.ArrayList;

/**
 * author：KingZ
 * date：2019/10/13
 * description：每个页面RecyclerView的Adapter
 */

public class PageListAdapter extends CommonRecyclerAdapter<ListBillData> {


    public PageListAdapter(Context context) {
        super(new ArrayList<ListBillData>());
    }

    @Override
    protected int getItemLayout(int type) {
        return R.layout.list_bill;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListBillData data = getItem(position);
        ((TextView)holder.itemView).setText(data.getItemName());

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
}
