package com.kingz.posterfilm;

import android.animation.ValueAnimator;
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

import com.base.BaseActivity;
import com.google.gson.Gson;
import com.kingz.adapter.DemoRecyclerAdapter;
import com.kingz.customdemo.R;
import com.kingz.mode.RecycleDataInfo;
import com.kingz.pages.photo.filmlist.MyItemDecoration;
import com.kingz.posterfilm.data.MgResponseBean;
import com.kingz.utils.ExecutorServiceHelper;
import com.kingz.utils.OkHttpClientManager;
import com.kingz.utils.ZLog;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Request;
import okhttp3.Response;

/**
 * author: King.Z
 * date:  2016/11/13 22:23
 * description:recyclerview 的Demo展示页面
 * LayoutManager : 管理RecyclerView的结构，控制其布局显示的方式。
 * Adapter : 处理每个Item的显示.
 * ItemDecoration : 添加每个Item的装饰，控制Item间的间隔.
 * ItemAnimator :  负责添加\移除\重排序时的动画效果，控制Item增删的动画.
 * <p>
 * LayoutManager\Adapter是必须, ItemDecoration\ItemAnimator是可选.
 */
public class MusicPosterPages extends BaseActivity {

    public static final String TAG = "MusicPosterPages";
    private RecyclerView view;
    private DemoRecyclerAdapter mAdapter;
    private final String url = "http://pianku.api.mgtv.com/rider/tag-data?ticket=&device_id=173365c356c6f97b69bca90a60009d6f4b8f7c97&tagId=222&net_id=&type=3&version=5.9.501.200.3.MGTV_TVAPP.0.0_Debug&uuid=mgtvmac020000445566&platform=ott&mac_id=02-00-00-44-55-66&license=ZgOOgo5MjkyOTA4FqqoghzuqhyA7IL8NBZkgDZk7lQ0GlSAGBgYNeyC%2FtJl8vwU7DQUFjkyOTI5MZgOOgg%3D%3D&_support=00100101011&pc=200&buss_id=1000014&pn=1";

    private static final int PROTECTED_LENGTH = 51200;// 输入流保护 50KB
    private static final String DEFAULT_ENCODING = "utf-8";//编码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_demo);
        initRecyclerView();

        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<MgResponseBean>() {
            @Override
            public void onError(Request request, Exception e) {
                ZLog.d(TAG, "onError ");
                e.printStackTrace();
            }

            @Override
            public void onResponse(final Response response) {
                ZLog.d(TAG, "onResponse");
                ExecutorServiceHelper.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        String result = "";
                        final InputStream inputStream = response.body().byteStream();
                        final ByteArrayOutputStream infoStream = new ByteArrayOutputStream();

                        byte[] bcache = new byte[8 * 1024];
                        int readSize = 0;//每次读取的字节长度
                        long totalSize = 0;//总字节长度
                        try {
                            while ((readSize = inputStream.read(bcache)) > 0) {
                                totalSize += readSize;
//                        if (totalSize > PROTECTED_LENGTH) {
//                            throw new Exception("输入流超出50K大小限制");
//                        }
                                //将bcache中读取的input数据写入infoStream
                                infoStream.write(bcache, 0, readSize);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                inputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            result = infoStream.toString(DEFAULT_ENCODING);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        final String finalResult = result;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MgResponseBean responseBean = new Gson().fromJson(finalResult, MgResponseBean.class);
                                mAdapter.attachData(responseBean.getData().getHitDocs());
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });
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
//                mAdapter.addData(1);
                break;
            case R.id.action_delete:
//                mAdapter.removeData(1);
                break;
        }
        return true;
    }

    private void initRecyclerView() {
        view = (RecyclerView) findViewById(R.id.test_recycler_view);
        view.setHasFixedSize(true);        // 设置固定大小  适配器更改不能影响RecyclerView的大小
        setRecyclerLayoutManager();    // 初始化布局
        setRecyclerAdapter();
        setItemDecoration();           // 初始化装饰
        setItemAnimator();             // 初始化动画效果
        setListeners();
    }

    private void setRecyclerLayoutManager() {
        //LayoutManager--------->GridLayoutManager(网格)/LinearLayoutManager(线性)/StaggeredGridLayoutManager(错列网格)

        //默认线性(垂直方向展示  正序)
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //水平线性 反转(40-39-....-1-0)
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, true));

        //固定网格(默认垂直、正序布局) ----- 两列
        //| 0 1 |
        //| 2 3 |
        //| 4 5 |
        view.setLayoutManager(new GridLayoutManager(this, 3));

        //横向固定网格
        //| 0 2 4 ... 39|
        //| 1 3 5 ... 40|
//        recyclerView.setLayoutManager(new GridLayoutManager(this,2,LinearLayout.HORIZONTAL,false));

        // 错列网格布局 spanCount：方向垂直:列数  方向水平:行数
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
    }

    private void setRecyclerAdapter() {
        mAdapter = new DemoRecyclerAdapter();
        mAdapter.setOnItemClickLitener(new OnRecycleViewItemClickLitener());
        view.setAdapter(mAdapter);
    }

    private void setListeners() {
        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //ZLog.d(TAG,"view scrollListener ---->>> onScrollStateChanged ");
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //ZLog.d(TAG,"viewq scrollListener ---->>> onScrolled dx:" + dx + "; dy:" + dy);
                super.onScrolled(recyclerView, dx, dy);
            }
        };
        view.addOnScrollListener(scrollListener);
        view.performClick();
    }

    private void setItemDecoration() {
        //调用addItemDecoration()方法添加decoration的时候，RecyclerView在绘制的时候，会去绘制decorator，即调用该类的onDraw和onDrawOver方法，
        view.addItemDecoration(new MyItemDecoration(this, LinearLayout.HORIZONTAL));
    }

    private void setItemAnimator() {
        Animation deleteAnimation = AnimationUtils.loadAnimation(this, R.anim.push_up_in);
        deleteAnimation.setRepeatMode(ValueAnimator.RESTART);
        //deleteAnimation.setRepeatCount(0);
        //deleteAnimation.setDuration(300);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAnimation(deleteAnimation);
    }

    /**
     * Create fake data.
     *
     * @return list of fake data.
     */
    private ArrayList<RecycleDataInfo> getFakeDataSource() {
        int count = 10;
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
     *
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
            Toast.makeText(MusicPosterPages.this, position + " click",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemLongClick(View view, int position) {
            Toast.makeText(MusicPosterPages.this, position + "long click",
                    Toast.LENGTH_SHORT).show();
            mAdapter.removeData(position);
        }
    }
}
