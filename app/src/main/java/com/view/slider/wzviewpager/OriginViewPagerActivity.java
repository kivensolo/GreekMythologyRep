package com.view.slider.wzviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.view.slider.wzviewpager.wzcodepagger.DotsView;
import com.kingz.customDemo.R;

import java.util.ArrayList;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/4/10 23:53 <br>
 * description: 原生的Viewpager
 *
 * 1）ViewPager类直接继承了ViewGroup类，所有它是一个容器类，
 *      可以在其中添加其他的view类
 * 2）ViewPager类需要一个PagerAdapter适配器类给它提供数据。
 *
 * 3）ViewPager经常和Fragment一起使用，并且提供了专门的
 *      FragmentPagerAdapter和FragmentStatePagerAdapter类供
 *      Fragment中的ViewPager使用。
 */
public class OriginViewPagerActivity extends Activity{

    public static final String TAG = "OriginViewPagerActivity";
    public static final int PAGE_NUMBER = 4;
    private ViewPager pager = null;
    private PagerTabStrip tabStrip = null;
    private  DotsView mDotsView;
    private ArrayList<View> viewContainter = new ArrayList<View>();
    private ArrayList<String> titleContainer = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_origin_viewpager);

        initViews();
        initAdapterData();
    }

    private void initViews() {
        pager = (ViewPager) this.findViewById(R.id.original_viewpager);
        tabStrip = (PagerTabStrip) this.findViewById(R.id.tabstrip);
        mDotsView = (DotsView) findViewById(R.id.dotsview_main);

        //取消tab下面的长横线
        tabStrip.setDrawFullUnderline(false);
        //设置tab的背景色
        tabStrip.setBackgroundColor(this.getResources().getColor(R.color.teal));
        //设置当前tab页签的下划线颜色
        tabStrip.setTabIndicatorColor(this.getResources().getColor(R.color.magenta));
        tabStrip.setTextSpacing(200);

        //设置导航点点
        mDotsView.setDotRessource(R.drawable.dot_selected, R.drawable.dot_unselected);
        mDotsView.setNumberOfPage(PAGE_NUMBER);
        mDotsView.setOnItemclickListenner(new DotsView.IItemClickedListenner() {
            @Override
            public void onItemClicked() {
                Log.i(TAG,"DotsView 被点击了！");
            }
        });

        //填充所需要包含的视图
        View view1 = LayoutInflater.from(this).inflate(R.layout.view_tab1, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.view_tab2, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.view_tab3, null);
        View view4 = LayoutInflater.from(this).inflate(R.layout.view_tab4, null);

      //viewpager开始添加view
        viewContainter.add(view1);
        viewContainter.add(view2);
        viewContainter.add(view3);
        viewContainter.add(view4);
      //页签项
        titleContainer.add("网易新闻");
        titleContainer.add("网易体育");
        titleContainer.add("网易财经");
        titleContainer.add("网易男人");
    }

    private void initAdapterData() {
        pager.setAdapter(new UserAdapter());
        pager.setOnPageChangeListener(new PageChangeListener());
    }

    class UserAdapter extends PagerAdapter {
        //viewpager中的组件数量
        @Override
        public int getCount() {
            return viewContainter.size();
        }

        //滑动切换的时候销毁当前的组件
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(viewContainter.get(position));
        }

        //每次滑动的时候生成的组件,此处返回当前每页的View
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(viewContainter.get(position));
            return viewContainter.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleContainer.get(position);
        }
    }

    class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.d(TAG, "-------scrolled arg0:" + position);
            Log.d(TAG, "-------scrolled arg1:" + positionOffset);
            Log.d(TAG, "-------scrolled arg2:" + positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
             Log.d(TAG, "------selected:" + position);
             mDotsView.selectDot(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d(TAG, "--------changed:" + state);
        }

    }
}
