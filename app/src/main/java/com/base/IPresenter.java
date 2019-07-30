package com.base;

/**
 * author：KingZ
 * date：2019/7/30
 * description：app 顶层Presenter对象
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
