package com.kingz.module.wanandroid.repository

import androidx.lifecycle.LiveData
import com.kingz.database.AppDatabase
import com.kingz.database.entity.ProductEntity

/**
 * author：ZekeWang
 * date：2021/10/8
 * description：本地数据源
 */
class LocalDataSource(private val sInstance: AppDatabase) {
    companion object {
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        @JvmStatic
        fun getInstance(sInstance: AppDatabase): LocalDataSource {
            if (INSTANCE == null) {
                synchronized(LocalDataSource::class) {
                    INSTANCE = LocalDataSource(sInstance)
                }
            }
            return INSTANCE!!
        }
    }

    /**
     * 获取指定产品id的产品信息
     */
    fun loadProduct(productId: Int): LiveData<ProductEntity>? {
        return sInstance.productDao().loadProduct(productId)
    }
}