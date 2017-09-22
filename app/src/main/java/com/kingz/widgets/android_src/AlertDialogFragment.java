package com.kingz.widgets.android_src;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import com.kingz.customdemo.R;
import com.kingz.utils.ZLog;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/6/10 18:42
 * description:
 *      原生Dialog存在问题：
 *          在手机配置发生变化后（比如：旋屏后），变化之前显示的Dialog，变化之后不会显示，更别提Dialog状态的恢复了。
 *  管理自定义的Dialog和系统原生的Dialog麻烦.DialogFragment怎么解决Dialog存在的问题：
 *  DialogFragment说到底还是一个Fragment，因此它继承了Fragment的所有特性。
 *  同理{@link android.app.FragmentManager}会管理DialogFragment。在手机配置发生变化的时候，FragmentManager可以负责现场的恢复工作。
 *  调用DialogFragment的{@link android.app.Fragment#setArguments(Bundle)}方法进行数据的设置，可以保证DialogFragment的数据也能恢复。
 *  DialogFragment里的{@link android.app.DialogFragment#onCreate(Bundle)} 和
 *                      {@link android.app.DialogFragment#onCreateDialog(Bundle)} 2个方法，
 *                      {@link android.app.DialogFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}可以用来创建自定义Dialog，
 *  onCreateDIalog 可以用Dialog来创建系统原生Dialog。可以在一个类中管理2种不同的dialog。
 *
 *          [生命周期]
 *      Fragment is added                             对应activity的
 *              |                       ----------------------------------------
 *          onAttach()
*               |
 *          onCreate()                                   Created
 *              |
 *         onCreateView()
 *              |
 *       onActivityCreate()             -------------------------------------------
 *              |
 *           onStart()                                    Started
 *              |                       -------------------------------------------
 *           onResume()                                   Resumed
 *             |
 *    ---------------------
 *    | Fragment is active|
 *    ---------------------
 *       |             |
 * 用户向后导航或者    fragment被添加到
 * fragment被          后退栈，然后被
 * removed/replaced    removed/replaced
 *          |       |                   -------------------------------------------
 *          onPause()                                     Paused
 *          |       |                   -------------------------------------------
 *           onStop()                                     Stoped
 *          |       |                   -------------------------------------------
 *        onDestoryView()                                Destoryed
 *          |          |
 *     onDestory()     重回onCreateView ---->
 *         |
 *     onDetach()
 *         |
 *  Fragment is destory
 *
 */
public class AlertDialogFragment extends DialogFragment{

    private static final String TAG = "AlertDialogFragment";
    private boolean isUseCustomDialog = true;
    public static final String mConfirm = "确认";
    public static final String mCancle = "取消";


    /**
     * 实例化dialog对象
     * @param title     Dialog标题
     * @param message   内容文本
     * @param cancelable 是否可以取消
     * @return  AlertDialogFragment
     */
    public static AlertDialogFragment newInstance(String title, String message, boolean cancelable){
        AlertDialogFragment instance = new AlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("message",message);
        bundle.putBoolean("cancelable",cancelable);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onAttach(Activity activity) {
        ZLog.d(TAG,"onAttach()...");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ZLog.d(TAG,"onCreate()...");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ZLog.d(TAG,"onCreateView()...");
        if(!isUseCustomDialog){
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            return inflater.inflate(R.layout.custom_dialog,container);
        }else{
            return null;
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ZLog.d(TAG,"onActivityCreated()...");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        ZLog.d(TAG,"onStart()...");
        super.onStart();
    }

    /**
     *     完全自定义实现一个自定义的Dialog,重写onCreateDialog(Bundle savedInstanceState)方法
     *     This is most useful for creating an {@link AlertDialog}
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (isUseCustomDialog) {
            String title = getArguments().getString("title");
            String message = getArguments().getString("message");

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.custom_dialog, null);
            TextView titleView = (TextView) view.findViewById(R.id.dialog_title_id);
            TextView messageView = (TextView) view.findViewById(R.id.dialog_content_id);
            titleView.setText(title);
            titleView.setTextColor(getResources().getColor(R.color.black));
            messageView.setText(message);
            messageView.setTextColor(getResources().getColor(R.color.black));

            builder.setView(view).setPositiveButton(mConfirm,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doPositiveClick();
                        }
                    }).setNegativeButton(mCancle, null);
            return builder.create();
        } else {
            return null;
        }
    }


    @Override
    public void onResume() {
        ZLog.d(TAG,"onResume()...");
        super.onResume();
    }

    @Override
    public void onPause() {
        ZLog.d(TAG,"onPause()...");
        super.onPause();
    }
    @Override
    public void onStop() {
        ZLog.d(TAG,"onStop()...");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        ZLog.d(TAG,"onDestroyView()...");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        ZLog.d(TAG,"onDestroy()...");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        ZLog.d(TAG,"onDetach()...");
        super.onDetach();
    }

    public void doPositiveClick() {
        // Do stuff here.
        ZLog.i(TAG, "Positive click!");
    }

    public void doNegativeClick() {
        // Do stuff here.
        ZLog.i(TAG, "Negative click!");
    }

}
