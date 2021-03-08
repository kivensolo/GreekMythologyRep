package com.zeke.network.retrofit.mannager

import android.content.Context
import com.zeke.network.OkHttpClientManager
import com.zeke.network.interceptor.AddCookiesInterceptor
import com.zeke.network.interceptor.SaveCookiesInterceptor
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * author：KingZ
 * date：2019/9/25
 *
 * 使用 {@see BaseRemoteDataSource()} 代替
 */
class Api private constructor() {

    private object Helper {
        val instance = Api()
    }

    companion object {
        fun getInstance() =
            Helper.instance
        // TODO  搞成外部可以自定义设置的方式
        const val HOST = "https://www.wanandroid.com"
    }

    private lateinit var retrofitBuilder: Retrofit.Builder

    fun init(context: Context) {
        initRetrofit(context)
    }

    private fun initRetrofit(context: Context) {
        val okHttpClientManager = OkHttpClientManager.getInstance()
        okHttpClientManager.setBuilderFactory(object :
            OkHttpClientManager.ClientBuilderFactory() {
            override fun getPreInterceptors(): MutableList<Interceptor> {
                val list = ArrayList<Interceptor>()
                list.add(AddCookiesInterceptor(context))
                list.add(SaveCookiesInterceptor(context))
                return list
            }
        })

        val okHttpClient = okHttpClientManager.okHttpClient
        retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.client(okHttpClient)
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //添加回调库，采用RxJava
    }

    fun build(): Retrofit {
        return retrofitBuilder.build()
    }

    fun setClient(client: OkHttpClient?): Retrofit.Builder {
        return retrofitBuilder.client(client)
    }

    fun setBaseUrl(url: String?): Retrofit.Builder {
        return retrofitBuilder.baseUrl(url)
    }

    fun addConverterFactory(factory: Converter.Factory?): Retrofit.Builder {
        return retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
    }

    fun addCallAdapterFactory(factory: CallAdapter.Factory?): Retrofit.Builder {
        return retrofitBuilder.addCallAdapterFactory(factory)
    }

    fun registeServer(clazz: Class<*>?) {}

    /**
     * 创建由指定interface服务接口的api端点实现类
     *
     * @param service 指定的服务接口的class字节码对象
     * @param <T>     服务接口的类型
     * @return 具体实现类
    </T> */
//    public <T> T create(Class<T> service) {
//        return retrofit.create(service);
//    }
    /**
     * 使用RxJava设置数据订阅对象
     *
     * @param Single         数据观察对象  不要用Observable<T>
     * @param SingleObserver 数据观察者
     * @param <T>            T
    </T></T> */
    fun <T> setSubscribe(Single: Single<T>,
                         SingleObserver: SingleObserver<T>?) {
        Single.subscribeOn(Schedulers.io()) // 代表IO线程加载数据 通常用于网络,读写文件等io密集型的操作
//.subscribeOn(Schedulers.computation())  //代表CPU计算密集型的操作, 例如需要大量计算的操作；
//.subscribeOn(Schedulers.newThread())    //代表一个常规的新线程
                .observeOn(AndroidSchedulers.mainThread()) //代表数据结果回调到Android主线程
                .subscribe(SingleObserver!!)
    }
}