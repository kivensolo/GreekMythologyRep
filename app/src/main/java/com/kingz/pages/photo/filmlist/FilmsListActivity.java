
package com.kingz.pages.photo.filmlist;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kingz.adapter.FilmRecycleViewPageAdapter;
import com.kingz.customdemo.R;
import com.kingz.mode.PosterGroupInfo;
import com.kingz.module.common.BaseActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * author: King.Z
 * date: 2016 2016/4/2 16:49
 * description: 影片列表页
 * FIXME 数据还有显示问题 需修正
 */
public class FilmsListActivity extends BaseActivity {

    public static final String TAG = "FilmsListActivity";
    private ArrayList<PosterGroupInfo> posterInfoList;
    private PosterGroupInfo posterGroupInfo;
    private ArrayList<PosterGroupInfo.Poster> posterList = new ArrayList<>();
    private PosterGroupInfo.Poster posterInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_demo);
        getLocalPosterData();
        initRecyclerView();

    }

    private void initRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.test_recycler_view);
        mRecyclerView.setHasFixedSize(true);         // 设置固定大小
        initRecyclerLayoutManager(mRecyclerView);    // 初始化布局
        initRecyclerAdapter(mRecyclerView);          // 初始化适配器
        initItemDecoration(mRecyclerView);           // 初始化装饰
        initItemAnimator(mRecyclerView);             // 初始化动画效果
    }

    private void initRecyclerLayoutManager(RecyclerView recyclerView) {
        //LayoutManager--------->GridLayoutManager(网格)/LinearLayoutManager(线性)/StaggeredGridLayoutManager(错列网格)
        // 错列网格布局 spanCount：Span简单的定义了一个某一维度上堆栈起来的item数
        // 维度垂直即为列数  维度水平即为行数
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this,
                5,
                GridLayoutManager.VERTICAL,
                false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 7 == 0 ? 2 : 5);
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
    }

    private void initRecyclerAdapter(RecyclerView recyclerView) {
        FilmRecycleViewPageAdapter mFilmRecycleViewPageAdapter = new FilmRecycleViewPageAdapter(posterList);
        recyclerView.setAdapter(mFilmRecycleViewPageAdapter);
    }

    private void initItemDecoration(RecyclerView recyclerView) {
        //调用addItemDecoration()方法添加decoration的时候，RecyclerView在绘制的时候，会去绘制decorator，即调用该类的onDraw和onDrawOver方法，
        recyclerView.addItemDecoration(new MyItemDecoration(this, LinearLayout.VERTICAL));
    }

    private void initItemAnimator(RecyclerView recyclerView) {
        Animation deleteAnimation = AnimationUtils.loadAnimation(this, R.anim.push_up_in);
        deleteAnimation.setRepeatMode(ValueAnimator.RESTART);
        deleteAnimation.setRepeatCount(0);
        deleteAnimation.setDuration(300);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAnimation(deleteAnimation);
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
