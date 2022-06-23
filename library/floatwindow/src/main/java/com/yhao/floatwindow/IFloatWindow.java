package com.yhao.floatwindow;

import android.view.View;

/**
 * Created by yhao on 2017/12/22.
 * https://github.com/yhaolpz
 */

public abstract class IFloatWindow {

    public abstract void changeMoveType(@MoveType.MOVE_TYPE int moveType, int slideLeftMargin, int slideRightMargin);

    public abstract void show();

    public abstract void hide();

    public abstract boolean isShowing();

    public abstract int getX();

    public abstract int getY();

    public abstract void updateX(int x);

    /**
     * 以屏幕宽或高的比更新X位置
     * @param screenType 尺寸参考，屏幕的宽或高
     * @param ratio 比例
     */
    public abstract void updateX(@Screen.screenType int screenType,float ratio);

    public abstract void updateY(int y);

    public abstract void updateY(@Screen.screenType int screenType,float ratio);

    public abstract void updateXY(int x, int y, boolean animation);

    public abstract void setTouchable(boolean enable);

    public abstract View getView();

    abstract void dismiss();
}
