package com.kingz.holder;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * author: King.Z <br>
 * date:  2016/8/18 20:16 <br>
 * description: 通用ViewHolder
 */
public class BaseViewHolder {

    private SparseArray<View> viewsArray;
    private View inflateView;
    private int mPosition;

    public static BaseViewHolder getHolder(Context mContext, View view,
                                           ViewGroup parent, int layoutId, int postion) {
        if (view == null) {
            return new BaseViewHolder(mContext, parent, layoutId, postion);
        } else {
            BaseViewHolder holder = (BaseViewHolder) view.getTag();
            holder.mPosition = postion;
            return holder;
        }
    }

    private BaseViewHolder(Context mContext, ViewGroup parent, int layoutId, int postion) {
        viewsArray = new SparseArray<>();
        mPosition = postion;
        inflateView = LayoutInflater.from(mContext).inflate(layoutId,parent,false);
        inflateView.setTag(this);
    }

    public View getInflateView(){
        return inflateView;
    }

    public <T extends View> T getView(int viewId){
        View view = viewsArray.get(viewId);//从缓存的Map中查
        if(null == view){
            view = inflateView.findViewById(viewId);
            viewsArray.put(viewId,view);
        }
        return (T) view;
    }
}
