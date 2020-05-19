package com.zeke.network.retrofit.mannager;

import com.zeke.network.OkHttpClientManager;

import java.util.HashMap;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author：KingZ
 * date：2019/9/25
 * description：GitHub Retrofit接口实例的管理类测试
 */
public class ApiManager {
//    private static final String REQUEST_PATH = "http://wthrcdn.etouch.cn";
    private static final String REQUEST_PATH = "https://api.github.com";
    private final Retrofit retrofit;
//    private static WeatherService weatherService = ApiManager.i().create(WeatherService.class);
    private HashMap<Class, Retrofit> mServiceHashMap = new HashMap<>();

    // /**
    //  * 获取github用户仓库列表
    //  *
    //  * @param city     用户名
    //  * @param observer Observer回调监听对象
    //  */
//    public static void getWeater(String city, Observer<WeatherEntity> observer) {
//        setSubscribe(weatherService.getWeather(city), observer);
//    }

//     /**
//      * 获取github用户仓库列表
//      * @param username 用户名
//      * @param observer Observer回调监听对象
//      */
//    public static void listReposByUser(String username, Observer<List<GitHubRepo>> observer) {
//        setSubscribe(movieService.listRepos(username), observer);
//    }

    private ApiManager() {
        OkHttpClient okHttpClient = OkHttpClientManager.getInstance().getOkHttpClient();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)           //设置使用okhttp网络请求
                .baseUrl(REQUEST_PATH)          //设置服务器路径
                .addConverterFactory(GsonConverterFactory.create())//添加Gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加回调库，采用RxJava
                .build();
    }

    public void registeServer(Class clazz) {
        mServiceHashMap.put(clazz, retrofit);
    }

    private static class SingletonHolder {
        private static final ApiManager INSTANCE = new ApiManager();
    }

    /*
     * 获取RetrofitServiceManager
     **/
    public static ApiManager i() {
        return SingletonHolder.INSTANCE;
    }

    public <T> T setApi(Class<T> clz) {
        Retrofit retrofit = mServiceHashMap.get(clz);
        if (retrofit != null) {
            return retrofit.create(clz);
        } else {
            return null;
        }
    }

    /**
     * 创建由指定interface服务接口的api端点实现类
     *
     * @param service 指定的服务接口的class字节码对象
     * @param <T>     服务接口的类型
     * @return 具体实现类
     */
//    public <T> T create(Class<T> service) {
//        return retrofit.create(service);
//    }

    /**
     * 使用RxJava设置数据订阅对象
     *
     * @param Single     数据观察对象  不要用Observable<T>
     * @param SingleObserver   数据观察者
     * @param <T>        T
     */
    public <T> void setSubscribe(Single<T> Single,
                                 SingleObserver<T> SingleObserver) {
        Single.subscribeOn(Schedulers.io())           // 代表IO线程加载数据 通常用于网络,读写文件等io密集型的操作
                //.subscribeOn(Schedulers.computation())  //代表CPU计算密集型的操作, 例如需要大量计算的操作；
                //.subscribeOn(Schedulers.newThread())    //代表一个常规的新线程
                .observeOn(AndroidSchedulers.mainThread())//代表数据结果回调到Android主线程
                .subscribe(SingleObserver);
    }
}
