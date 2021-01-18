package com.kingz.pickerview

import com.contrarywind.interfaces.IPickerViewData

/**
 * author：ZekeWang
 * date：2021/1/18
 * description：省份的Bean对象
 * 注意：如果是添加Bean实体数据，则实体类需要实现 IPickerViewData 接口，
 */
data class ProvinceBean(
    var id: Long,
    var name: String,
    var description: String,
    var others: String
) : IPickerViewData {

    //这个用来显示在PickerView上面的字符串,PickerView会通过getPickerViewText方法获取字符串显示出来。
    override fun getPickerViewText(): String {
        return name
    }
}
