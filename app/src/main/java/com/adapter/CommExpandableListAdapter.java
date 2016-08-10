package com.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/8/10 19:02 <br>
 * description: 公共的可收缩伸展列表事例 <br>
 */
public class CommExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext = null;
    // 测试数据，开发时可能来自数据库，网络....
    private String[] groups = {"家人", "朋友", "同事"};
    private String[] familis = {"老爸", "老妈", "妹妹"};
    private String[] friends = {"小李", "张三", "李四"};
    private String[] colleagues = {"陈总", "李工", "李客户"};

    private List<String> groupList = null;
    private List<List<String>> itemList = null;

    public CommExpandableListAdapter(Context context) {
        this.mContext = context;
        groupList = new ArrayList<String>();
        itemList = new ArrayList<List<String>>();
        initData();
    }

    /**
     * 初始化数据，将相关数据放到List中，方便处理
     */
    private void initData() {
        for (int i = 0; i < groups.length; i++) {
            groupList.add(groups[i]);
        }
        List<String> item1 = new ArrayList<String>();
        for (int i = 0; i < familis.length; i++) {
            item1.add(familis[i]);
        }

        List<String> item2 = new ArrayList<String>();
        for (int i = 0; i < friends.length; i++) {
            item2.add(friends[i]);
        }

        List<String> item3 = new ArrayList<String>();
        for (int i = 0; i < colleagues.length; i++) {
            item3.add(colleagues[i]);
        }

        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    /**
     * 收缩列表时要处理的东西都放这儿
     */
    public void onGroupCollapsed(int groupPosition) {

    }

    /**
     * 展开列表时要处理的东西都放这儿
     */
    public void onGroupExpanded(int groupPosition) {

    }

    /************************  获取Group相关属性 *******************/
    @Override
    public int getGroupCount() {
        return groupList.size();
    }
    /**
     * 返回分组对象，用于一些数据传递，在事件处理时可直接取得和分组相关的数据
     */
    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 分组视图，这里暂时也是一个文本视图
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView text = null;
        if (convertView == null) {
            text = new TextView(mContext);
        } else {
            text = (TextView) convertView;
        }
        String name = (String) groupList.get(groupPosition);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, 40);
        text.setLayoutParams(lp);
        text.setTextSize(18);
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        text.setPadding(36, 0, 0, 0);
        text.setText(name);
        return text;
    }

/************************  获取Child相关属性 *******************/
    /**
     * 字节点视图，这里我们显示一个文本对象
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView text = null;
        if (convertView == null) {
            text = new TextView(mContext);
        } else {
            text = (TextView) convertView;
        }
        // 获取子节点要显示的名称
        String name = itemList.get(groupPosition).get(childPosition);
        // 设置文本视图的相关属性
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, 40);
        text.setLayoutParams(lp);
        text.setTextSize(18);
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        text.setPadding(45, 0, 0, 0);
        text.setText(name);
        return text;
    }
    /**
     * 设置子节点对象，在事件处理时返回的对象，可存放一些数据
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itemList.get(groupPosition).get(childPosition);
    }

    /**
     * 返回当前分组的数据个数
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return itemList.get(groupPosition).size();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Indicates whether the child and group IDs are stable across changes to
     * the underlying data.
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }


    /**
     * Whether the child at the specified position is selectable.
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 分组是否为空
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }


}
