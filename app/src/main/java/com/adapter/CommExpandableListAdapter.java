package com.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ListBillData;
import com.kingz.customdemo.R;
import com.widgets.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/8/10 19:02 <br>
 * description: 公共可收缩伸展列表适配器 <br>
 */
public class CommExpandableListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    private static String TAG = CommExpandableListAdapter.class.getSimpleName();
    private Context mContext = null;
    private List<String> groupList = null;
    private LayoutInflater mInflater;
    private List<List<ListBillData>> itemList = null;

    public CommExpandableListAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public CommExpandableListAdapter(Context context, Object group, Object child) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        initData(group, child);
    }

    private void initData(Object group, Object child) {
        addGroupData(group);
        addChildData(child);
    }
    /**
     * 添加父级分类数据
     * @param object
     */
    public void addGroupData(Object object){
        groupList = new ArrayList<>();
        if(object instanceof List){
            groupList = (List<String>) object;
        }
    }

    /**
     * 添加子分类数据
     * @param object
     */
    public void addChildData(Object object){
        itemList = new ArrayList<>();
        if(object instanceof List){
            itemList = (List<List<ListBillData>>) object;
        }
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    /**
     * when group Collapsed.
     */
    public void onGroupCollapsed(int groupPosition) {

    }
    /**
     *when group Expanded
     */
    public void onGroupExpanded(int groupPosition) {

    }

/************************  获取Group相关属性 *******************/
    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * getGroupView
     * @param groupPosition  groupPosition
     * @param isExpanded     isExpanded
     * @param convertView    convertView
     * @param parent         parent
     * @return Veiw
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder viewHolder;
        if(null == convertView){
            viewHolder = new GroupHolder();
            convertView = mInflater.inflate(R.layout.expand_group_layout,null,false);
            viewHolder.itemText = (TextView) convertView.findViewById(R.id.parent_group);
            viewHolder.indictorImg = (ImageView) convertView.findViewById(R.id.parent_group_img);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (GroupHolder) convertView.getTag();
        }
        viewHolder.itemText.setTextSize(20);
        viewHolder.itemText.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        viewHolder.itemText.setText(groupList.get(groupPosition));
        if(isExpanded){
            convertView.setBackground(mContext.getResources().getDrawable(R.color.fruitpurple));
            viewHolder.itemText.setTextColor(mContext.getResources().getColor(R.color.darkturquoise));
            viewHolder.indictorImg.setBackground(mContext.getResources().getDrawable(R.drawable.down_arrow));
        }else{
            convertView.setBackground(mContext.getResources().getDrawable(R.color.transparent));
            viewHolder.itemText.setTextColor(mContext.getResources().getColor(R.color.mediumaquamarine));
            viewHolder.indictorImg.setBackground(mContext.getResources().getDrawable(R.drawable.right_arrow));
        }
        return convertView;
    }

/************************  获取Child相关属性 *******************/
    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // 动画伸缩主要是在此进行处理的
        // AnimatedExpandableListView 重写的方法  父类为：getChildView
        ChildHolder viewHolder;
        if(null == convertView){
            viewHolder = new ChildHolder();
            convertView = mInflater.inflate(R.layout.expand_child_layout,null,false);
            viewHolder.itemText = (TextView) convertView.findViewById(R.id.child_group);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ChildHolder) convertView.getTag();
        }
        viewHolder.itemText.setTextSize(20);
        viewHolder.itemText.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        viewHolder.itemText.setText(itemList.get(groupPosition).get(childPosition).getUserName()) ;
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        if(groupPosition >= itemList.size()){
            return 0;
        }
        return itemList.get(groupPosition).size();
    }

    /**
     * 字节点视图
     */
//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//
//    }
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
//    @Override
//    public int getChildrenCount(int groupPosition) {
//        if(groupPosition >= itemList.size()){
//            return 0;
//        }
//        return itemList.get(groupPosition).size();
//    }

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

    private static class ChildHolder{
        public TextView itemText;
    }
    private static class GroupHolder{
        public TextView itemText;
        public ImageView indictorImg;
    }
}
