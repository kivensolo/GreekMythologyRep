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

import androidx.room.*
import com.kingz.database.model.Comment
import java.util.*

@Entity(tableName = "comments",
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = ["id"],
        childColumns = ["productId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["productId"])]
)
class CommentEntity : Comment {
    @PrimaryKey(autoGenerate = true)
    private var id = 0
    private var productId = 0
    private var text: String? = null
    private var postedAt: Date? = null
    override fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    override fun getProductId(): Int {
        return productId
    }

    fun setProductId(productId: Int) {
        this.productId = productId
    }

    override fun getText(): String {
        return text!!
    }

    fun setText(text: String?) {
        this.text = text
    }

    override fun getPostedAt(): Date {
        return postedAt!!
    }

    fun setPostedAt(postedAt: Date?) {
        this.postedAt = postedAt
    }

    constructor() {}

    @Ignore
    constructor(id: Int, productId: Int, text: String?, postedAt: Date?) {
        this.id = id
        this.productId = productId
        this.text = text
        this.postedAt = postedAt
    }
}