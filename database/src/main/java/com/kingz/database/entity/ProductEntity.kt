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
package com.kingz.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.kingz.database.config.DBConfig

@Entity(tableName = DBConfig.TAB_NAME_OF_PRODUCTS)
class ProductEntity {
    @PrimaryKey
    var id = 0
    var name: String? = null
    var description: String? = null
    var price = 0

    /**
     * 内嵌对象
     */
    @Embedded
    var info:Info? = null

    constructor()

    @Ignore
    constructor(id: Int, name: String?, description: String?, price: Int) {
        this.id = id
        this.name = name
        this.description = description
        this.price = price
    }

    constructor(product: ProductEntity) {
        id = product.id
        name = product.name
        description = product.description
        price = product.price
    }
}