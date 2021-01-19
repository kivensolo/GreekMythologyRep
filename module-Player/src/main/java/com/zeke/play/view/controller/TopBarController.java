package com.zeke.play.view.controller;

import android.content.res.Configuration;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeke.module_player.R;

/**
 * author：KingZ
 * date：2019/7/31
 * description：顶部工具栏Controller
 */
public class TopBarController extends BaseController {
    private ImageView backView;         // 返回图标view
    private TextView titleTxtView;      // 影片TitleView
    private ImageView settingView;     // 设置view

    public TopBarController(View view) {
        this.rootView = view.findViewById(R.id.player_bar_top);
        this.backView = rootView.findViewById(R.id.back_tv);
        this.titleTxtView = rootView.findViewById(R.id.player_title_name);
        settingView = rootView.findViewById(R.id.player_setting);
    }

    public void setTitle(String txt) {
        titleTxtView.setText(txt);
        if (isShown()) {
            refreshTitle();
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        backView.setOnClickListener(listener);
        settingView.setOnClickListener(listener);
    }

    @Override
    public void show() {
        rootView.setVisibility(View.VISIBLE);
        refreshTitle();
    }

    private void refreshTitle(){
        int orientation = rootView.getResources().getConfiguration().orientation;
        //横屏才设置文字  竖屏不设置
        if(Configuration.ORIENTATION_LANDSCAPE == orientation){
            titleTxtView.setVisibility(View.VISIBLE);
            settingView.setVisibility(View.VISIBLE);
        }else{
            titleTxtView.setVisibility(View.INVISIBLE);
            settingView.setVisibility(View.INVISIBLE);
        }
    }
}
