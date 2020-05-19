package com.base;

import com.kingz.module.common.base.IAppView;

/**
 * author：KingZ
 * date：2019/7/30
 * description：app顶层Presenter对于Fragment主要生命周期的定义
 */
public interface IPresenter<T extends IAppView> {
    void onCreate();

    void onCreateView();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroyView();

    void onDestroy();
}
