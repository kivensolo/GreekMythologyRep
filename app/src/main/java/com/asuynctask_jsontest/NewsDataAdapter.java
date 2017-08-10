package com.asuynctask_jsontest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kingz.customdemo.R;

import java.util.List;

/**
 * Created by zhi.wang on 2015/11/7.
 * @Description: 数据适配器————文艺式！
 *              ListView的数据加载优化，监听滚动状态
 *
 */
public class NewsDataAdapter extends BaseAdapter implements AbsListView.OnScrollListener{

    private static final String TAG = "NewsDataAdapter";
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private List<NewsBean> mList;

    private int mFirstVisibleItem;          // 第一张可见图片的下标
    private int mVisibleItemCount;          // 一屏有多少张图片可见
    private boolean isFirstEnter = true;    //记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
    public static String[] URLS;            //用于保存当前所获得所有图片的URl地址

    public NewsDataAdapter(Context context, List<NewsBean> mList, ListView listView) {
        this.mList = mList;
        mInflater = LayoutInflater.from(context);
        mImageLoader = new ImageLoader(listView);
        URLS = new String[mList.size()];

        //把当前所见屏幕区域内的图片URl存放起来
        for (int i = 0; i < mList.size(); i++) {
            URLS[i] = mList.get(i).newspictureUrl;
        }
        //******** 注册滚动监听事件 不然onScrollStateChanged这些方法buuhi被调用*********
        listView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * View的重用就是文艺方法的使用
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG,"getView().......");
        //1.创建viewHodler
        ViewHodler viewHodler ;
        //2.判断旧的视图是否存在,若不存在，填充一个布局文件
        if(convertView == null){
            viewHodler =  new ViewHodler();
            convertView = mInflater.inflate(R.layout.lru_item_layout,null,false);
            viewHodler.pic_icon = (ImageView) convertView.findViewById(R.id.pic_icon);
            viewHodler.newsTitle = (TextView) convertView.findViewById(R.id.item_title);
            viewHodler.newsContent = (TextView) convertView.findViewById(R.id.item_content);
            //3.给视图贴上一个TAG（Object类型），便于以后查找
            convertView.setTag(viewHodler);
        }else{
            //4.若旧的视图存在，则直接获取到旧视图
            viewHodler = (ViewHodler) convertView.getTag();
        }

        String url = mList.get(position).newspictureUrl;
        viewHodler.pic_icon.setTag(url); //将url设置为tag
        // new ImageLoader().showImageByThread(viewHodler.pic_icon,url);
        mImageLoader.showImageByAsyncTask(viewHodler.pic_icon,url);
        //只从滚动的状态来进行后台下载，不从getView来进行下载，这里只进行缓存判断

        viewHodler.newsTitle.setText(mList.get(position).newsTitle);
        viewHodler.newsContent.setText(mList.get(position).newsContent);
        //6.返回视图
        return convertView;
    }



      /**************************  滑动加载优化  start************************/
     /**
     *      滚动状态改变监听（在状态改变的时候添加）
     * @param view：正在反馈的滚动状态的视图
     * @param scrollState：当前的滚动状态，为SCROLL_STATE_IDLE = 0         //静止  空闲
     *                                  SCROLL_STATE_TOUCH_SCROLL = 1； //触摸滚动,并且手指还在屏幕上
     *                                  SCROLL_STATE_FLING = 2；        //手指滑动后，离开屏幕，屏幕继续滚动到停止的状态
     *                  Ps：以前是在getView方法中来后台获取图片，现在改为了在滑动状态改变的时候才去新建Task获取图片数据
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //只有当静止的时候才去下载图片，滑动的时候停止下载任务
        if(scrollState == SCROLL_STATE_IDLE){
            Log.i(TAG,"滚动状态停止，执行loadStart2EndBitmaps方法");
            mImageLoader.loadStart2EndBitmaps(mFirstVisibleItem, mVisibleItemCount);
        }else{
            //停止所有任务
            mImageLoader.cancelAllTasks();
        }
    }

    /**
     * ***************滚动事件回调方法*****************
     *      在整个滑动过程中都会调用
     * @param view：正在反馈的滚动状态的视图
     * @param firstVisibleItem：第一次的可见单元格的索引 (如果忽略 visibleItemCount == 0)
     * @param visibleItemCount: 可见的单元格数量
     * @param totalItemCount：  list adaptor中的总item个数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
         // 下载的任务应该由onScrollStateChanged里调用，但首次进入程序时onScrollStateChanged并不会调用，
         // 因此在这里为首次进入程序开启下载任务。
        if (isFirstEnter && visibleItemCount > 0) {
            mImageLoader.loadStart2EndBitmaps(firstVisibleItem, visibleItemCount);
            isFirstEnter = false;
        }
    }

    /**
     * 匿名内部类ViewHodler
     * 用于储存ListView中的相关控件
     */
    class ViewHodler{
        public TextView newsTitle;
        public TextView newsContent;
        public ImageView pic_icon;
    }
}
