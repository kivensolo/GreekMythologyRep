package com.kingz.four_components;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KingZ on 2016/1/13.
 * Discription:获取系统的内容提供数据
 */
public class ObtainConnectPeopleActivity extends Activity{

    private ArrayAdapter<String> adapter = null;
    private List<String> contactaList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(-1,-1);
        root.setLayoutParams(lps);
        setContentView(root);

        ListView contactsView = new ListView(this);
        root.addView(contactsView);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contactaList);
        contactsView.setAdapter(adapter);
        readContacts();
    }

    /**
     * 获取系统联系人
     */
    private void readContacts() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,null,null,null);
            while( null != cursor){
    //            String personName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String personName = cursor.getString(cursor.getColumnIndex("display_name"));
                String personNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactaList.add(personName+"\n"+personNum);
            }
            if(null == cursor){
                Toast.makeText(this,"未找到联系人",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }
}
