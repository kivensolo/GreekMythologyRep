package com.base;

import android.view.View;

/**
 * author：KingZ
 * date：2019/7/30
 * description：app顶层view接口
 */
public interface IAppView {

    void setPresenter(IPresenter presenter);

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    void showError(View.OnClickListener listener);

    void showEmpty(View.OnClickListener listener);

    /**
     * 是否可见
     */
    boolean isShown();

    /**
     * 弹出吐司
     */
    void showMessage(String tips);

}
