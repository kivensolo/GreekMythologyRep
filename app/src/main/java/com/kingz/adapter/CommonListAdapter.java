package com.kingz.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.kingz.holder.BaseViewHolder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by KingZ.
 * Data: 2016 2016/8/18
 * Discription: 通用 List 适配器
 */
public abstract class CommonListAdapter<T> extends BaseAdapter {
    protected BaseViewHolder commonViewHolder;
    protected LayoutInflater mInflater;
    protected Context mContex;
    protected List<T> mData;

    public CommonListAdapter(Context contex, List<T> datas) {
        this.mContex = contex;
        mInflater = LayoutInflater.from(mContex);
        this.mData = datas;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        if (position < 0 || position >= mData.size()) {
            return null;
        }
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    //如果有统一的ItemView样式
    //则可以把commonViewHolder = BaseViewHolder.getHolder(mContex,convertView,parent,R.layout.filemanager_list_item,position);写到getView里面

    protected abstract void fillData(CommonViewHolder holder, int position);

    public CommonListAdapter<T> addItem(T obj, int index) {
        this.mData.add(index, obj);
        return this;
    }

    public void addAll(List<T> list) {
        if (list != null) {
            this.mData.addAll(list);
        } else {
            removeAll();
        }
        notifyDataSetChanged();
    }

    public void addAll(T... list) {
        if (list != null && list.length > 0) {
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


    /**
     * 通用 List ViewHodler
     */
    public static final class CommonViewHolder {
        /**
         * View容器，用于存放Holer中的View
         * SparseArray: Android推荐使用的一个优化容器，
         * 相当于一个Map<integer,View>
         */
        private SparseArray<View> holders = new SparseArray<>();

        /**
         * Item布局View convertView
         */
        private View mConvertView;

        public CommonViewHolder(View itemView) {
            mConvertView = itemView;
            mConvertView.setTag(this);
        }

        public CommonViewHolder(Context context, ViewGroup parent, int layoutId) {
            mConvertView = LayoutInflater.from(context).inflate(layoutId, null);
            mConvertView.setTag(this);
        }

        /**
         * 获取ViewHolder
         */
        public static CommonViewHolder getViewHolder(Context context, View convertView,
                                                     ViewGroup parent, int layoutId, int position) {
            if (convertView == null) {
                return new CommonViewHolder(context, parent, layoutId);
            }
            return (CommonViewHolder) convertView.getTag();
        }

        public <T extends View> T getView(int viewId) {
            View item = holders.get(viewId);
            if (item == null) {
                item = mConvertView.findViewById(viewId);
                holders.put(viewId, item);
            }
            return (T) item;
        }

        /**
         * 获取convertView
         * @return
         */
        public View getConvertView() {
            return mConvertView;
        }

    }
}