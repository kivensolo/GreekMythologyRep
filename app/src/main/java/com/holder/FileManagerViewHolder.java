package com.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.kingz.customdemo.R;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/8/18 20:16 <br>
 * description: 文件管理器ViewHolder <br>
 */
public class FileManagerViewHolder {

    private Context mContext;
    private View convertView;
    public TextView fileName;
    public TextView fileSize;
    public TextView fileData;
    public TextView fileType;
    public ImageView fileTypeImg;

    public FileManagerViewHolder(Context mContext, View convertView, ViewGroup parent) {
        this.mContext = mContext;
        if (convertView == null) {
            this.convertView = LayoutInflater.from(mContext).inflate(R.layout.filemanager_list_item, null, false);
            //convertView.setTag();
        }else{
            this.convertView = convertView;
        }
    }

    public void getView(int id){
        fileName = (TextView) convertView.findViewById(R.id.file_name);
        fileSize = (TextView) convertView.findViewById(R.id.file_size);
        fileType = (TextView) convertView.findViewById(R.id.file_type);
        fileData = (TextView) convertView.findViewById(R.id.file_date);
        fileTypeImg = (ImageView) convertView.findViewById(R.id.file_img);
    }
}
