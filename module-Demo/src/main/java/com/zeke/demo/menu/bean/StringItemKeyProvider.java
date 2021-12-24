package com.zeke.demo.menu.bean;


import androidx.recyclerview.selection.ItemKeyProvider;

import java.util.List;

/**
 * @ProjectName: MenuDemo
 * @Package: com.anand.menudemo.bean
 * @ClassName: StringItemKeyProvider
 * @Description: java类作用描述
 * @Author: AnandLin
 * @CreateDate: 2019/9/5 11:07
 * @UpdateUser: AnandLin
 * @UpdateDate: 2019/9/5 11:07
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class StringItemKeyProvider extends ItemKeyProvider<String> {

    private List<String> items;

    public StringItemKeyProvider(int scope,List<String> items) {
        super(scope);
        this.items = items;
    }

    @Override
    public String getKey(int position) {
        return items.get(position);
    }

    @Override
    public int getPosition(String key) {
        return items.indexOf(key);
    }
}
