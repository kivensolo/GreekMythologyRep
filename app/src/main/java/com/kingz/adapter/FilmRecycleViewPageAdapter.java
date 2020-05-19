package com.kingz.adapter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.kingz.customdemo.R;
import com.kingz.mode.PosterGroupInfo;
import com.kingz.pages.photo.filmlist.FilmListRecyclerViewHolder;
import com.zeke.kangaroo.utils.EncryptTools;
import com.zeke.kangaroo.utils.FileUtils;
import com.zeke.kangaroo.utils.ZLog;
import com.zeke.ktx.App;

import java.io.File;
import java.util.List;

/**
 * author: King.Z
 * date:  2017/2/7 21:51
 * description:
 * TODO 使用顶级抽象Adapter处理
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

    private ImageView itemView;

    /**
     * 影片数据
     */
    private List<PosterGroupInfo.Poster> posterList;

    RequestOptions requestOptions = new RequestOptions()
            .placeholder(R.drawable.android) //设置“加载中”状态时显示的图片
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .error(R.drawable.alert_dialog_icon); //设置“加载失败”状态时显示的图片


    public FilmRecycleViewPageAdapter(List<PosterGroupInfo.Poster> posterList) {
        //指定缓存路径\路径名
        dataPath = new File(App.instance.getApplicationContext().getCacheDir().getPath(), FILMIMG_PATH_NAME);
        if (!dataPath.exists()) {
            dataPath.mkdirs();
        } else if (dataPath.isFile()) {
            throw new IllegalArgumentException("dataPath is file");
        }
        if (posterList == null) {
            throw new IllegalArgumentException("posterList must not be null");
        }
        this.posterList = posterList;

        // 保存当前海报信息
        // TODO 使用glide替换。
        FileUtils.saveObjectWithPath(posterList, new File(dataPath, FILM_FILE_NAME));
        ZLog.d(TAG, "储存影片数据至本地指定目录!");
    }

    @Override
    public FilmListRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
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
        // FIXME: Glide会报错 No content provider:  xxxxx
        // 原因是图片有问题。 换一个图片就好了
        Glide.with(App.instance.getApplicationContext())
             // .load(mPoster.poster_ur)
             .load("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1415616805,3449257715&fm=26&gp=0.jpg")
             .apply(requestOptions)
             .transition(DrawableTransitionOptions.withCrossFade(600))
             .into(itemView);

        // OkHttpClientManager.displayImage(viewHolder.getmImageView(), mPoster.poster_ur);

//        ViewGroup.LayoutParams lp = viewHolder.getmImageView().getLayoutParams();
//        lp.height = ;
//        viewHolder.getTvLabel().setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return posterList.size();
    }


}
