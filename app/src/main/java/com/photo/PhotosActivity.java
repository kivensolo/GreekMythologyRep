package com.photo;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.BaseActivity;
import com.adapter.BitmapPageAdapter;
import com.kingz.customdemo.R;
import com.utils.BitMapUtils;

import java.util.*;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/6 23:05
 * description:
 *
 *   一个采用recycleView的布局页面来显示图片特效（还未完成）.
 *       LayoutManager: 管理RecyclerView的结构.
         Adapter: 处理每个Item的显示.
         ItemDecoration: 添加每个Item的装饰.
         ItemAnimator: 负责添加\移除\重排序时的动画效果.

        LayoutManager\Adapter是必须, ItemDecoration\ItemAnimator是可选.
 *
 *
 *   tips:8月7号-----打算用GridView来显示。  或者listView  点击后变换图片
 *
 *
 */
public class PhotosActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    public static final String TAG = "PhotosActivity";

    private Bitmap srcBitmap;
    private ListView listView;
    private BitmapPageAdapter bitmapAdapter;
    private ImageView img1;

    private RecyclerView recyclerView;
    private DemoRecyclerAdapter mAdapter;

    String[] strs = {"平移图片", "放大/缩小图片", "旋转图片","圆角图片","圆形图片",
            "斜切图片","水印---图片","水印---文字","倒影",};
    List<String> datas = Arrays.asList(strs);

    @Override
    protected void findID() {
        super.findID();
        setContentView(R.layout.photos_activity);
//        recyclerView = (RecyclerView) findViewById(R.id.test_recycler_view);
//        initRecyclerView(recyclerView);
        bitmapAdapter = new BitmapPageAdapter(this,datas);
        listView = (ListView) findViewById(R.id.type_change_id);
        listView.setAdapter(bitmapAdapter);
        listView.setOnItemClickListener(this);
        img1 = (ImageView) findViewById(R.id.normal_pic);
        setImageView();
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


    Bitmap waterMark;
    private void setImageView() {
        srcBitmap = BitMapUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.sunyanzi_1), 680, 420);
        waterMark = BitMapUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.m), 110,132);
        //加载原始图片
        img1.setImageBitmap(srcBitmap);
        ////加载平移的图片
        //img2.setImageBitmap(srcBitmap);
        //
        ////加载剪切的图片
        //img3.setImageBitmap(BitMapUtils.setZoomImg(srcBitmap,160,90));
        //
        ////旋转(顺时针)
        //img4.setImageBitmap(BitMapUtils.setRotateImage(180, srcBitmap));
        //
        ////圆角矩形
        //img5.setImageBitmap(BitMapUtils.setRoundCorner(srcBitmap,45));
        //
        ////圆形图片
        //img6.setImageBitmap(BitMapUtils.setBitmapCircle(srcBitmap));
        //
        ////X/Y轴倾斜图片
        //img7.setImageBitmap(BitMapUtils.setSkew(srcBitmap,-0.3f,0));
        //
        ////添加水印图片
        //img8.setImageBitmap(BitMapUtils.createWaterMarkBitmap(srcBitmap,waterMark,srcBitmap.getWidth() - waterMark.getWidth(),0));
        //
        ////添加水印文字
        ////img9.setImageBitmap(BitMapUtils.createWaterMarkText(srcBitmap,"测试水印",
        ////        srcBitmap.getWidth() - srcBitmap.getWidth()/2,srcBitmap.getHeight() - srcBitmap.getHeight()/2));
        //
        //img9.setImageBitmap(BitMapUtils.setInvertedBitmap(srcBitmap,srcBitmap.getHeight()/3));
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
            default:
                Log.i(TAG,"未匹配");
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
