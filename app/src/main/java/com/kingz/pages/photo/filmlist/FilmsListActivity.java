
package com.kingz.pages.photo.filmlist;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import com.BaseActivity;
import com.kingz.customdemo.R;
import com.kingz.mode.PosterGroupInfo;
import com.kingz.pages.photo.adapter.FilmRecycleViewPageAdapter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/4/2 16:49
 * description: 影片列表页  用GridView和本地的图片url去显示图片
 * 网络方式使用OKHttp
 * <p>
 * 改：用RecyclerView替换GridView,实现瀑布流
 */
public class FilmsListActivity extends BaseActivity {

    public static final String TAG = "FilmsListActivity";
    private ArrayList<PosterGroupInfo> posterInfoList;
    private PosterGroupInfo posterGroupInfo;
    private ArrayList<PosterGroupInfo.Poster> posterList = new ArrayList<>();
    private PosterGroupInfo.Poster posterInfo;
    private RecyclerView mRecyclerView;
    private FilmRecycleViewPageAdapter mFilmRecycleViewPageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLocalPosterData();

//        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(-1, -1);
//        GridView rootView = new GridView(this);
//        rootView.setColumnWidth(200);
//        rootView.setVerticalSpacing(20);
//        rootView.setHorizontalSpacing(20);
//        rootView.setNumColumns(GridView.AUTO_FIT);
//        rootView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
//        rootView.setLayoutParams(lps);
//        rootView.setBackgroundColor(Color.WHITE);
//        rootView.setAdapter(new FilmListGridViewAdapter(this, posterList));
//        setContentView(rootView);

        setContentView(R.layout.activity_recyclerview_demo);
        mRecyclerView = (RecyclerView) findViewById(R.id.test_recycler_view);
        initRecyclerView(mRecyclerView);

    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);         // 设置固定大小
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
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
    }

    private void initRecyclerAdapter(RecyclerView recyclerView) {
        mFilmRecycleViewPageAdapter = new FilmRecycleViewPageAdapter(posterList);
        mFilmRecycleViewPageAdapter.setOnItemClickLinstener(new OnRecycleViewItemClickLitener());
        recyclerView.setAdapter(mFilmRecycleViewPageAdapter);
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

    public class OnRecycleViewItemClickLitener implements FilmRecycleViewPageAdapter.OnItemClickListener {
        @Override
        public void onItemClick(View view, int position) {
            Toast.makeText(FilmsListActivity.this, position + " click", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemLongClick(View view, int position) {
            Toast.makeText(FilmsListActivity.this, position + "long click", Toast.LENGTH_SHORT).show();
//            mAdapter.removeData(position);
        }
    }

    /**
     * 获取本地海报xml数据
     */
    private void getLocalPosterData() {
        try {
            InputStream filmInfoStream = getResources().getAssets().open("posterimage.xml");
            XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
            pullParser.setInput(filmInfoStream, "utf-8");
            int evtType = pullParser.getEventType();

            boolean isInposter = false;
            posterInfoList = new ArrayList<>();

            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        String tagValue = pullParser.getName();
                        if ("poster".equals(tagValue)) {
                            posterInfo = new PosterGroupInfo().new Poster();
                            isInposter = true;
                        } else if ("group".equals(tagValue)) {
                            posterGroupInfo = new PosterGroupInfo();
                        } else if ("id".equals(tagValue)) {
                            posterGroupInfo.id = pullParser.nextText();
                        } else if ("img".equals(tagValue)) {
                            posterInfo.poster_ur = pullParser.nextText();
                        } else if ("title".equals(tagValue)) {
                            if (isInposter) {
                                posterInfo.title = pullParser.nextText();
                            } else {
                                posterGroupInfo.title = pullParser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = pullParser.getName();
                        if ("group".equals(endTag)) {
                            posterInfoList.add(posterGroupInfo);
                            posterGroupInfo = null;
                        } else if ("poster".equals(endTag)) {
                            posterList.add(posterInfo);
                            posterInfo = null;
                            isInposter = false;
                        }
                        break;
                    default:
                        break;
                }
                evtType = pullParser.next();
            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }

}
