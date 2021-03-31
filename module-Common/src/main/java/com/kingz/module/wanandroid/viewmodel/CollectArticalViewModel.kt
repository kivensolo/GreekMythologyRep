package com.kingz.module.wanandroid.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.kingz.database.AppDatabase
import com.kingz.database.entity.CollectionArtical
import com.kingz.module.wanandroid.bean.Article
import com.zeke.reactivehttp.base.BaseReactiveViewModel

/**
 * author：ZekeWang
 * date：2021/3/31
 * description：收藏文章数据得ViewModel
 *
 * //TODO 做与服务器收藏数据的定时同步
 */
open class CollectArticalViewModel(application: Application) : BaseReactiveViewModel() {
    //列表查询的中介
    private val mediatorLiveData = MediatorLiveData<List<CollectionArtical?>?>()
    private val db: AppDatabase?
    private val mContext: Context

    init {
        mContext = application
        db = AppDatabase.getInstance(mContext)
        mediatorLiveData.addSource(
            db.getCollectArticalDao().allCollectArticals)
        { collectArticalList ->
            if (db.databaseCreated.value != null) {
                mediatorLiveData.postValue(collectArticalList)
            }
        }
    }

    /**
     * 从Room数据库中获取收藏文章列表数据
     */
    fun getCollectList(owner: LifecycleOwner?, observer: Observer<List<CollectionArtical?>?>?) {
        if (owner != null && observer != null){
            mediatorLiveData.observe(owner, observer)
        }
    }

    /**
     * 插入数据收藏
     */
    fun inserArticalList(data:List<Article>){
        for (datum in data) {
            val artical = CollectionArtical().apply {
                title = datum.title ?: ""
                date = datum.niceDate ?: ""
            }
            db?.getCollectArticalDao()?.insert(artical)
        }
    }
}