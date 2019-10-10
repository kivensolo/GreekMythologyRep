package com.kingz.posterfilm;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import com.base.BaseActivity;
import com.kingz.adapter.MgPosterAdapter;
import com.kingz.customdemo.R;
import com.kingz.mode.RecycleDataInfo;
import com.kingz.net.OkHttpClientManager;
import com.kingz.net.retrofit.GitRepo;
import com.kingz.net.retrofit.RetrofitServiceManager;
import com.kingz.pages.photo.filmlist.MyItemDecoration;
import com.kingz.posterfilm.data.MgResponseBean;
import com.kingz.utils.ZLog;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Request;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * author: King.Z
 * date:  2016/11/13 22:23
 * description:recyclerview 的Demo展示页面
 *      LayoutManager : 管理RecyclerView的结构，控制其布局显示的方式。
 *      Adapter : 处理每个Item的显示.
 *      ItemDecoration : 添加每个Item的装饰，控制Item间的间隔.
 *      ItemAnimator :  负责添加\移除\重排序时的动画效果，控制Item增删的动画.
 * <p>
 *      LayoutManager\Adapter是必须, ItemDecoration\ItemAnimator是可选.
 */
public class MusicPosterPages extends BaseActivity {

    public static final String TAG = MusicPosterPages.class.getSimpleName();
    private RecyclerView view;
    private MgPosterAdapter mAdapter;
    private final String url = "http://pianku.api.mgtv.com/rider/tag-data?ticket=&device_id=173365c356c6f97b69bca90a60009d6f4b8f7c97&tagId=222&net_id=&type=3&version=5.9.501.200.3.MGTV_TVAPP.0.0_Debug&uuid=mgtvmac020000445566&platform=ott&mac_id=02-00-00-44-55-66&license=ZgOOgo5MjkyOTA4FqqoghzuqhyA7IL8NBZkgDZk7lQ0GlSAGBgYNeyC%2FtJl8vwU7DQUFjkyOTI5MZgOOgg%3D%3D&_support=00100101011&pc=200&buss_id=1000014&pn=1";


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
            public void onResponse(final MgResponseBean response) {
                ZLog.d(TAG, "onResponse");
                mAdapter.attachData(response.getData().getHitDocs());
                mAdapter.notifyDataSetChanged();
            }
        });

        RetrofitServiceManager.listRepos("kivensolo", new Observer<List<GitRepo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<GitRepo> gitRepos) {
                ZLog.d("onNext");
                if(gitRepos == null) return;
                String repoName = gitRepos.get(0).getName();
                ZLog.d("first repo name :" + repoName);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                ZLog.d("onComplete");
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
        setRecyclerLayoutManager();
        setRecyclerAdapter();
        setItemDecoration();
        setItemAnimator();
        setScrollListeners();
    }

    private void setRecyclerLayoutManager() {
        view.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void setRecyclerAdapter() {
        mAdapter = new MgPosterAdapter(getBaseContext());
        view.setAdapter(mAdapter);
    }

    private void setScrollListeners() {
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
}
