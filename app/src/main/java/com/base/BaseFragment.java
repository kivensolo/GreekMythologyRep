package com.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingz.GreekLifeCycle;
import com.zeke.ktx.App;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author：KingZ
 * date：2019/7/26
 * description：基类的Fragment
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    private Unbinder unbinder;
    protected Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(new GreekLifeCycle(TAG));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void bindButterKnife(View view){
        unbinder = ButterKnife.bind(this,view);
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
    public void onDestroyView() {
        super.onDestroyView();
        //解绑ButterKnife
        if(unbinder != null){
            unbinder.unbind();
        }
    }

    @Override
    public Context getContext() {
        return mActivity == null ? App.Companion.getInstance().getApplicationContext(): mActivity.getBaseContext();
    }

    public boolean onBackPressed() {
        return false;
    }

    public void showPlayBufferTips() {}
    public void dismissPlayBufferTips() {}
}
