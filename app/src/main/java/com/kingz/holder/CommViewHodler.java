package com.kingz.holder;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/8/18 20:16 <br>
 * description: 万能ViewHolder <br>
 * 用Map来储存ItemView
 */
public class CommViewHodler {

    private Context mContext;
    private SparseArray<View> viewsMap;
    private View mConvertView;
    private int mPosition;

    public static CommViewHodler getHolder(Context mContext, View convertView,
                                     ViewGroup parent,int layoutId, int postion) {
        if (convertView == null) {
            return new CommViewHodler(mContext, parent, layoutId, postion);
        } else {
            CommViewHodler holder = (CommViewHodler) convertView.getTag();
            holder.mPosition = postion;
            return holder;
        }
    }

    public CommViewHodler(Context mContext, ViewGroup parent,
                          int layoutId, int postion) {
        viewsMap = new SparseArray<>();
        this.mContext = mContext;
        mPosition = postion;
        mConvertView = LayoutInflater.from(mContext).inflate(layoutId,parent,false);
        mConvertView.setTag(this);
    }

    public View getmConvertView(){
        return mConvertView;
    }

    public <T extends View> T getView(int viewId){
        View view = viewsMap.get(viewId);//从缓存的Map中查
        if(null == view){
            view = mConvertView.findViewById(viewId);
            viewsMap.put(viewId,view);
        }
        return (T) view;
    }
}
