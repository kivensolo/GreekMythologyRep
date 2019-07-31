package com.kingz.play.view.controller;

import android.content.res.Configuration;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingz.customdemo.R;

/**
 * author：KingZ
 * date：2019/7/31
 * description：顶部工具栏Controller
 */
public class TopBarController extends BaseController{
    private ImageView backView;     // 返回图标view
    private TextView titleTxtView;      // 影片TitleView
    private String titleContent;

    public TopBarController(View view) {
        this.rootView = view.findViewById(R.id.player_bar_top);
        this.backView = rootView.findViewById(R.id.back_tv);
        this.titleTxtView = rootView.findViewById(R.id.player_title_name);
    }

    public void setTitle(String txt) {
        this.titleContent = txt;
        if (isShown()) {
            refreshTitle();
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        backView.setOnClickListener(listener);
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
            titleTxtView.setText(titleContent);
        }else{
            titleTxtView.setText("");
        }
    }
}
