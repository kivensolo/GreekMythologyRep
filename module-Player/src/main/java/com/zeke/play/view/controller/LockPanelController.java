package com.zeke.play.view.controller;

import android.view.View;
import android.widget.ImageView;

import com.module.tools.ViewUtils;
import com.zeke.module_player.R;

/**
 * author：KingZ
 * date：2019/7/31
 * description：播放器锁屏画面控制器
 */

public class LockPanelController extends BaseController {
    private ImageView lockImg;  //锁屏按钮
    private boolean isLocked;  //目前是否处于锁屏模式

    public LockPanelController(View view) {
        rootView = view.findViewById(R.id.lock_layout);
        lockImg = rootView.findViewById(R.id.player_lock);
    }


    public void switchLockState() {
        isLocked = !isLocked;
        lockImg.setImageResource(isLocked ? R.drawable.ic_player_lock : R.drawable.ic_player_unlock);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        lockImg.setOnClickListener(listener);
    }

    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public void show() {
        lockImg.setVisibility(ViewUtils.isLandScape(rootView) ? View.VISIBLE : View.GONE);
        rootView.setVisibility(View.VISIBLE);
    }
}
