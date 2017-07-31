package com.kingz.db;

/**
 * 数据库适配器
 */
public class DataBaseAdapter {
    /**
     * 数据库表适配器
     */
    public static final DataBaseColumns[] mDataBaseColumns = {
            new ConfigColums(),
    };

    public static final String[] mContentType = {
            "vnd.android.cursor.dir/vnd.com.starcor.hunan.config",
    };
}
