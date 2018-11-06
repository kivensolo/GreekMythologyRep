package com.kingz.four_components.activity.news;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.kingz.customdemo.R;
import com.kingz.utils.ZLog;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/25
 * Discription:标题的Fragment
 * 需要在Activity中添加此Fragment
 * 创建提供布局的片段
 * 注：如果片段是 ListFragment 的子类，则默认实现会从 onCreateView() 返回一个 ListView，因此无需实现它。
 */
public class TitleFragmentDynamic extends ListFragment {
    private static final String TAG = "TitleFragmentDynamic";
    boolean mDualPane;
    int mCurCheckPosition = 0;
    private FragmentManager mManager;

    //@Override
    //public void onAttach(Context context) {
    //    ZLog.d(TAG,"onAttach()");
    //    super.onAttach(context);
    //}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ZLog.d(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        mManager = getFragmentManager();
        //TODO create other info
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 该方法生成fragment视图的布局，然后将生成的View返回给托管activity
        ZLog.d(TAG,"onCreateView()");
        View view = inflater.inflate(R.layout.title_layout,container,false);
        //TODO 可以对填充布局的控件进行一些处理 如输入监听
        return view;
    }

    /************ inflate() 方法的三个参数 *********************
        1.您想要扩展的布局的资源 ID；
        2.将作为扩展布局父项的 ViewGroup 。传递 container 对系统向扩展布局的根视图（由其所属的父视图指定）应用布局参数具有重要意义；
        3.指示是否应该在扩展期间将扩展布局附加至 ViewGroup（第二个参数）的布尔值。
          （在本例中，其值为 false，因为系统已经将扩展布局插入container—传递true值会在最终布局中创建一个多余的视图组。）
     ***********************************************************/

    /**
     * 在 Activity 的 onCreate() 方法已返回时调用
     * @param savedInstanceState  异常下保存的数据
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ZLog.d(TAG,"onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_activated_1, NewsInfo.titleData));

        //检查是否有内容的view 有则说明是横屏
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        if (savedInstanceState != null) {
            //Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
            Log.i(TAG,"mCurCheckPosition = "+mCurCheckPosition);
        }
        if (mDualPane) {
            //双屏模式对ListFragment进行设置
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(mCurCheckPosition);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(position != mCurCheckPosition){
            showDetails(position);
        }
    }

    private void showDetails(int index) {
        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            ContentFragmentDynamic details = (ContentFragmentDynamic)mManager.findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != index) {
                //初次初始化时，findFragmentById(R.id.details) 查找到的fragment为null
                details = ContentFragmentDynamic.newInstance(index);
                FragmentTransaction transaction = mManager.beginTransaction();
                transaction.replace(R.id.details, details);
                //transaction.addToBackStack(null); //添加至返回栈
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            }
        } else {
            ZLog.d(TAG,"单屏幕显示");
            //Intent intent = new Intent();
            //intent.setClass(getActivity(), XXXX.class);
            //intent.putExtra("index", index);
            //startActivity(intent);
        }
    }
}
