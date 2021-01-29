package com.kingz.module.wanandroid.api

import com.kingz.base.BaseApiService
import com.kingz.base.response.ResponseResult
import com.kingz.module.wanandroid.bean.*
import com.kingz.module.wanandroid.response.WanAndroidResponse
import retrofit2.http.*

/**
 * Wan android recom data api.
 */
interface WanAndroidApiService : BaseApiService {

    //------------------------ 首页数据API Start----------------------------
    /**
     * 导航数据
     * @return
     */
    @GET("/navi/json")
    fun naviData(): WanAndroidResponse<List<Navi>>  // TODO 后续换成 ResponseResult

     /**
     * 首页Banner
     */
    @GET("/banner/json")
    suspend fun bannerData(): ResponseResult<MutableList<BannerItem>>?

    //------------------------ 首页数据API End----------------------------


    //------------------------ 文章信息API Start----------------------------
    /**
     * 获取文章列表
     * @param pageIndex 页码从0开始
     */
    @GET("/article/list/{pageIndex}/json")
    suspend fun requestArticles(@Path("pageIndex") pageIndex: Int)
            : WanAndroidResponse<ArticleData>

    /**
     * 置顶文章
     */
    @GET("/article/top/json")
    fun topArticles(): WanAndroidResponse<List<Article>>

     /**
     * 收藏站内文章
     */
    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id:Int): CollectBean?
    /**
     * 取消收藏站内文章
     */
    @POST("lg/uncollect/{id}/json")
    suspend fun unCcollect(@Path("id") id:Int): CollectBean?

    /**
     * 收藏文章列表
     */
    @POST("lg/collect/list/{page_idnex}/json")
    suspend fun collectList(@Path("page_idnex") id:Int): WanAndroidResponse<CollectListBean>?

    /**
     * 收藏站外文章
     */
    @GET("lg/collect/add/json")
    suspend fun collect(@Field("title") title:String,
                        @Field("author") author:String,
                        @Field("link") link:String): WanAndroidResponse<CollectBean>?
    //------------------------ 文章信息API End----------------------------


    /**
     * 搜索热词
     */
    @GET("hotkey/json")
    suspend fun getHotKey(): HotKeyBean?

    /**
     * 知识体系
     */
    @GET("tree/json")
    suspend fun getknowlegeSystem(): TreeBean?


    //------------------------ 用户信息API Start----------------------------
     /**
     * 登录
     * @param username 账号
     * @param password 密码
     * @return
     */
    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(@Field("username") username:String,
                      @Field("password") password:String)
             :WanAndroidResponse<User>
    /**
     * 退出登录
     * http://www.wanandroid.com/user/logout/json
     */
    @GET("user/logout/json")
    suspend fun logout(): WanAndroidResponse<*>?

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @param repassword 确认密码
     * @return
     */
    @POST("user/register")
    @FormUrlEncoded
    suspend fun register(@Field("username") username: String?,
        @Field("password") password: String?,
        @Query("repassword") repassword: String?
    ): WanAndroidResponse<User?>?
    //------------------------ 用户信息API End----------------------------

}