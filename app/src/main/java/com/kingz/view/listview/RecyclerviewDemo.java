package com.kingz.view.listview;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import com.kingz.adapter.DemoRecyclerAdapter;
import com.kingz.customdemo.R;
import com.kingz.mode.RecycleDataInfo;
import com.kingz.pages.photo.filmlist.MyItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/11/13 22:23
 * description:recyclerview 的Demo页面
 * LayoutManager: 管理RecyclerView的结构，控制其显示的方式。
 * Adapter: 处理每个Item的显示.
 * ItemDecoration: 添加每个Item的装饰，控制Item间的间隔.
 * ItemAnimator: 负责添加\移除\重排序时的动画效果，控制Item增删的动画.
 * LayoutManager\Adapter是必须, ItemDecoration\ItemAnimator是可选.
 * <p>
 * RecyclerView架构，提供了一种插拔式的体验，高度的解耦，
 * 异常的灵活，通过设置它提供的不同LayoutManager，ItemDecoration , ItemAnimator实现令人瞠目的效果。
 */
public class RecyclerviewDemo extends Activity {

    private RecyclerView view;
    private DemoRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_demo);
        view = (RecyclerView) findViewById(R.id.test_recycler_view);
        initRecyclerView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                mAdapter.addData(1);
                break;
            case R.id.action_delete:
                mAdapter.removeData(1);
                break;
        }
        return true;
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);         // 设置固定大小  适配器更改不能影响RecyclerView的大小
        initRecyclerLayoutManager(recyclerView);    // 初始化布局
        initRecyclerAdapter(recyclerView);          // 初始化适配器
        initItemDecoration(recyclerView);           // 初始化装饰
        initItemAnimator(recyclerView);             // 初始化动画效果
    }

    private void initRecyclerLayoutManager(RecyclerView recyclerView) {
        //LayoutManager--------->GridLayoutManager(网格)/LinearLayoutManager(线性)/StaggeredGridLayoutManager(错列网格)
        // 错列网格布局 spanCount：方向垂直:列数  方向水平:行数
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
    }

    private void initRecyclerAdapter(RecyclerView recyclerView) {
        mAdapter = new DemoRecyclerAdapter(getData());
        mAdapter.setOnItemClickLitener(new OnRecycleViewItemClickLitener());
        recyclerView.setAdapter(mAdapter);
    }

    private void initItemDecoration(RecyclerView recyclerView) {
        //调用addItemDecoration()方法添加decoration的时候，RecyclerView在绘制的时候，会去绘制decorator，即调用该类的onDraw和onDrawOver方法，
        recyclerView.addItemDecoration(new MyItemDecoration(this, -1));
    }

    private void initItemAnimator(RecyclerView recyclerView) {
        Animation deleteAnimation = AnimationUtils.loadAnimation(this, R.anim.push_up_in);
        deleteAnimation.setRepeatMode(ValueAnimator.RESTART);
        deleteAnimation.setRepeatCount(0);
        deleteAnimation.setDuration(300);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAnimation(deleteAnimation);
    }

    private ArrayList<RecycleDataInfo> getData() {
        int count = 40;
        ArrayList<RecycleDataInfo> data = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            RecycleDataInfo model = new RecycleDataInfo();
            model.setDateTime(getBeforeDay(new Date(), i));
            model.setLabel("No. " + i);
            data.add(model);
        }
        return data;
    }

    /**
     * 手动拼接日期数据
     *
     * @param date 日期
     * @param i    偏移量
     * @return 新的日期
     */
    private static Date getBeforeDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, i);
        return calendar.getTime();
    }

    public class OnRecycleViewItemClickLitener implements DemoRecyclerAdapter.OnItemClickLitener {
        @Override
        public void onItemClick(View view, int position) {
             Toast.makeText(RecyclerviewDemo.this, position + " click",
                        Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemLongClick(View view, int position) {
            Toast.makeText(RecyclerviewDemo.this, position + "long click",
                        Toast.LENGTH_SHORT).show();
            mAdapter.removeData(position);
        }
    }
}
