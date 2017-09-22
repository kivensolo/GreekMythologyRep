package com.kingz.four_components.provider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/15 22:29
 * description: BaseProvider
 *   ContentProvider的接口函数无法直接调用，可通过ContentResolver对象来通过URI间接调用ContentProvider。
 *
 *     content://<authority>/<data_path>/<id>
 *
 *     Uri指定了将要操作的ContentProvider，其实可以把一个Uri看作是一个网址，我们把Uri分为三部分。
        第一部分是"content://"。可以看作是网址中的"http://"。
        第二部分是主机名或authority，用于唯一标识这个ContentProvider，外部应用需要根据这个标识来找到它。可以看作是网址中的主机名，比如"blog.csdn.net"。
             因此一般< authority >都由类的小写全称组成，以保证唯一性< data_path > 是数据路径，用来确定请求的是哪个数据集。
             如果ContentProvider近提供一个数据集，数据路径则可以省略；如果ContentProvider提供多个数据集，
             数据路径必须指明具体数据集。数据集的数据路径可以写成多段格式，例如people/girl和people/boy。

        第三部分是路径名，用来表示将要操作的数据。可以看作网址中细分的内容路径
 */
@SuppressLint("Registered")
public class BaseProvider extends ContentProvider{

    @Override
    public boolean onCreate() {
        return false;
    }

    /**
     * 用于查询指定Uri的ContentProvider，返回一个Cursor
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    /**
     * 用来返回指定URI的MIME数据类型，若URI是单条数据，则返回的MIME数据类型以vnd.android.cursor.item/开头；
     * 若URI是多条数据属于集合类型，则返回的MIME数据类型以vnd.android.cursor.dir/开头。
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * 用于添加数据到指定Uri的ContentProvider中
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    /**
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * 用于更新指定Uri的ContentProvider中的数据
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
