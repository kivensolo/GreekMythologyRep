package com.kingz.module.common.service

import com.zeke.network.retrofit.model.WeatherEntity
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * author: King.Z <br></br>
 * date:  2020/3/4 20:31 <br></br>
 * description: 查询天气的接口 <br></br>
 */
interface WeatherService {
    //    @GET("weather_mini")
    //      单独用retrofit2返回call对象
    //Call<WeatherEntity> getWeather(@Query("city") String city);
    // 使用rxjava
    @GET("weather_mini")
    fun getWeather(@Query(
            "city") city: String?): Observable<WeatherEntity?>? // 相当于 "/weather_mini？city=$city"
}