package com.kingz.four_components.activity.news;

import android.content.Context;
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
import com.zeke.kangaroo.utils.ZLog;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/25
 * Discription:
 * 列表样式的Fragment
 */
public class TitleListFragment extends ListFragment {
    private static final String TAG = "TitleListFragment";
    public static final String KEY_CHOICE_POSTION = "curChoice";
    boolean mDualPane;
    int mCurCheckPosition = 0;
    private FragmentManager mManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 可以对填充布局的控件进行一些处理 如输入监听
        return inflater.inflate(R.layout.title_layout,container,false);
    }

    /************ inflate() 方法的三个参数 *********************
        1.想要扩展的布局的资源 ID；
        2.父容器的 ViewGroup
        3.是否将fragment布局附加至父容器。
          （一般为 false，因为系统已经将fragment插入container,
            传递true值会在最终布局中创建一个多余的视图组。）
     ***********************************************************/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_activated_1, NewsInfo.titleData));

        View contentView = getActivity().findViewById(R.id.news_content_layout);
        mDualPane = contentView != null && contentView.getVisibility() == View.VISIBLE;
        if (savedInstanceState != null) {
            mCurCheckPosition = savedInstanceState.getInt(KEY_CHOICE_POSTION, 0);
            Log.i(TAG,"mCurCheckPosition = "+mCurCheckPosition);
        }
        if (mDualPane) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showContent(mCurCheckPosition);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(position != mCurCheckPosition){
            showContent(position);
        }
    }

    /**
     * 显示内容
     * @param index list的idnex
     */
    private void showContent(int index) {
        if (mDualPane) { //双屏模式
            getListView().setItemChecked(index, true);
            ContentFragment contentFragment = (ContentFragment)mManager.findFragmentById(R.id.news_content_layout);
            if (contentFragment == null || contentFragment.getShownIndex() != index) {
                ContentFragment newFragment = ContentFragment.newInstance(index);
                FragmentTransaction transaction = mManager.beginTransaction();
                if (transaction != null) {
                    transaction.replace(R.id.news_content_layout, newFragment);
                    //transaction.addToBackStack(null); //添加至返回栈
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();
                }
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
