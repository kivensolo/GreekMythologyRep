package com.kingz.db;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

public class ConfigColums extends DataBaseColumns
{
	public static final String TABLE_NAME="config";
	public static final String CONFIG_KEY="key";
	public static final String CONFIG_VALUE="value";
	public static final Map<String, String> TABLE_CREATER_MAP = new HashMap<String, String>();
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected Map<String, String> getTableMap() {
		TABLE_CREATER_MAP.put(_ID,
				"integer primary key autoincrement  not null");
		TABLE_CREATER_MAP.put(CONFIG_KEY, "String");
		TABLE_CREATER_MAP.put(CONFIG_VALUE, "String");
		return TABLE_CREATER_MAP;
	}

	@Override
	public Uri getContentUri() {
		return CONTENT_URI;
	}

}
