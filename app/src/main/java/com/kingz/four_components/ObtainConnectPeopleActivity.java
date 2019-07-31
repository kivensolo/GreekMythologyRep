package com.kingz.four_components;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.base.BaseActivity;
import com.kingz.customdemo.R;
import com.kingz.four_components.provider.ContentChangeListener;
import com.kingz.utils.ZLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KingZ on 2016/1/13.
 * Discription:获取系统的内容提供数据
 */
public class ObtainConnectPeopleActivity extends BaseActivity {

    public static final String CONTENT_URI_USER_INFO = "content://com.starcor.xinjiang.system.provider.kork.userinfo/query";
    public static final String CONTENT_URI_COMMON_INFO = "content://com.starcor.xinjiang.system.provider.kork.commoninfo/query";
    private ArrayAdapter<String> adapter = null;
    private List<String> contactaList;
    ContentChangeListener contentChangeListener;
    public ContentResolver baseResolver;
    private Button queryBtn;
    private Button deleteBtn;
    private Button insertBtn;
    private Button updateBtn;
    private TextView contentView;

    private Cursor cursor = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactaList = new ArrayList<>();
        initViews();
        //注册监听
        contentChangeListener = new ContentChangeListener(new Handler(),this);
        getContentResolver().registerContentObserver(Uri.parse(CONTENT_URI_USER_INFO), true, contentChangeListener);
        readContacts();
    }

    private void initViews() {
        setContentView(R.layout.relativelayout_page);

        queryBtn = (Button) findViewById(R.id.query_btn);
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse(CONTENT_URI_USER_INFO);
                    Cursor mCursor = baseResolver.query(uri, null, null, null, null);
                    if (mCursor != null) {
                        if (mCursor.moveToFirst()) {
                            String name = mCursor.getString(mCursor.getColumnIndex("user_name"));
                            String id = mCursor.getString(mCursor.getColumnIndex("user_id"));
                            String token = mCursor.getString(mCursor.getColumnIndex("user_webtoken"));
                            ZLog.d("Test", "name = " + name + ";id=" + id + ";token = " + token);
                        }
                        mCursor.close();
                    }

                    Uri ur2 = Uri.parse(CONTENT_URI_COMMON_INFO);
                    //uri.withAppendedPath(uri, "");
                    Cursor c2 = baseResolver.query(ur2, null, null, null, null);
                    if (c2 != null) {
                        if (c2.moveToFirst()) {
                            String appName = c2.getString(c2.getColumnIndex("app_name"));
                            String version = c2.getString(c2.getColumnIndex("app_version"));
                            String mac = c2.getString(c2.getColumnIndex("device_mac"));
                            ZLog.d("Test", "appName=" + appName + ";  version=" + version + ";  mac=" + mac);
                        }
                        c2.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });

        deleteBtn = (Button) findViewById(R.id.puykork_id);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] value =  {"1001005","bestv","Umai:PROG/3101145@BESTV.SMG.SMG","show_product_purchase"};
                Intent intent = new Intent();
                intent.setClassName("com.starcor.xinjiang", "com.starcor.hunan.ThirdInvokePurchaseActivity");
                intent.putExtra("product_id", value[0]);
                intent.putExtra("cp_id", value[1]);
                intent.putExtra("import_id", value[2]);
                intent.putExtra("cmd_ex", value[3]);
                startActivity(intent);
            }
        });

        insertBtn = (Button) findViewById(R.id.show_intent);
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Uri uri = Uri.parse("scheme://authority/path1/path2?query1&query2&query3#fragment");
                intent.setDataAndType(uri,"application/vnd.android.package-archive");
                intent.setPackage("com.kingz.niubi");
                intent.setSourceBounds(new Rect(110,110,1080,1080));
            }
        });

        updateBtn = new Button(this);

        contentView = (TextView) findViewById(R.id.intent_content);
        //ListView contactsView = new ListView(this);
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactaList);
        //contactsView.setAdapter(adapter);
    }

    /**
     * 获取系统联系人
     */
    private void readContacts() {
        try {
            if (baseResolver == null) {
                baseResolver = getContentResolver();
            }
            cursor = baseResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            //while (null != cursor) {
            //String personName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //String personNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //contactaList.add(personName + "\n" + personNum);
            contactaList.add("Test" + "\n" + "Fire");
            //}
            //if (null == cursor) {
            //    ToastTools.getInstance().showToast(this, "未找到联系人");
            //}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (contentChangeListener != null) {
            getContentResolver().unregisterContentObserver(contentChangeListener);
        }
    }
}
