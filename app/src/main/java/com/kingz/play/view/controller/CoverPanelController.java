package com.kingz.play.view.controller;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.kingz.utils.ViewTools;

/**
 * author：KingZ
 * date：2019/7/31
 * description：信息提示面板
 */

public class CoverPanelController  extends BaseController{
    private ImageView backImg;
    private TextView titleTxt;
    private TextView mTipsTxt;
    private ProgressBar progressBar;
    private TextView qualityTxt;
    private ImageView fullScreenImg;

    private String titleContent;

    public CoverPanelController(View view) {
        rootView = view.findViewById(R.id.player_cover_layout);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        progressBar = rootView.findViewById(R.id.rotate_loadding);
        mTipsTxt = rootView.findViewById(R.id.player_tips_txt);
        backImg = rootView.findViewById(R.id.cover_back);
        titleTxt = rootView.findViewById(R.id.player_title_name_cover);
        fullScreenImg = rootView.findViewById(R.id.img_fullscreen_cover);
        qualityTxt = rootView.findViewById(R.id.tv_quality_cover);
    }

    public void setTitle(String txt) {
        this.titleContent = txt;
        //在显示的时候  刷新显示
        if (isShown()) {
            show();
        }
    }

    /**
     * 播放完成的重新播放
     */
    public void showComplete(String tipsStr) {
        progressBar.setVisibility(View.GONE);
        mTipsTxt.setVisibility(View.VISIBLE);
        mTipsTxt.setText(tipsStr);
        show();
    }

    /**
     * 加载视频时候的展示
     */
    public void showLoading(String tipsStr) {
        progressBar.setVisibility(View.VISIBLE);
        mTipsTxt.setVisibility(View.VISIBLE);
        mTipsTxt.setText(tipsStr);
        show();
    }

    /**
     * 播放错误的展示
     */
    public void showError(String tipsStr) {
        progressBar.setVisibility(View.GONE);
        mTipsTxt.setVisibility(View.VISIBLE);
        mTipsTxt.setText(tipsStr);
        show();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        backImg.setOnClickListener(listener);
        fullScreenImg.setOnClickListener(listener);
        qualityTxt.setOnClickListener(listener);
    }


    @Override
    public void show() {
        if(ViewTools.isLandScape(rootView)){
            fullScreenImg.setVisibility(View.GONE);
            titleTxt.setText(titleContent);
        }else{
            fullScreenImg.setVisibility(View.VISIBLE);
            titleTxt.setText("");
        }
        qualityTxt.setVisibility(View.GONE);
        rootView.setVisibility(View.VISIBLE);
    }
}
