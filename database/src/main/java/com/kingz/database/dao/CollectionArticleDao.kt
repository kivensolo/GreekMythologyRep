package com.kingz.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kingz.database.config.DBConfig
import com.kingz.database.entity.CollectionArticle

/**
 * author：ZekeWang
 * date：2021/3/31
 * description：收藏文章的DAO接口
 */
@Dao
interface CollectionArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg articles: CollectionArticle):LongArray?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articlesList: MutableList<CollectionArticle>):List<Long>?

    @Delete
    fun delete(article:CollectionArticle?):Int

    @Update
    fun update(vararg articles:CollectionArticle):Int

    //使用LiveData进行观察查询
    /**
     * 这个写法叫 Annotation use-site targets
     * https://stackoverflow.com/questions/62546062/room-using-getquery-instead-query
     *
     * https://kotlinlang.org/docs/annotations.html#annotation-use-site-targets
     */
    @get:Query("SELECT * FROM ${DBConfig.TAB_NAME_OF_COLLECT_ARTICLE}")
    val allCollectArticles: LiveData<List<CollectionArticle?>?>

    @Query("SELECT * FROM ${DBConfig.TAB_NAME_OF_COLLECT_ARTICLE}")
    fun getUserAllCollectLIst():List<CollectionArticle>

}