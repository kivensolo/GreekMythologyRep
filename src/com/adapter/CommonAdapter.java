package com.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/3/23 23:16
 * description: 万用适配器 抽象类
 *
 *  数据源的类型改为T
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    /**上下文 */
	protected Context mContext;

    /** 数据源 */
	protected List<T> listDatas;

    /** Item布局ID */
	protected int layoutId;

    public CommonAdapter(Context context, List<T> listDatas, int layoutId) {
		this.mContext = context;
		this.listDatas = listDatas;
		this.layoutId = layoutId;
	}

    @Override
    public int getCount() {
        return listDatas == null ? 0 : listDatas.size();
    }

    /**
	 * 获取当前点击的Item的数据时用
	 * 在onItemClick中 parent.getAdapter().getItem(),
     * 获取当前点击的Item的数据
	 */
    @Override
    public Object getItem(int position) {
        return listDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder holder = CommonViewHolder.getViewHolder(mContext, convertView,
				parent, layoutId, position);
		fillData(holder, position);
		return holder.getConvertView();
    }

    /**
	 *
	 * 抽象方法，用于子类实现，填充数据
	 * @param holder
	 * @param position
	 */
	 protected abstract void fillData(CommonViewHolder holder, int position);
}
