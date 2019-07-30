package com.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.App;

/**
 * author：KingZ
 * date：2019/7/26
 * description：基类的Fragment
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return mActivity == null ? App.getAppInstance(): mActivity;
    }

    public boolean onBackPressed() {
        return true;
    }


}
