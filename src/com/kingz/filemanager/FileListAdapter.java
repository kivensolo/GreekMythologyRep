package com.kingz.filemanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.kingz.uiusingListViews.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by KingZ.
 * Data: 2016 2016/2/27
 * Discription:文件管理器适配器
 */
public class FileListAdapter extends BaseAdapter{

    private static final String TAG = "FileListAdapter";
    private Context contex;
    private ArrayList<File> filesList;
    private LayoutInflater mInflater;
    private boolean isRoot;     //是否根目录

    public FileListAdapter(Context contex, ArrayList<File> filesList, boolean isRoot) {
        mInflater = LayoutInflater.from(contex);
        this.contex = contex;
        this.filesList = filesList;
        this.isRoot = isRoot;
    }

    @Override
    public int getCount() {
        return filesList.size();
    }

    @Override
    public Object getItem(int position) {
        return  filesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHolder ;
        if(convertView == null){
            viewHolder =  new ViewHodler();
            convertView = mInflater.inflate(R.layout.filemanager_list_item,null,false);
            //给视图贴上一个TAG（Object类型），便于以后查找
            convertView.setTag(viewHolder);
            viewHolder.fileName = (TextView) convertView.findViewById(R.id.file_name);
            viewHolder.fileSize = (TextView) convertView.findViewById(R.id.file_size);
            viewHolder.fileTypeImg = (ImageView) convertView.findViewById(R.id.file_img);
        }else{
            viewHolder = (ViewHodler) convertView.getTag();
        }
        File currentfile = (File) getItem(position);
//        viewHolder.fileTypeImg.setTag(name); //将文件名、文件夹名设置为TAG,用来判断是什么类型的文件
        if(position == 0 && !isRoot) {
            Log.d(TAG, "非根目录");
            viewHolder.fileName.setText("返回上一级");
//            viewHolder.fileName.setBackgroundColor(contex.getResources().getColor(R.color.chartreuse));
            viewHolder.fileTypeImg.setVisibility(View.GONE);
//            viewHolder.fileName.setVisibility(View.GONE);
            viewHolder.fileSize.setVisibility(View.GONE);
        }else{
            viewHolder.fileName.setText(currentfile.getName());
//            viewHolder.fileSize.setText("333");
            if (currentfile.isDirectory()) {
                viewHolder.fileSize.setText("文件夹");
                viewHolder.fileTypeImg.setVisibility(View.VISIBLE);
                viewHolder.fileSize.setTextColor(0xFF00FF99);
            } else {
                long fileSize = 2049; //先设置个固定大小
                if (fileSize > 1024 * 1024) {
                    float size = fileSize / (1024f * 1024f);
                    viewHolder.fileSize.setText(new DecimalFormat("#.00").format(size) + "MB");
                } else if (fileSize >= 1024) {
                    float size = fileSize / 1024;
                    viewHolder.fileSize.setText(new DecimalFormat("#.00").format(size) + "KB");
                } else {
                    viewHolder.fileSize.setText(fileSize + "B");
                }
                int dot = filesList.indexOf(".");
//            if (dot > -1 && dot < (fileNames.length() - 1)) {
////                viewHolder.type.setText(fileName.substring(dot + 1) + "文件");
//            }
//            viewHolder.data.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(file.lastModified()));
            }
        }
        return convertView;
    }

    class ViewHodler{
        public TextView fileName;
        public TextView fileSize;
        public ImageView fileTypeImg;
    }
}
