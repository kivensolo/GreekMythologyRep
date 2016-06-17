package com.kingz.controls;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import com.kingz.uiusingListViews.R;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/6/10 18:42
 * description:
 *      原生Dialog存在问题：
 *          在手机配置发生变化后（比如：旋屏后），变化之前显示的Dialog，变化之后不会显示，更别提Dialog状态的恢复了。
 *          管理自定义的Dialog和系统原生的Dialog麻烦
 *       DialogFragment怎么解决Dialog存在的问题：
 *          DialogFragment说到底还是一个Fragment，因此它继承了Fragment的所有特性。
 *              同理FragmentManager会管理DialogFragment。
 *              在手机配置发生变化的时候，FragmentManager可以负责现场的恢复工作。
 *              调用DialogFragment的setArguments(bundle)方法进行数据的设置，可以保证DialogFragment的数据也能恢复。
 *          DialogFragment里的onCreateView和onCreateDIalog 2个方法，onCreateView可以用来创建自定义Dialog，
 *              onCreateDIalog 可以用Dialog来创建系统原生Dialog。可以在一个类中管理2种不同的dialog。
 */
public class AlertDialogFragment extends DialogFragment{

    private static final String TAG = "AlertDialogFragment";

    /**
     * 实例化dialog对象
     * @param title
     * @param message
     * @param cancelable
     * @return
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        View view = inflater.inflate(R.layout.custom_dialog,container);
//        return view;
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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

        builder.setView(view).setPositiveButton("确认",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doPositiveClick();
                }
            }).setNegativeButton("取消",null);
        return builder.create();
    }

    public void doPositiveClick() {
        // Do stuff here.
        Log.i(TAG, "Positive click!");
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.i(TAG, "Negative click!");
    }

}
