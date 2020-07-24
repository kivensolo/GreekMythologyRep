package com.zeke.module_login.database

import android.content.Context
import com.kingz.database.dao.UserDao

/**
 * author: King.Z <br>
 * date:  2020/7/24 22:16 <br>
 * description:  <br>
 *
 *
 */
class UserDataBaseHelper {
    companion object {
        fun getUserDao(ctx: Context): UserDao? {
//           return DatabaseBuilder.getInstance(ctx).getAlbumDao()
            return null
        }
    }
}