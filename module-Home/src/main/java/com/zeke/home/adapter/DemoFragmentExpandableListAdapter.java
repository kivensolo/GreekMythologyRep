package com.zeke.home.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingz.module.home.R;
import com.zeke.home.entity.DemoGroup;
import com.zeke.kangaroo.view.animation.AnimatedExpandableListView;

import java.util.Collections;
import java.util.List;

/**
 * author: King.Z <br>
 * date:  2016/8/10 19:02 <br>
 * description: 可收缩伸展列表适配器 <br>
 */
public class DemoFragmentExpandableListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    private Context mContext = null;
    private LayoutInflater mInflater;
    private List<DemoGroup> sampleGroups = null;

    public DemoFragmentExpandableListAdapter(Context mContext) {
        this.mContext = mContext;
        sampleGroups = Collections.emptyList();
        mInflater = LayoutInflater.from(mContext);
    }

    public void setSampleGroups(List<DemoGroup> sampleGroups) {
        this.sampleGroups = sampleGroups;
        notifyDataSetChanged();
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
     * when group Expanded
     */
    public void onGroupExpanded(int groupPosition) {

    }

    /************************  获取Group相关属性 *******************/
    @Override
    public int getGroupCount() {
        return sampleGroups.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return sampleGroups.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * getGroupView
     *
     * @param groupPosition groupPosition
     * @param isExpanded    isExpanded
     * @param convertView   convertView
     * @param parent        parent
     * @return Veiw
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder viewHolder;
        if (null == convertView) {
            viewHolder = new GroupHolder();
            convertView = mInflater.inflate(R.layout.expand_group_layout,parent, false);
            viewHolder.itemText = convertView.findViewById(R.id.parent_group);
            viewHolder.indictorImg = convertView.findViewById(R.id.parent_group_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupHolder) convertView.getTag();
        }
        viewHolder.itemText.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
//        viewHolder.itemText.setText(groupList.get(groupPosition));
        viewHolder.itemText.setText(sampleGroups.get(groupPosition).getTitle());
        if (isExpanded) {
            convertView.setBackground(mContext.getResources().getDrawable(R.color.ic_green_light));
            viewHolder.itemText.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.indictorImg.setBackground(mContext.getResources().getDrawable(R.drawable.ic_down_arrow));
        } else {
            convertView.setBackground(mContext.getResources().getDrawable(R.color.transparent));
            viewHolder.itemText.setTextColor(mContext.getResources().getColor(R.color.black));
            viewHolder.indictorImg.setBackground(mContext.getResources().getDrawable(R.drawable.ic_right_arrow));
        }
        return convertView;
    }

    /************************  获取Child相关属性 *******************/
    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // 动画伸缩主要是在此进行处理的
        // AnimatedExpandableListView 重写的方法  父类为：getChildView
        ChildHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ChildHolder();
            convertView = mInflater.inflate(R.layout.expand_child_layout, null, false);
            viewHolder.itemText = convertView.findViewById(R.id.child_group);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildHolder) convertView.getTag();
        }
        viewHolder.itemText.setText(sampleGroups.get(groupPosition).getSampleByIndex(childPosition).getName());
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        if (groupPosition >= sampleGroups.size()) {
            return 0;
        }
        return sampleGroups.get(groupPosition).getSamples().size();
    }

    /**
     * 设置子节点对象，在事件处理时返回的对象，可存放一些数据
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return sampleGroups.get(groupPosition).getSamples().get(childPosition);
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
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    private static class ChildHolder {
        public TextView itemText;
    }

    private static class GroupHolder {
        public TextView itemText;
        public ImageView indictorImg;
    }

}
