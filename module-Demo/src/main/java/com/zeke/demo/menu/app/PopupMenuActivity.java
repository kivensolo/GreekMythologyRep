package com.zeke.demo.menu.app;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.zeke.demo.R;


public class PopupMenuActivity extends AppCompatActivity {

    private static final String TAG = "PopupMenuActivity";

    private Button btnOpenPopupMenu;

    public static void actionStart(Context context){
        Intent intent = new Intent(context,PopupMenuActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_menu);

        initView();
        initData();
        initEvent();


    }

    /**
     * 初始化视图
     */
    private void initView() {
        btnOpenPopupMenu = findViewById(R.id.btn_open_PopupMenu);

    }

    /**
     * 初始化数据
     */
    private void initData() {
    }

    /**
     * 初始化事件
     */
    private void initEvent() {

        btnOpenPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupMenu(v);
            }
        });
    }

    private void createPopupMenu(View view){
        PopupMenu popupMenu = new PopupMenu(this,view);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            //写法1：getMenuInflater().inflate(R.menu.context_menu,popupMenu.getMenu());
            popupMenu.getMenuInflater().inflate(R.menu.context_menu,popupMenu.getMenu());
        }else {
            //在 API 级别 14 及更高版本中，您可以将两行合并在一起，使用 PopupMenu.inflate() 扩充菜单。
            popupMenu.inflate(R.menu.context_menu);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.context_menu_add) {
                    Toast.makeText(PopupMenuActivity.this, "点击保存菜单", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.context_menu_del) {
                    Toast.makeText(PopupMenuActivity.this, "点击删除菜单", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.context_menu_save) {
                    Toast.makeText(PopupMenuActivity.this, "点击保存菜单", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }


}
