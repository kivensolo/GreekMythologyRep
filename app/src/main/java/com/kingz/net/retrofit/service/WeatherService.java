package com.kingz.net.retrofit.service;

import com.kingz.net.model.WeatherEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * author: King.Z <br>
 * date:  2020/3/4 20:31 <br>
 * description: 查询天气的接口 <br>
 */
public interface WeatherService {
//    @GET("weather_mini")
//      单独用retrofit2返回call对象
    //Call<WeatherEntity> getWeather(@Query("city") String city);
    // 使用rxjava
    @GET("weather_mini")
    Observable<WeatherEntity> getWeather(@Query("city") String city);
    // 相当于 "/weather_mini？city=$city"
}
