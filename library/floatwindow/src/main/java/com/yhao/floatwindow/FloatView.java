package com.yhao.floatwindow;

import android.view.View;

/**
 * 对Window进行的抽象封装
 */
abstract class FloatView {

    abstract void setSize(int width, int height);

    abstract void setView(View view);

    abstract void setGravity(int gravity, int xOffset, int yOffset);

    abstract void init();

    abstract void dismiss();

    void updateXY(int x, int y) {
    }

    void updateX(int x) {
    }

    void updateY(int y) {
    }

    int getX() {
        return 0;
    }

    int getY() {
        return 0;
    }
}
