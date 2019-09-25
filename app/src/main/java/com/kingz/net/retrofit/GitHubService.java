package com.kingz.net.retrofit;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 把网络接口统一放到一个接口类中，
 * 让Retrofit创建接口实例类来方便调用
 */
public interface GitHubService {
    @GET("users/{user}/repos")
    Observable<List<GitRepo>> listRepos(@Path("user") String user);

//    @GET("users/{user}/repos")
//    Call<List<GitRepo>> listRepos(@Path("user") String user,
//                               @Query("xxx") String parms);
}