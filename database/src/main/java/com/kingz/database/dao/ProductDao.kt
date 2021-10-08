/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kingz.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kingz.database.config.DBConfig
import com.kingz.database.entity.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM ${DBConfig.TAB_NAME_OF_PRODUCTS}")
    fun loadAllProducts(): LiveData<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<ProductEntity>)

    @Query("select * from ${DBConfig.TAB_NAME_OF_PRODUCTS} where id = :productId")
    fun loadProduct(productId: Int): LiveData<ProductEntity>

    @Query("select * from ${DBConfig.TAB_NAME_OF_PRODUCTS} where id = :productId")
    fun loadProductSync(productId: Int): ProductEntity

    /**
     * 使用JOIN查找两个表的交集数据
     * @param query 查询文本
     * @return 查询到符合条件的数据项
     */
    @Query("SELECT products.* FROM products JOIN productsFts ON (products.id = productsFts.rowid) WHERE productsFts MATCH :query")
    fun searchAllProducts(query: String): LiveData<List<ProductEntity>>

}