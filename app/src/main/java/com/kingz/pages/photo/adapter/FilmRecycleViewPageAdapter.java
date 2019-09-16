package com.kingz.pages.photo.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.App;
import com.kingz.mode.PosterGroupInfo;
import com.kingz.net.OkHttpClientManager;
import com.kingz.pages.photo.filmlist.FilmListRecyclerViewHolder;
import com.kingz.utils.EncryptTools;
import com.kingz.utils.FileUtils;
import com.kingz.utils.ZLog;

import java.io.File;
import java.util.List;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2017/2/7 21:51
 * description:
 */

public class FilmRecycleViewPageAdapter extends RecyclerView.Adapter<FilmListRecyclerViewHolder> {

    private static String TAG = FilmRecycleViewPageAdapter.class.getSimpleName();

    /**
     * 储存海报图片的文件信息
     */
    private static final String FILM_FILE_NAME = "FilmVideoInfo.dat";
    private static final String FILM_IMAGE_DATA = "FilmPoster.dat";
    private static String FILMIMG_PATH_NAME = "FilmPageDir";
    private static File dataPath;


    /**
     * 每一个海报的View
     */
    private ImageView itemView;

    /**
     * 影片数据
     */
    private List<PosterGroupInfo.Poster> posterList;
    private OnItemClickListener clickListener;

    List<PosterGroupInfo.Poster> cacheData;

    /**
     * Item回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int posotion);

        void onItemLongClick(View view, int posotion);
    }


    /**
     * 设置回调监控
     */
    public void setOnItemClickLinstener(OnItemClickListener listener) {
        this.clickListener = listener;
    }


    public FilmRecycleViewPageAdapter(List<PosterGroupInfo.Poster> posterList) {
        //指定缓存路径\路径名
        dataPath = new File(App.getAppInstance().getAppContext().getCacheDir().getPath(), FILMIMG_PATH_NAME);
        if (!dataPath.exists()) {
            dataPath.mkdirs();
        } else if (dataPath.isFile()) {
            throw new IllegalArgumentException("dataPath is file");
        }
        if (posterList == null) {
            throw new IllegalArgumentException("posterList must not be null");
        }
        this.posterList = posterList;

        //保存当前海报信息
        FileUtils.saveObjectWithPath(posterList, new File(dataPath, FILM_FILE_NAME));
        ZLog.d(TAG, "储存影片数据至本地指定目录!");
    }

    @Override
    public FilmListRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        cacheData = (List<PosterGroupInfo.Poster>) FileUtils.readObjectWithPath(new File(dataPath, FILM_FILE_NAME));
        itemView = new ImageView(viewGroup.getContext());
        itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new FilmListRecyclerViewHolder(itemView);
    }

    /**
     * 每一个ViewHolder的数据绑定回调
     */
    @Override
    public void onBindViewHolder(FilmListRecyclerViewHolder viewHolder, int i) {
        PosterGroupInfo.Poster mPoster = posterList.get(i);
        String md5 = EncryptTools.MD5(mPoster.poster_ur);
        if(TextUtils.isEmpty(md5)){
            return;
        }
        File file = new File(dataPath, md5);
        if(FileUtils.readObjectWithPath(file) != null){
            //加载缓存图片
            Bitmap cacheBitmap = (Bitmap) FileUtils.readObjectWithPath(file);
            viewHolder.getmImageView().setImageBitmap(cacheBitmap);
            return;
        }
        //请求网络图片数据
        OkHttpClientManager.displayImage(viewHolder.getmImageView(), mPoster.poster_ur);
//        ViewGroup.LayoutParams lp = viewHolder.getmImageView().getLayoutParams();
//        lp.height = ;
//        viewHolder.getTvLabel().setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return posterList.size();
    }


}
