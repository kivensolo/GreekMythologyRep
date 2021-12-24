package com.zeke.demo.menu.bean;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.zeke.demo.menu.adapter.SelectionQuickAdapter;

/**
 * @ProjectName: MenuDemo
 * @Package: com.anand.menudemo.bean
 * @ClassName: MyItemDetailsLookup
 * @Description: 将RecycleView每个触摸通过MotionEvent下发到Item对应的ViewHolder信息
 * @Author: AnandLin
 * @CreateDate: 2019/8/30 13:55
 * @UpdateUser: AnandLin
 * @UpdateDate: 2019/8/30 13:55
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MyItemDetailsLookup extends ItemDetailsLookup<Long> {
    private RecyclerView recyclerView;

    public MyItemDetailsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent motionEvent) {
        View childViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if(childViewUnder != null){
            RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(childViewUnder);
            if(childViewHolder instanceof SelectionQuickAdapter.SelectionQickViewHolder){
                return ((SelectionQuickAdapter.SelectionQickViewHolder)childViewHolder).getItemDetails();
            }
        }
        return null;
    }
}
