package com.kingz.four_components.provider;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import com.kingz.four_components.ObtainConnectPeopleActivity;
import com.utils.ZLog;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2017/8/16 10:34 <br>
 * description: Content数据变化监听 <br>
 */
public class ContentChangeListener extends ContentObserver {
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public ContentChangeListener(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        ZLog.d("ContentChangeListener", "received UserInfo changed");
        ObtainConnectPeopleActivity val = new ObtainConnectPeopleActivity();
        Uri uri = Uri.parse(ObtainConnectPeopleActivity.CONTENT_URI_USER_INFO);
        Cursor mCursor = val.baseResolver.query(uri, null, null, null, null);
        //Cursor mCursor = val.baseResolver.query(uri, null, null, null, "personid desc");

        if (mCursor != null) {
            ZLog.d("ContentChangeListener", "mCursor != null");
            if (mCursor.moveToFirst()) {
                String name = mCursor.getString(mCursor.getColumnIndex("user_name"));
                String id = mCursor.getString(mCursor.getColumnIndex("user_id"));
                String token = mCursor.getString(mCursor.getColumnIndex("user_webtoken"));
                ZLog.d("ContentChangeListener", "name = " + name + ";id=" + id + ";token = " + token);
            }
            mCursor.close();
        }
    }
}
