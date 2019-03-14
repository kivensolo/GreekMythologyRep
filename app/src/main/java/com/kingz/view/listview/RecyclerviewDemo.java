package com.kingz.view.listview;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kingz.adapter.DemoRecyclerAdapter;
import com.kingz.customdemo.R;
import com.kingz.mode.RecycleDataInfo;
import com.kingz.pages.photo.filmlist.MyItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * author: King.Z
 * date:  2016/11/13 22:23
 * description:recyclerview 的Demo展示页面
 * LayoutManager : 管理RecyclerView的结构，控制其布局显示的方式。
 * Adapter : 处理每个Item的显示.
 * ItemDecoration : 添加每个Item的装饰，控制Item间的间隔.
 * ItemAnimator :  负责添加\移除\重排序时的动画效果，控制Item增删的动画.
 *
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
        initRecyclerView();
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

    private void initRecyclerView() {
        view = (RecyclerView) findViewById(R.id.test_recycler_view);
        view.setHasFixedSize(true);        // 设置固定大小  适配器更改不能影响RecyclerView的大小
        setRecyclerLayoutManager(view);    // 初始化布局
        setRecyclerAdapter(view);          // 初始化适配器
        setItemDecoration(view);           // 初始化装饰
        setItemAnimator(view);             // 初始化动画效果
    }

    private void setRecyclerLayoutManager(RecyclerView recyclerView) {
        //LayoutManager--------->GridLayoutManager(网格)/LinearLayoutManager(线性)/StaggeredGridLayoutManager(错列网格)

        //默认线性(垂直方向展示  正序)
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //水平线性 反转(40-39-....-1-0)
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, true));

        //固定网格(默认垂直、正序布局) ----- 两列
        //| 0 1 |
        //| 2 3 |
        //| 4 5 |
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        //横向固定网格
        //| 0 2 4 ... 39|
        //| 1 3 5 ... 40|
//        recyclerView.setLayoutManager(new GridLayoutManager(this,2,LinearLayout.HORIZONTAL,false));

        // 错列网格布局 spanCount：方向垂直:列数  方向水平:行数
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
    }

    private void setRecyclerAdapter(RecyclerView recyclerView) {
        mAdapter = new DemoRecyclerAdapter(getDataSource());
        mAdapter.setOnItemClickLitener(new OnRecycleViewItemClickLitener());
        recyclerView.setAdapter(mAdapter);
    }

    private void setItemDecoration(RecyclerView recyclerView) {
        //调用addItemDecoration()方法添加decoration的时候，RecyclerView在绘制的时候，会去绘制decorator，即调用该类的onDraw和onDrawOver方法，
        recyclerView.addItemDecoration(new MyItemDecoration(this, LinearLayout.HORIZONTAL));
    }

    private void setItemAnimator(RecyclerView recyclerView) {
        Animation deleteAnimation = AnimationUtils.loadAnimation(this, R.anim.push_up_in);
        deleteAnimation.setRepeatMode(ValueAnimator.RESTART);
        deleteAnimation.setRepeatCount(0);
        deleteAnimation.setDuration(300);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAnimation(deleteAnimation);
    }

    /**
     * Create fake data.
     * @return list of fake data.
     */
    private ArrayList<RecycleDataInfo> getDataSource() {
        int count = 40;
        ArrayList<RecycleDataInfo> data = new ArrayList<>();
        Date date = new Date();
        for (int i = 0; i < count; i++) {
            RecycleDataInfo model = new RecycleDataInfo();
            model.setDateTime(getOffsetDate(date, i));
            model.setLabel("No. " + i);
            data.add(model);
        }
        return data;
    }

    /**
     * Get the date of the specified offset days based on the current date.
     * @param date Current date
     * @param i    offset days
     * @return new date
     */
    private static Date getOffsetDate(Date date, int i) {
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
