package com.kingz.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.zeke.kangaroo.zlog.ZLog;

/**
 * author: King.Z <br>
 * date:  2017/7/31 22:51 <br>
 * description: SQLiteOpenHelper辅助类 <br>
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();

    public DBHelper(Context context) {
        super(context, DataBaseColumns.DATABASE_NAME, null, DataBaseColumns.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        ZLog.d(TAG, "onCreate SQLiteDatabase");
        operateTable(db, "");
        initData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ZLog.d(TAG, "onUpgrade oldVersion:" + oldVersion + ",newVersion:" + newVersion);
        switch (newVersion) {
            case DataBaseColumns.DATABASE_VERSION:
                operateTable(db, "");
                initData(db);
                break;
        }
    }

    private void initData(SQLiteDatabase db) {
        String value = "off";
        db.execSQL("insert into " + ConfigColums.TABLE_NAME + "(" + ConfigColums.CONFIG_KEY + "," + ConfigColums.CONFIG_VALUE + ") values ('log_output','" + value + "')");
        ZLog.d(TAG, "initData database version 10");
    }

    /**
     * Execute operation about creating or drop tables in traffic database.
     *
     * @param db        The database.
     * @param actionString which identifies to complete creating or drop tables. If it is
     *                     "" or null, operate creating tables. Otherwise operate drop
     *                     tables.
     */
    public void operateTable(SQLiteDatabase db, String actionString) {
        Class<DataBaseColumns>[] columnsClasses = DataBaseColumns.getSubClasses();
        DataBaseColumns columns = null;
        for (int i = 0; i < columnsClasses.length; i++) {
            try {
                columns = columnsClasses[i].newInstance();
                if (TextUtils.isEmpty(actionString)) {
                    db.execSQL(columns.getTableCreateor());
                } else {
                    db.execSQL(actionString + columns.getTableName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
