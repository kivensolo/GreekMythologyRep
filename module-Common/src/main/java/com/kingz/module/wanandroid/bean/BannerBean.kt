package com.kingz.module.wanandroid.bean

import android.os.Parcel
import android.os.Parcelable

data class BannerData(
    val itemList: MutableList<BannerItem>
)

/**
 * desc :
 * id : 6
 * imagePath : http://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png
 * isVisible : 1
 * order : 1
 * title : 我们新增了一个常用导航Tab~
 * type : 0
 * url : http://www.wanandroid.com/navi
 */
data class BannerItem(
    var desc: String?,       // 简单描述
    var id: Int,            // 文章id
    var imagePath: String?,  // 图片地址
    var isVisible: Int,     // 是否可见 1:可见
    var order: Int,         // 序号
    var title: String?,      // Title
    var type: Int,          // 类型  不知道是啥用
    var url: String?,
    @BannerType var viewType: Int = STYLE_PIC
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(desc)
        parcel.writeInt(id)
        parcel.writeString(imagePath)
        parcel.writeInt(isVisible)
        parcel.writeInt(order)
        parcel.writeString(title)
        parcel.writeInt(type)
        parcel.writeString(url)
        parcel.writeInt(viewType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BannerItem> {
        override fun createFromParcel(parcel: Parcel): BannerItem {
            return BannerItem(parcel)
        }

        override fun newArray(size: Int): Array<BannerItem?> {
            return arrayOfNulls(size)
        }
    }
}