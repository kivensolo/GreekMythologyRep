package com.photo;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import com.bumptech.glide.Glide;
import com.kingz.customdemo.R;
import com.utils.BitMapUtils;
import com.utils.ToastTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/6 23:05
 * description: Listview显示的页面
 *
 *   (已废弃)：一个采用recycleView的布局页面来显示图片特效（还未完成）.
 *      LayoutManager: 管理RecyclerView的结构.
        Adapter: 处理每个Item的显示.
        ItemDecoration: 添加每个Item的装饰.
        ItemAnimator: 负责添加\移除\重排序时的动画效果.
        LayoutManager\Adapter是必须, ItemDecoration\ItemAnimator是可选.
 */
public class BitmapPhotosActivity extends PhotosActivity{

    public static final String TAG = "BitmapPhotosActivity";

    private DemoRecyclerAdapter mAdapter;

    String[] strs = {"平移图片", "放大/缩小图片", "旋转图片","圆角图片","圆形图片",
            "斜切图片","水印---图片","水印---文字","倒影","Glide加载图片"};

    @Override
    protected void findID() {
        datas = Arrays.asList(strs);
        setImageView();
        super.findID();
    }

    /**
     * 初始化RecyclerView
     * @param recyclerView
     */
    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);         // 设置固定大小
        initRecyclerLayoutManager(recyclerView);    // 初始化布局
        initRecyclerAdapter(recyclerView);          // 初始化适配器
        initItemDecoration(recyclerView);           // 初始化装饰
//        initItemAnimator(recyclerView);           // 初始化动画效果
    }

    /**
     * 管理RecyclerView的布局结构
     * @param recyclerView
     */
    private void initRecyclerLayoutManager(RecyclerView recyclerView) {
        //LayoutManager:GridLayoutManager(网格)/LinearLayoutManager(线性)/StaggeredGridLayoutManager(错列网格)
        // 错列网格布局 spanCount：方向垂直，就是列数  方向水平，就是行数
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
    }

    /**
     * 初始化适配器
     * @param recyclerView
     */
    private void initRecyclerAdapter(RecyclerView recyclerView) {
        //创建初始化数据
        mAdapter = new DemoRecyclerAdapter(getData());
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 项的装饰, 比如ListView中的分割线, 在本例中, 左右两条粉线.
     * @param recyclerView
     */
    private void initItemDecoration(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new MyItemDecoration(this));
    }

    private void setImageView() {
        srcBitmap = BitMapUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.sunyanzi_1), 680, 420);
        waterMark = BitMapUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.m), 110,132);
        //加载原始图片
        img1.setImageBitmap(srcBitmap);
    }

    @Override
    public void showLoadingDialog() {
        super.showLoadingDialog();

    }

    @Override
    protected void Listener() {
        super.Listener();
    }

    @Override
    protected void initIntent() {
        super.initIntent();
    }

    /**
     * 模拟的数据
     *
     * @return 数据
     */
    private ArrayList<PhotoDataModel> getData() {
        int count = 10;
        ArrayList<PhotoDataModel> data = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            PhotoDataModel model = new PhotoDataModel();
            model.setDateTime(getBeforeDay(new Date(), i));
            model.setLabel("No. " + i);
            data.add(model);
        }
        return data;
    }
    /**
     * 获取日期的前一天
     *
     * @param date 日期
     * @param i    偏离
     * @return 新的日期
     */
    private static Date getBeforeDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, i * (-1));
        return calendar.getTime();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view,position,id);
        String clickedtype = (String) bitmapAdapter.getItem(position);
        Log.i(TAG,"onItemClick： chooseTpye = " + clickedtype);
        switch (position){
            case 0:
                setShowBitMap(srcBitmap);
                break;
            case 1:
                setShowBitMap(BitMapUtils.setZoomImg(srcBitmap,160,90));
                break;
            case 2:
                setShowBitMap(BitMapUtils.setRotateImage(180, srcBitmap));
                break;
            case 3:
                setShowBitMap(BitMapUtils.setRoundCorner(srcBitmap,45));
                break;
            case 4:
                setShowBitMap(BitMapUtils.setBitmapCircle(srcBitmap));
                break;
            case 5:
                setShowBitMap(BitMapUtils.setSkew(srcBitmap,-0.3f,0));
                break;
            case 6:
                setShowBitMap(BitMapUtils.createWaterMarkBitmap(srcBitmap,waterMark,srcBitmap.getWidth() - waterMark.getWidth(),0));
                break;
            case 7:
                setShowBitMap(BitMapUtils.createWaterMarkText(srcBitmap,"测试水印",
                srcBitmap.getWidth() - srcBitmap.getWidth()/2,srcBitmap.getHeight() - srcBitmap.getHeight()/2));
                break;
            case 8:
                setShowBitMap(BitMapUtils.setInvertedBitmap(srcBitmap,srcBitmap.getHeight()/3));
                break;
            case 9:
                Glide.with(this)
                    .load("http://nuuneoi.com/uploads/source/playstore/cover.jpg")
                    .error(R.mipmap.sample_2)
                    .fitCenter() //图片比ImageView大的时候，就会按照比例对图片进行缩放，并将图片居中显示。如果这张图片比ImageView小，那么就会根据比例对图片进行扩大，然后将其居中显示
                    .centerCrop()
                    .into(img1);
                break;
            default:
                ToastTools.getInstance().showMgtvWaringToast(this,"未匹配");
                break;
        }
    }

    private void setShowBitMap(final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                img1.setImageBitmap(bitmap);
            }
        });
    }
}

