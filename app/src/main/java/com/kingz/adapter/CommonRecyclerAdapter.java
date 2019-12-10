package com.kingz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

/**
 * author：KingZ
 * date：2019/10/10
 * description：通用 RecyclerView 的适配器
 */
public abstract class CommonRecyclerAdapter<T> extends Adapter<CommonRecyclerAdapter.ViewHolder> {
    protected List<T> mData;
    protected Context context;
    protected IItemClickListener itemClickListener;


    public interface IItemClickListener{
        void onItemClick(ViewHolder holder,View v);
        void onItemLongClick(ViewHolder holder,View v);
    }

    public void setItemClickListener(IItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public CommonRecyclerAdapter(Context context, List<T> datas) {
        if(datas == null){
            throw new IllegalArgumentException("Adapter Data must not be null");
        }
        this.context = context;
        this.mData = datas;
    }

    public int getCount() {
        return mData.size();
    }

    public List<T> getAll(){
        return mData;
    }

    public T getItem(int position) {
        if (position < 0 || position >= mData.size()) {
            return null;
        }
        return mData.get(position);
    }

    //根据viewType返回需要填充的布局id
    protected abstract int getItemLayout(int type);

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建item的view
        View contentView = LayoutInflater.from(parent.getContext()).inflate(this.getItemLayout(viewType), null, false);
        return new CommonRecyclerAdapter.ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }


    public CommonRecyclerAdapter<T> addItem(T obj){
        this.mData.add(obj);
        return this;
    }

    public CommonRecyclerAdapter<T> addItem(T obj, int index){
        this.mData.add(index,obj);
        return this;
    }

    public void addAll(List<T> list){
        if(list!= null){
            this.mData.addAll(list);
        }else{
            removeAll();
        }
        notifyDataSetChanged();
    }

    public void addAll(T... list){
        if(list != null && list.length > 0) {
            this.mData.addAll(Arrays.asList(list));
        }
        notifyDataSetChanged();
    }

    public T remove(int index) {
        if (index < 0 || index >= mData.size()) {
            return null;
        }
        return this.mData.remove(index);
    }

    public void removeAll() {
        this.mData.clear();
    }


    protected void setHolderListeners(@NonNull final ViewHolder holder) {
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null){
                    itemClickListener.onItemClick(holder,v);
                }
            }
        });

        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(itemClickListener != null){
                    itemClickListener.onItemLongClick(holder, v);
                }
                return true;
            }
        });
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> holder = new SparseArray<>();

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public <T extends View> T getView(int id) {
            View view = this.holder.get(id);
            if(view == null) {
                view = this.itemView.findViewById(id);
                this.holder.put(id, view);
            }
            return (T) view;
        }

        //FIXME 优化onItemClick和onLongClick回调  这种为每个item添加点击监听的解决方案是浪费性能的
        public void setOnLongClickListener(View.OnLongClickListener listener) {
            this.itemView.setOnLongClickListener(listener);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            this.itemView.setOnClickListener(listener);
        }
    }
}