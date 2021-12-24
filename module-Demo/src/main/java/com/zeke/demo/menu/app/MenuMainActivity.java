package com.zeke.demo.menu.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.zeke.demo.R;
import com.zeke.demo.menu.fragmet.BaseFragmet;
import com.zeke.demo.menu.fragmet.OptionMenuFragment;

public class MenuMainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout lllContianer;
    private FrameLayout flContianer;

    private Button btnOptionMenu01;
    private Button btnOptionMenu02;
    private Button btnContextMenu;
    private Button btnListViewContextMode;
    private Button btnRecycleViewContextMode;
    private Button btnSingleView;
    private Button btnPopupMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        initView();         //初始化视图
        initData();         //初始化数据
        initEvent();        //初始化事件
    }

    /**
     * 初始化视图
     */
    private void initView() {
        lllContianer = findViewById(R.id.ll_contianer);
        flContianer = findViewById(R.id.fragment_contianer);

        btnOptionMenu01 = findViewById(R.id.btn_OptionMenu01);
        btnOptionMenu02 = findViewById(R.id.btn_OptionMenu02);
        btnContextMenu = findViewById(R.id.btn_ContextMenu);
        btnListViewContextMode = findViewById(R.id.btn_ListView_ContextMode);
        btnRecycleViewContextMode = findViewById(R.id.btn_RecycleView_ContextMode);
        btnSingleView = findViewById(R.id.btn_single_view);
        btnPopupMenu = findViewById(R.id.btn_PopupMenu);
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
        btnOptionMenu01.setOnClickListener(this);
        btnOptionMenu02.setOnClickListener(this);
        btnContextMenu.setOnClickListener(this);
        btnListViewContextMode.setOnClickListener(this);
        btnRecycleViewContextMode.setOnClickListener(this);
        btnSingleView.setOnClickListener(this);
        btnPopupMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_OptionMenu01) {
            OptionMenuActivity.actionStart(MenuMainActivity.this);
        } else if (id == R.id.btn_OptionMenu02) {
            //OptionMenu2Activity.actionStart(MenuMainActivity.this);
            replaceFragment(OptionMenuFragment.newInstance());
        } else if (id == R.id.btn_ContextMenu) {
            ContextMenuActivity.actionStart(MenuMainActivity.this);
        } else if (id == R.id.btn_ListView_ContextMode) {
            ContextMenu2Activity.actionStart(MenuMainActivity.this);
        } else if (id == R.id.btn_RecycleView_ContextMode) {
            ContextMenu3Activity.actionStart(MenuMainActivity.this);
        } else if (id == R.id.btn_single_view) {
            ContextMenu4Activity.actionStart(MenuMainActivity.this);
        } else if (id == R.id.btn_PopupMenu) {
            PopupMenuActivity.actionStart(MenuMainActivity.this);
        }
    }

    private void replaceFragment(BaseFragmet fragmet){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_contianer, fragmet)
                .addToBackStack(null)
                .commit();
    }
}
