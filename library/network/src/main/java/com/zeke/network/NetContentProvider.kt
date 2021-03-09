package com.zeke.network

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/**
 * 通过ContentProvider 进行init
 */
class NetContentProvider : ContentProvider() {

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(
            uri: Uri,
            projection: Array<out String>?,
            selection: String?,
            selectionArgs: Array<out String>?,
            sortOrder: String?
    ): Cursor? = null

    override fun update(
            uri: Uri,
            values: ContentValues?,
            selection: String?,
            selectionArgs: Array<out String>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun getType(uri: Uri): String? = ""

    override fun onCreate(): Boolean {
//        context?.run {
//           Api.getInstance().init(applicationContext)
//        }
        return true
    }
}