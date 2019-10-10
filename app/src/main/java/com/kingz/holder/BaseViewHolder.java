package com.kingz.holder;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * author: King.Z <br>
 * date:  2016/8/18 20:16 <br>
 * description:
 *  通用ViewHolder
 */
public class BaseViewHolder {

    private Context mContext;
    private SparseArray<View> viewsMap;
    private View mConvertView;
    private int mPosition;

    public static BaseViewHolder getHolder(Context mContext, View convertView,
                                           ViewGroup parent, int layoutId, int postion) {
        if (convertView == null) {
            return new BaseViewHolder(mContext, parent, layoutId, postion);
        } else {
            BaseViewHolder holder = (BaseViewHolder) convertView.getTag();
            holder.mPosition = postion;
            return holder;
        }
    }

    private BaseViewHolder(Context mContext, ViewGroup parent, int layoutId, int postion) {
        viewsMap = new SparseArray<>();
        this.mContext = mContext;
        mPosition = postion;
        mConvertView = LayoutInflater.from(mContext).inflate(layoutId,parent,false);
        mConvertView.setTag(this);
    }

    public View getmConvertView(){
        return mConvertView;
    }

    public View getView(int viewId){
        View view = viewsMap.get(viewId);//从缓存的Map中查
        if(null == view){
            view = mConvertView.findViewById(viewId);
            viewsMap.put(viewId,view);
        }
        return view;
    }
}
