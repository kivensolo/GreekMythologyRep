package com.kingz.uiusingLayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kingz.uiusingListViews.R;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/25
 * Discription:【标题Fragment】创建提供布局的片段
 * 添加用户界面 要想为片段提供布局，必须实现 onCreateView() 回调方法，
 * Android 系统会在片段需要绘制其布局时调用该方法。
 * 此方法的实现返回的 View 必须是片段布局的根视图。
 * 注：如果片段是 ListFragment 的子类，则默认实现会从 onCreateView() 返回一个 ListView，因此无需实现它。
 */
public class TitleFragment extends Fragment {
    /**
     * @param inflater
     * @param container  片段布局将插入到的父ViewGroup（来自 Activity 的布局）
     * @param savedInstanceState savedInstanceState 参数是在恢复片段时，提供上一片段实例相关数据的 Bundle（处理片段生命周期部分对恢复状态做了详细阐述）。
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.title_layout,container,false);
    }
    /**
     * inflate() 方法的三个参数:
     *  1.您想要扩展的布局的资源 ID；
     *  2.将作为扩展布局父项的 ViewGroup 。传递 container 对系统向扩展布局的根视图（由其所属的父视图指定）应用布局参数具有重要意义；
     *  3.指示是否应该在扩展期间将扩展布局附加至 ViewGroup（第二个参数）的布尔值。
     *    （在本例中，其值为 false，因为系统已经将扩展布局插入container—传递true值会在最终布局中创建一个多余的视图组。）
     */

}
