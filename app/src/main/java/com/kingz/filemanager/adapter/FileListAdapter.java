package com.kingz.filemanager.adapter;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.kingz.holder.CommonViewHolder;
import com.zeke.kangaroo.adapter.CommonListAdapter;
import com.zeke.ktx.App;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by KingZ.
 * Data: 2016 2016/2/27
 * Discription:文件管理器的适配器
 */
public class FileListAdapter extends CommonListAdapter<File> {

    private static final String TAG = FileListAdapter.class.getSimpleName();

    private TextView nameView;
    private TextView sizeView;
    private TextView typeView;
    private TextView dataView;
    private ImageView imgView;
    private File itemFile;

    private boolean isRoot;           //是否根目录
    private String  itemFileName;     //文件名


    public FileListAdapter(ArrayList<File> filesList, boolean isRoot) {
        super(filesList);
        this.isRoot = isRoot;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CommonViewHolder holder = CommonViewHolder.create(position,
                convertView,parent,R.layout.filemanager_list_item);
        nameView = holder.getView(R.id.file_name);
        sizeView = holder.getView(R.id.file_size);
        typeView = holder.getView(R.id.file_type);
        dataView = holder.getView(R.id.file_date);
        imgView  = holder.getView(R.id.file_img);
        porocessDirAndFile(position);
        return holder.getInflateView();
    }

    private void porocessDirAndFile(int position) {
        itemFile = (File) getItem(position);
        if(position == 0 && !isRoot) {
            imgView.setVisibility(View.GONE);
            sizeView.setVisibility(View.GONE);
            typeView.setVisibility(View.GONE);
            dataView.setVisibility(View.GONE);
            nameView.setText("返回上一级");
            nameView.setTextSize(28);
            nameView.setTextColor(App.instance.getApplicationContext()
                    .getResources().getColor(R.color.qianpurple));
            nameView.setGravity(Gravity.START|Gravity.CENTER);
            nameView.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
        }else{
            itemFileName = itemFile.getName();
            nameView.setText(itemFileName);
            if (itemFile.isDirectory()) {
                sizeView.setText("文件夹");
                sizeView.setVisibility(View.VISIBLE);
                imgView.setVisibility(View.VISIBLE);
                dataView.setVisibility(View.GONE);
                typeView.setVisibility(View.GONE);
                nameView.setTextSize(22);
            } else {
                imgView.setVisibility(View.INVISIBLE);
                long fileSize = itemFile.length();
                if (fileSize > 1024 * 1024) {
                    float size = fileSize / (1024f * 1024f);
                    sizeView.setText(new DecimalFormat("#.00").format(size) + "MB");
                } else if (fileSize >= 1024) {
                    float size = fileSize / 1024;
                    sizeView.setText(new DecimalFormat("#.00").format(size) + "KB");
                } else {
                    sizeView.setText(fileSize + "B");
                }
                int dot = itemFileName.indexOf(".");
                if (dot > -1 && dot < (itemFileName.length() - 1)) {
                    Log.d(TAG,"该文件类型 ：" + itemFileName.substring(dot + 1,itemFileName.length() - 1));
                    typeView.setText(itemFileName.substring(dot + 1,itemFileName.length() - 1)+"文件");
                }
                dataView.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(itemFile.lastModified()));
            }
        }
    }
}
