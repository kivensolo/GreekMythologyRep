package com.kingz.four_components.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by KingZ on 2016/1/12.
 * Discription: ContentProvider Test
 *
 * 应用程序之间共享数据的一种机制,为存储和获取数据提供了统一的接口。
 * ContentProvide对数据进行封装，不用关心数据存储的细节。使用表的形式来组织数据。
 *
 * 常见的一些数据提供了默认的ContentProvider（包括音频、视频、图片和通讯录等）。
 * 总的来说使用ContentProvider对外共享数据的好处是统一了数据的访问方式
 * 标准uri:  "content://com.kingz.four_components.componentsofcontentprovider/tablename"  访问**这个应用的tablename表
 * "content://com.kingz.four_components.componentsofcontentprovider/tablename/1" 表中id为1的数据
 *
 * Story:
 * 忒提丝---海洋女神，海神涅柔斯(Nereus)和海洋女神多丽斯(Doris)的女儿，
 * 是他们的女儿之中最贤慧者。在提坦之战（Titanomachy）时曾召来百臂巨人（Hekatonchires）
 * 帮助宙斯反抗泰坦。
 */
@SuppressWarnings("all")
public class ThetisProvider extends ContentProvider {

    private static final int TABLE1_DIR = 0;
    private static final int TABLE1_ITEM = 1;
    private static final int TABLE2_DIR = 2;
    private static final int TABLE2_ITEM = 3;
    public static final String VND_ANDROID_CURSOR_DIR = "vnd.android.cursor.dir";
    public static final String VND_ANDROID_CURSOR_ITEM = "vnd.android.cursor.item";

    //TODO dir 和item真正的用途
    private static UriMatcher uriMatcher;
    public static final String AUTHORITY = "com.kingz.four_components.myprovider";
    public static final String MIME_PRE = "vnd." + AUTHORITY;

    public static final String PATH_TABLE_ONE_DIR = "table1";
    public static final String PATH_TABLE_ONE_ITEM = "table1/#";
    public static final String PATH_TABLE_TWO_DIR = "table2";
    public static final String PATH_TABLE_TWO = "table2/#";

    static {
        uriMatcher = new UriMatcher(uriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH_TABLE_ONE_DIR, TABLE1_DIR);
        uriMatcher.addURI(AUTHORITY, PATH_TABLE_ONE_ITEM, TABLE1_ITEM);
        uriMatcher.addURI(AUTHORITY, PATH_TABLE_TWO, TABLE2_DIR);
        uriMatcher.addURI(AUTHORITY, PATH_TABLE_TWO_DIR, TABLE2_ITEM);
    }

    /**
     * Provider初始化
     * 只有当存在ContentResolver尝试访问程序中数据的时候，被容提供者才会被初始化
     * @return 是否初始化成功
     */
    @Override
    public boolean onCreate() {
        return true;
    }

    /**
     * 从内容提供器中查询数据
     *
     * @param uri           确定查询哪张表
     * @param projection    查询列数
     * @param selection     查询哪些行
     * @param selectionArgs 查询哪些行
     * @param sortOrder     对结果进行排序
     * @return 查询的结果放在Cursor中返回
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //对传入的uri参数进行匹配，发现URI格式成功的匹配了该uri对象就返回相应的自定义代码
        switch (uriMatcher.match(uri)) {
            case TABLE1_DIR:
                break;
            case TABLE1_ITEM:
                break;
            case TABLE2_DIR:
                break;
            case TABLE2_ITEM:
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * 返回当前Url所代表数据的MIME类型
     *
     * @param uri
     * @return 与参数相对应的MIME字符串  MIME：三部分组成
     * a:必须以vnd开头
     * b:如果内容URI以路径结尾，则后接android.cursor.dir/    要操作的数据属于集合类型数据
     * 如果内容URI以id结尾则接android.cursor.item/         要操作的数据属于非集合类型数据
     * c:最后接上vnd.<authority>.<path>
     */
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TABLE1_DIR:
                //vnd.android.cursor.dir/vnd.com.kingz.four_components.componentsofcontentprovider.table1
                return VND_ANDROID_CURSOR_DIR + "/" + MIME_PRE + "." + PATH_TABLE_ONE_DIR;
            case TABLE1_ITEM:
                return VND_ANDROID_CURSOR_ITEM+ "/" + MIME_PRE + "." + PATH_TABLE_ONE_DIR;
            case TABLE2_DIR:
                return VND_ANDROID_CURSOR_DIR + "/" + MIME_PRE + "." + PATH_TABLE_TWO_DIR;
            case TABLE2_ITEM:
                return VND_ANDROID_CURSOR_ITEM + "/" + MIME_PRE + "." + PATH_TABLE_TWO_DIR;
            default:
                break;
        }
        return null;
    }

    /**
     * @param uri    茶如数据的表路径
     * @param values 保存的key
     * @return 代表新纪录的URI
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
