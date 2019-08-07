package com.kingz.play.presenter;

import com.base.IPresenter;

/**
 * author：KingZ
 * date：2019/8/7
 * description：
 */
public abstract class AbsBasePresenter implements IPresenter {
    public static final String TAG = AbsBasePresenter.class.getSimpleName();

    public void onCreate(){}

    public void onCreateView(){}

    public void onStart(){}

    public void onResume(){}

    public void onPause(){}

    public void onStop(){}

    public void onDestroyView(){}

    public void onDestroy(){}
}
