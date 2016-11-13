package com.view.listview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.kingz.customdemo.R;
import com.photo.DemoRecyclerAdapter;
import com.photo.MyItemDecoration;
import com.photo.KingZTestDataModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/11/13 22:23
 * description:recyclerview 的Demo页面
 *   LayoutManager: 管理RecyclerView的结构，控制其显示的方式。
     Adapter: 处理每个Item的显示.
     ItemDecoration: 添加每个Item的装饰，控制Item间的间隔.
     ItemAnimator: 负责添加\移除\重排序时的动画效果，控制Item增删的动画.
     LayoutManager\Adapter是必须, ItemDecoration\ItemAnimator是可选.

 RecyclerView架构，提供了一种插拔式的体验，高度的解耦，
 异常的灵活，通过设置它提供的不同LayoutManager，ItemDecoration , ItemAnimator实现令人瞠目的效果。
 */
public class RecyclerviewDemo extends Activity{

    private RecyclerView view;
    private DemoRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_demo);
        view = (RecyclerView) findViewById(R.id.test_recycler_view);
        initRecyclerView(view);
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

    private void initRecyclerLayoutManager(RecyclerView recyclerView) {
        //LayoutManager--------->GridLayoutManager(网格)/LinearLayoutManager(线性)/StaggeredGridLayoutManager(错列网格)
        // 错列网格布局 spanCount：方向垂直，就是列数  方向水平，就是行数
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL));
    }

    private void initRecyclerAdapter(RecyclerView recyclerView) {
        mAdapter = new DemoRecyclerAdapter(getData());
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 项的装饰, 比如ListView中的分割线, 在本例中, 左右两条粉线.
     */
    private void initItemDecoration(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new MyItemDecoration(this));
    }

    private ArrayList<KingZTestDataModel> getData() {
        int count = 10;
        ArrayList<KingZTestDataModel> data = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            KingZTestDataModel model = new KingZTestDataModel();
            model.setDateTime(getBeforeDay(new Date(), i));
            model.setLabel("No. " + i);
            data.add(model);
        }
        return data;
    }

    /**
     * 手动拼接日期数据
     * @param date 日期
     * @param i    偏移量
     * @return 新的日期
     */
    private static Date getBeforeDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, i * (-1));
        return calendar.getTime();
    }
}
