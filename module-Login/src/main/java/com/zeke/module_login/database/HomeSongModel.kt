package com.zeke.module_login.database

import com.kingz.database.AbsDatabaseModel
import com.kingz.database.DatabaseApplication
import com.kingz.database.entity.BaseEntity

class UserDataBaseModel<E : BaseEntity> : AbsDatabaseModel<E>() {

    private val userDao = DatabaseApplication.getInstance().getUserDao()
}