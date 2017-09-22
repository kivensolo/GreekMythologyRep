package com.kingz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.kingz.holder.CommViewHodler;

import java.util.List;

/**
 * Created by KingZ.
 * Data: 2016 2016/8/18
 * Discription:万能适配器
 */
public abstract class CommonAdapter<T> extends BaseAdapter{

    private static final String TAG = CommonAdapter.class.getSimpleName();
    protected CommViewHodler commonViewHolder;
    protected LayoutInflater mInflater;
    protected Context mContex;
    protected List<T> mData;

    public CommonAdapter(Context contex,List<T> datas) {
        this.mContex = contex;
        mInflater = LayoutInflater.from(mContex);
        this.mData = datas;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return  mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    //如果有统一的ItemView样式
    //则可以把commonViewHolder = CommViewHodler.getHolder(mContex,convertView,parent,R.layout.filemanager_list_item,position);写到getView里面

    protected abstract void fillData(CommonViewHolder holder, int position);

	public void setItems(List<T> items){
		if(items != null){
			this.mData =items;
		}else{
			this.mData.clear();
		}
		notifyDataSetChanged();
	}

	public void addItems(List<T> items){
		if(items!= null){
			this.mData.addAll(items);
		}else{
			this.mData.clear();
		}
		notifyDataSetChanged();
	}
}
