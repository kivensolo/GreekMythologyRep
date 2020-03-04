package com.kingz.net.retrofit.service;

import com.kingz.net.model.GitHubRepo;
import com.kingz.net.model.GitHubUserInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 定义一个接口配置网络请求;
 * 把网络接口统一放到一个接口类中，
 * 让Retrofit创建接口实例类来方便调用
 */
public interface GitHubService {
    @GET("users/{user}")
    Observable<GitHubUserInfo> getUserInfo(@Path("user") String user);

    @GET("users/{user}/repos")
    Observable<List<GitHubRepo>> listRepos(@Path("user") String user);
}