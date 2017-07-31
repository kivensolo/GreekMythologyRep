package com.kingz.four_components.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by KingZ on 2016/1/12.
 * Discription: ContentProvider Test
 *  应用程序之间共享数据的一种机制
 *  为存储和获取数据提供了统一的接口。ContentProvide对数据进行封装，不用关心数据存储的细节。使用表的形式来组织数据。
 *  常见的一些数据提供了默认的ContentProvider（包括音频、视频、图片和通讯录等）。
 *  总的来说使用ContentProvider对外共享数据的好处是统一了数据的访问方式
 *  标准uri:  "content://com.kingz.four_components.componentsofcontentprovider/tablename"  访问**这个应用的tablename表
 *           "content://com.kingz.four_components.componentsofcontentprovider/tablename/1" 表中id为1的数据
 *
 */
@SuppressWarnings("all")
public class MyDemoContentProvider extends ContentProvider{

    private static final int TABLE1_DIR = 0;
    private static final int TABLE1_ITEM = 1;
    private static final int TABLE2_DIR = 2;
    private static final int TABLE2_ITEM = 3;

    //TODO dir 和item真正的用途
    private static UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(uriMatcher.NO_MATCH);
        uriMatcher.addURI("com.kingz.four_components.componentsofcontentprovider","table1",TABLE1_DIR);
        uriMatcher.addURI("com.kingz.four_components.componentsofcontentprovider","table1/#",TABLE1_ITEM);
        uriMatcher.addURI("com.kingz.four_components.componentsofcontentprovider","table2",TABLE2_DIR);
        uriMatcher.addURI("com.kingz.four_components.componentsofcontentprovider","table2/#",TABLE2_ITEM);

    }
    /**
     * 初始化内容提供者的时候调用，通常会在这里完成对数据库的创建和升级等操作，
     * 返回true表示内容提供者初始化成功，false则失败。
     * ps： 只有当存在ContentResolver尝试访问我们程序中的数据的时候，被容提供者才会被初始化
     * @return
     */
    @Override
    public boolean onCreate() {
        return true;
    }

    /**
     * 从内容提供器中查询数据
     * @param uri               确定查询哪张表
     * @param projection        查询列数
     * @param selection         查询哪些行
     * @param selectionArgs     查询哪些行
     * @param sortOrder         对结果进行排序
     * @return          查询的结果放在Cursor中返回
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //对传入的uri参数进行匹配，发现URI格式成功的匹配了该uri对象就返回相应的自定义代码
        switch (uriMatcher.match(uri)){
            case TABLE1_DIR:
                //查询表一的所有数据
                break;
            case TABLE1_ITEM:
                //查询表一的单条数据
                break;
            case TABLE2_DIR:
                //查询表二的所有数据
                break;
            case TABLE2_ITEM:
                //查询表二的单条数据
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * 返回当前Url所代表数据的MIME类型
     * @param uri
     * @return  与参数相对应的MIME字符串  MIME：三部分组成
     *          a:必须以vnd开头
     *          b:如果内容URI以路径结尾，则后接android.cursor.dir/    要操作的数据属于集合类型数据
     *            如果内容URI以id结尾则接android.cursor.item/         要操作的数据属于非集合类型数据
     *          c:最后接上vnd.<authority>.<path>
     */
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case TABLE1_DIR:
                return "vnd.android.cursor.dir/vnd.com.kingz.four_components.componentsofcontentprovider.table1";
            case TABLE1_ITEM:
                return "vnd.android.cursor.item/vnd.com.kingz.four_components.componentsofcontentprovider.table1";
            case TABLE2_DIR:
                return "vnd.android.cursor.dir/vnd.com.kingz.four_components.componentsofcontentprovider.table2";
            case TABLE2_ITEM:
                return "vnd.android.cursor.item/vnd.com.kingz.four_components.componentsofcontentprovider.table2";
            default:
                break;
        }
        return null;
    }

    /**
     *
     * @param uri       茶如数据的表路径
     * @param values    保存的key
     * @return          代表新纪录的URI
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    /**
     * @param uri           索要删除数据的表
     * @param selection     删除条件（行）
     * @param selectionArgs 删除条件（行）
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
    //更新
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
