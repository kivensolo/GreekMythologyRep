package com.kingz.holder;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * author: King.Z <br>
 * date:  2016/8/18 20:16 <br>
 * description: 通用ViewHolder
 */
public class CommonViewHolder {

    /**
     * View容器，用于存放Holer中的View
     */
    private SparseArray<View> viewsArray;
    private View inflateView;

    private CommonViewHolder(int layoutId, ViewGroup parent) {
        viewsArray = new SparseArray<>();
        inflateView = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        inflateView.setTag(this);
    }

    private CommonViewHolder(View view) {
        viewsArray = new SparseArray<>();
        inflateView = view;
        inflateView.setTag(this);
    }

    /**
     * 创建 CommonViewHolder
     * @param postion     子数据在整个数据集中的位置
     * @param convertView 重用旧视图（需检查是否为空）
     * @param parent      该视图最终将附加到的父级
     * @param layoutId    需要填充的View布局id
     * @return CommonViewHolder
     */
    public static CommonViewHolder create(int postion, View convertView,
                                          ViewGroup parent, int layoutId) {
        if (convertView == null) {
            return new CommonViewHolder(layoutId, parent);
        } else {
            return (CommonViewHolder) convertView.getTag();
        }
    }

    /**
     * 创建 CommonViewHolder
     * @param postion     子数据在整个数据集中的位置
     * @param convertView 重用旧视图（需检查是否为空）
     * @param view        手动设置的需要填充的View
     * @return CommonViewHolder
     */
    public static CommonViewHolder create(int postion, View convertView, View view) {
        if (convertView == null) {
            return new CommonViewHolder(view);
        } else {
            return (CommonViewHolder) convertView.getTag();
        }
    }

    public <T extends View> T getView(int viewId){
        View view = viewsArray.get(viewId);
        if(null == view){
            view = inflateView.findViewById(viewId);
            viewsArray.put(viewId,view);
        }
        return (T) view;
    }

    public View getInflateView(){
        return inflateView;
    }
}
