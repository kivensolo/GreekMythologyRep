package com.kingz.net.retrofit;

import com.kingz.net.OkHttpClientManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author：KingZ
 * date：2019/9/25
 * description：Retrofit接口实例的管理类测试
 */
public class RetrofitServiceManager {
    private static final String REQUEST_PATH = "https://api.github.com/";
    private final Retrofit retrofit;

    private RetrofitServiceManager() {
        OkHttpClient okHttpClient = OkHttpClientManager.getInstance().getOkHttpClient();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)//设置使用okhttp网络请求
                .baseUrl(REQUEST_PATH)//设置服务器路径
                .addConverterFactory(GsonConverterFactory.create())//添加转化库，默认是Gson
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加回调库，采用RxJava
                .build();
    }

    private static class SingletonHolder {
        private static final RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
    }

    /*
    * 获取RetrofitServiceManager
    **/
    public static RetrofitServiceManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }



    private static GitHubService movieService = RetrofitServiceManager.getInstance().create(GitHubService.class);

    public static void listRepos(String username, Observer<List<GitRepo>> observer) {
        setSubscribe(movieService.listRepos(username), observer);
    }

    private static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }
}
