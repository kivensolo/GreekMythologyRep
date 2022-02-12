package com.kingz.module.wanandroid.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.kingz.database.entity.CollectionArticle
import com.kingz.module.wanandroid.bean.Article
import com.kingz.module.wanandroid.bean.CollectListBean
import com.zeke.kangaroo.zlog.ZLog

/**
 * author：ZekeWang
 * date：2021/3/31
 * description：收藏文章数据得ViewModel
 */
open class CollectArticleViewModel : WanAndroidViewModelV2() {
    //列表查询的中介(暂时无用)
    private val mediatorLiveData = MediatorLiveData<List<CollectionArticle?>?>()

    val userCollectArticleListLiveData: MutableLiveData<CollectListBean> by lazy {
        MutableLiveData<CollectListBean>()
    }

    /**
     * 从Room数据库中获取收藏文章列表数据
     * 若数据为空则走网络
     */
    fun getCollectArticleData() {
        ZLog.d("Try to get from cache.")
        //allCollectArticles: LiveData<List<CollectionArticle?>?>
        launchIO {
            val localCollectionData = db?.getCollectArticleDao()?.getUserAllCollectLIst()
            if (localCollectionData == null || localCollectionData.isEmpty()) {
                ZLog.e("Local cache is empty.")
                getMineCollectArticleList(0)
            } else {
                ZLog.d("Cache size is: ${localCollectionData.size}, building result...")
                val datas: MutableList<Article> = ArrayList()
                localCollectionData.forEach { localItem ->
                    val tempItem = Article().apply {
                        title = localItem.title_name
                        id = localItem.id
                    }
                    datas.add(tempItem)
                }
                val result = CollectListBean().apply {
                    this.datas = datas
                    this.size = datas.size
                    this.curPage = 1
                }
                ZLog.d("build end, post data.")
                userCollectArticleListLiveData.postValue(result)
            }
        }
    }

    /**
     * 获取登录用户自身的文章集合
     * 优先从本地缓存获取，本地没有再走网络
     * @param pageIndex: 页面index id
     */
    private suspend fun getMineCollectArticleList(pageIndex: Int) {
        ZLog.d("Request remote article data with index=$pageIndex")
        var result: CollectListBean? = null
        try {
            val articleList = remoteDataSource.getArticleList(pageIndex)
            result = articleList.data
            if (result != null) {
                inserArticleList(result?.datas)
            }
        } catch (e: Exception) {
            ZLog.e("getMineCollectArticleList on exception: ${e.printStackTrace()}")
        } finally {
            userCollectArticleListLiveData.postValue(result)
        }
    }

    /**
     * 插入数据收藏
     */
    private fun inserArticleList(data:List<Article>?){
        if (data != null) {
            ZLog.d("Has new article data, save to local cache.")
            // 一次性插入数据
            val collectArticleList:MutableList<CollectionArticle> = ArrayList()
            for (datum in data) {
                val article = CollectionArticle().apply {
                    title_id = datum.id
                    title_name = datum.title ?: ""
                    date = datum.niceDate ?: ""
                    link = datum.link ?: ""
                    score = 100.0
                }
                collectArticleList.add(article)
            }
            db?.getCollectArticleDao()?.insert(collectArticleList)
        }
    }

    fun getCollectList(owner: LifecycleOwner?, observer: Observer<List<CollectionArticle?>?>?) {
        if (owner != null && observer != null){
            mediatorLiveData.observe(owner, observer)
        }
    }
}