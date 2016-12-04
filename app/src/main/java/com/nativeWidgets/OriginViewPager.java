package com.nativeWidgets;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kingz.animation.ViewpageTransformerAnimation;
import com.kingz.customdemo.R;
import com.utils.BitMapUtils;
import com.view.slider.wzviewpager.wzcodepagger.DotsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/4/10 23:53 <br>
 * description: 原生的Viewpager
 *
 * 1）ViewPager类直接继承了ViewGroup类，所有它是一个容器类，可以在其中添加其他的view类
 * 2）ViewPager类需要一个PagerAdapter适配器类给它提供数据。
 *
 * 3）ViewPager经常和Fragment一起使用，并且提供了专门的
 *      FragmentPagerAdapter和FragmentStatePagerAdapter类供
 *      Fragment中的ViewPager使用。
 */
public class OriginViewPager extends Activity{

    public static final String TAG = "OriginViewPager";
    public static final int PAGE_NUMBER = 4;
    private ViewPager pager = null;
    private  DotsView mDotsView;
    private List<ImageView> imageViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_origin_viewpager);
        initViews();
    }

    private void initViews() {
        initDataSrc();
        initViewPager();
        initPagerTabStrip();
        initDotsView();
    }

    private void initDataSrc() {
        ImageView first=new ImageView(this);
        first.setImageBitmap(BitMapUtils.setInvertedBitmapById(this,R.drawable.bg,0));
        first.setTag("阳光");
        ImageView second=new ImageView(this);
        second.setImageBitmap(BitMapUtils.setInvertedBitmapById(this,R.drawable.comic_1,0));
        second.setTag("动漫");
        ImageView third=new ImageView(this);
        third.setImageBitmap(BitMapUtils.setInvertedBitmapById(this,R.drawable.cloud,0));
        third.setTag("卷云");
        ImageView fourth=new ImageView(this);
        fourth.setImageBitmap(BitMapUtils.setInvertedBitmapById(this,R.drawable.kingofliu,0));
        fourth.setTag("刘德华");
        ImageView fifth=new ImageView(this);
        fifth.setImageBitmap(BitMapUtils.setInvertedBitmapById(this,R.drawable.ironman_1,0));
        fifth.setTag("钢铁侠");
        imageViewList.add(first);
        imageViewList.add(second);
        imageViewList.add(third);
        imageViewList.add(fourth);
        imageViewList.add(fifth);
    }

    private void initDotsView() {
        mDotsView = (DotsView) findViewById(R.id.dotsview_main);
        mDotsView.setDotRessource(R.drawable.dot_selected, R.drawable.dot_unselected);
        mDotsView.setNumberOfPage(PAGE_NUMBER);
        mDotsView.setOnItemclickListenner(new DotsView.IItemClickedListenner() {
            @Override
            public void onItemClicked() {
            }
        });
    }

    private void initPagerTabStrip() {
        PagerTabStrip tabStrip = (PagerTabStrip) this.findViewById(R.id.tabstrip);
        tabStrip.setDrawFullUnderline(false);                                                 //取消tab下面的长横线
        tabStrip.setBackgroundColor(this.getResources().getColor(R.color.teal));              //设置tab的背景色
        tabStrip.setTabIndicatorColor(this.getResources().getColor(R.color.magenta));        //设置当前tab页签的下划线颜色
        tabStrip.setTextSpacing(200);
    }

    private void initViewPager() {
        pager = (ViewPager) this.findViewById(R.id.original_viewpager);
        pager.setPageTransformer(true, new ViewpageTransformerAnimation());
        pager.setOffscreenPageLimit(3);
        int pagerWidth= (int) (getResources().getDisplayMetrics().widthPixels*3.0f/5.0f);
        ViewGroup.LayoutParams lp = pager.getLayoutParams();
        if (lp==null){
            lp=new ViewGroup.LayoutParams(pagerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        }else {
            lp.width=pagerWidth;
        }
        pager.setLayoutParams(lp);
        pager.setPageMargin(-30);
        findViewById(R.id.viewpager_main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return pager.dispatchTouchEvent(motionEvent);
            }
        });

        pager.setAdapter(new UserAdapter());
        pager.setOnPageChangeListener(new PageChangeListener());
    }

    private void initAdapterData() {

    }

    class UserAdapter extends PagerAdapter {
        //viewpager中的组件数量
        @Override
        public int getCount() {
            return imageViewList.size();
        }

        //滑动切换的时候销毁当前的组件
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView(imageViewList.get(position));
        }

        //每次滑动的时候生成的组件,此处返回当前每页的View
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            (container).addView(imageViewList.get(position),position);
            return imageViewList.get(position);
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
            return (String)imageViewList.get(position).getTag();
        }
    }

    class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.d(TAG, "-------scrolled arg0:" + position);
//            Log.d(TAG, "-------scrolled arg1:" + positionOffset);
//            Log.d(TAG, "-------scrolled arg2:" + positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
//             Log.d(TAG, "------selected:" + position);
             mDotsView.selectDot(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            Log.d(TAG, "--------changed:" + state);
        }

    }
}
